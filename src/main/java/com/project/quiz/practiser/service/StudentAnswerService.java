package com.project.quiz.practiser.service;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.mongodb.client.result.UpdateResult;
import com.project.quiz.common.DefineCode;
import com.project.quiz.common.MyAssert;
import com.project.quiz.practiser.domain.BigQuestionAnswer;
import com.project.quiz.practiser.domain.QuestionsLists;
import com.project.quiz.practiser.domain.base.QuestionAnswer;
import com.project.quiz.practiser.web.req.AnswerReq;
import com.project.quiz.practiser.web.req.FindAnswerReq;
import com.project.quiz.practiser.web.req.StudentFindQuestionsReq;
import com.project.quiz.questionlibrary.domain.BigQuestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.project.quiz.common.Dic.VERIFY_STATUS_AGREE;
import static com.project.quiz.practiser.constant.Dic.IS_ANSWER_COMPLETED_N;
import static com.project.quiz.practiser.constant.Dic.IS_ANSWER_COMPLETED_Y;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;

/**
 * @author: zhangyy
 * @email: zhang10092009@hotmail.com
 * @date: 19-6-10 16:28
 * @version: 1.0
 * @description:
 */
@Service
public class StudentAnswerService {

    private final ReactiveMongoTemplate reactiveMongoTemplate;
    private final ReactiveRedisTemplate<String, String> reactiveRedisTemplate;

    public StudentAnswerService(ReactiveMongoTemplate reactiveMongoTemplate, ReactiveRedisTemplate<String, String> reactiveRedisTemplate) {
        this.reactiveMongoTemplate = reactiveMongoTemplate;
        this.reactiveRedisTemplate = reactiveRedisTemplate;
    }

    public Mono<List<QuestionAnswer>> findStudentQuestions(final StudentFindQuestionsReq req) {
        final String key = req.getKey();
        Mono<Boolean> booleanMono = reactiveRedisTemplate.hasKey(key);
        return booleanMono.flatMap(b -> {
            if (!b) {
                return findQuestions(req, key);
            } else {
                return findRedisQuestions(key).flatMap(list -> {
                    if (list.isEmpty()){
                        return findQuestions(req, key);
                    }
                    return Mono.just(list);
                });
            }
        });
    }

    private Mono<List<QuestionAnswer>> findQuestions(final StudentFindQuestionsReq req, final String key){
        return findBigQuestions(req)
                .filterWhen(list -> setRedisStudentQuestions(list, key))
                .flatMapMany(Flux::fromIterable)
                .collectList();
    }

    /**
     * 随机获取学生要回答的题目信息
     *
     * @param req
     * @return
     */
    private Mono<List<QuestionAnswer>> randomBigQuestions(final StudentFindQuestionsReq req) {
        //使用mongoDB 随机题目
        Criteria criteria =createCriteria(req.getChapterId(), req.getCourseId());
        criteria.and("verifyStatus").is(VERIFY_STATUS_AGREE);
        return reactiveMongoTemplate.aggregate(Aggregation.newAggregation(match(criteria),
                Aggregation.sample(req.getNumber())), "bigQuestion", BigQuestion.class)
                .map(bigQuestion -> {
                    QuestionAnswer questionAnswer = new QuestionAnswer();
                    BeanUtil.copyProperties(bigQuestion, questionAnswer);
                    return questionAnswer;
                })
                .collectList();
//        return list.flatMapMany(Flux::fromIterable)
//                .map(bigQuestion -> {
//                    QuestionAnswer questionAnswer = new QuestionAnswer();
//                    BeanUtil.copyProperties(bigQuestion, questionAnswer);
//                    return questionAnswer;
//                }).collectList();
    }

    /**
     * 保存学生回答需要回答的习题快照信息
     *
     * @param list
     * @param req
     * @return
     */
    private Mono<Boolean> setQuestions(final List<QuestionAnswer> list, final StudentFindQuestionsReq req) {
        Criteria criteria = buildCriteriaQuestionId(req.getChapterId(), req.getCourseId(), req.getClassId(), "", req.getStudentId());
        Update update = updateQuery(req.getChapterId(), "", req.getCourseId(), req.getClassId(), "", req.getStudentId());
        update.set("bigQuestions", list);
        update.set("isAnswerCompleted", IS_ANSWER_COMPLETED_N);
        return reactiveMongoTemplate.upsert(Query.query(criteria), update, QuestionsLists.class)
                .map(UpdateResult::wasAcknowledged)
                .flatMap(r -> MyAssert.isFalse(r, DefineCode.ERR0012, "保存失败"));
    }

//    private QuestionAnswer setAnswerIsNull(QuestionAnswer questionAnswer) {
//        questionAnswer.setAnalysis("");
//        questionAnswer.setAnswer("");
//        return questionAnswer;
//    }

    /**
     * 查询学生需要回答习题信息，如果没有快照，去获取随机题库获取题目集合
     *
     * @param req
     * @return
     */
    private Mono<List<QuestionAnswer>> findBigQuestions(final StudentFindQuestionsReq req) {
        Criteria criteria = createCriteria(req.getChapterId(), req.getCourseId());
        if (StrUtil.isNotBlank(req.getStudentId())) {
            criteria.and("studentId").is(req.getStudentId());
        }
        return reactiveMongoTemplate.findOne(Query.query(criteria), QuestionsLists.class)
                .switchIfEmpty(Mono.justOrEmpty(new QuestionsLists()))
                .flatMap(questionsLists -> {
                    if ((questionsLists.getBigQuestions() != null) && !questionsLists.getBigQuestions().isEmpty()) {
                        return Mono.just(questionsLists.getBigQuestions());
                    } else {
                        return randomBigQuestions(req).filterWhen(list -> setQuestions(list, req));
                    }
                });
    }

    /**
     * 查询题目保存快照信息
     *
     * @param key
     * @return
     */
    private Mono<List<QuestionAnswer>> findRedisQuestions(final String key) {
        return reactiveRedisTemplate.opsForValue().get(key).flatMap(s -> Mono.just(JSONUtil.toList(JSONUtil.parseArray(s), QuestionAnswer.class)));
    }

    /**
     * 设置redis保存快照题目信息
     *
     * @param bigQuestions
     * @param key
     * @return
     */
    private Mono<Boolean> setRedisStudentQuestions(final List<QuestionAnswer> bigQuestions, final String key) {
        if (bigQuestions.isEmpty()) {
            return Mono.just(true);
        }else {
            return reactiveRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(bigQuestions), Duration.ofHours(24));
        }
    }

    /**
     * 保存学生回答的练习和判断学生回答的结果
     *
     * @param req
     * @return
     */
    public Mono<Boolean> saveStudentAnswer(final AnswerReq req) {
        final String key = req.getKey();
        return findRedisQuestions(key)
                .flatMapMany(Flux::fromIterable)
                .filter(questionAnswer -> req.getQuestionId().equals(questionAnswer.getId()))
                .next()
                .switchIfEmpty(Mono.justOrEmpty(new QuestionAnswer()))
                .flatMap(questionAnswer -> {
                    MyAssert.isNull(questionAnswer.getId(), DefineCode.ERR0010, "不存在相关习题信息");
                    return checkResult(questionAnswer.getAnswer(), req.getStuAnswer())
                            .flatMap(b -> {
                                Criteria criteria = buildCriteriaQuestionId(req.getChapterId(), req.getCourseId(), "", req.getQuestionId(), req.getStudentId());
                                Update update = updateQuery(req.getChapterId(), "", req.getCourseId(), "", req.getQuestionId(), req.getStudentId());
                                update.set("right", b);
                                update.set("stuAnswer", req.getStuAnswer());
                                update.set("bigQuestion", questionAnswer);
                                return reactiveMongoTemplate.upsert(Query.query(criteria), update, BigQuestionAnswer.class)
                                        .map(UpdateResult::wasAcknowledged)
                                        .flatMap(r -> MyAssert.isFalse(r, DefineCode.ERR0012, "保存失败!"))
                                        .map(o -> b)
                                        .filterWhen(r -> addQuestionsAnswer(req, b))
                                        .filterWhen(r -> updateRedisDate(key, req));
                            });
                });
    }

    private Mono<Boolean> updateRedisDate(String key, final AnswerReq req) {
        Criteria criteria = createCriteria(req.getChapterId(), "");
        if (StrUtil.isNotBlank(req.getStudentId())) {
            criteria.and("studentId").is(req.getStudentId());
        }
        return reactiveMongoTemplate.findOne(Query.query(criteria), QuestionsLists.class)
                .switchIfEmpty(Mono.justOrEmpty(new QuestionsLists()))
                .flatMap(questionsLists -> {
                    if ((questionsLists.getBigQuestions() != null) && !questionsLists.getBigQuestions().isEmpty()) {
                        return setRedisStudentQuestions(questionsLists.getBigQuestions(), key);
                    }
                    return Mono.just(true);
                });
    }

    private Mono<Boolean> addQuestionsAnswer(final AnswerReq req, final Boolean right) {
        Criteria criteria = buildCriteria(req.getChapterId(), req.getCourseId(), "", req.getStudentId());
        Mono<QuestionsLists> questionsListsMono = reactiveMongoTemplate.findOne(Query.query(criteria), QuestionsLists.class)
                .switchIfEmpty(Mono.just(new QuestionsLists()));
        Mono<Long> setMono = questionsListsMono.filter(Objects::nonNull)
                .flatMap(questionsLists -> {
                    if (questionsLists.getQuestionIds() == null) {
                        return Mono.just(1L);
                    } else {
                        Set<String> set = new HashSet<>(questionsLists.getQuestionIds());
                        set.add(req.getQuestionId());
                        return Mono.just((long) set.size());
                    }
                });
        Mono<Long> longMono = questionsListsMono
                .filter(questionsLists -> questionsLists.getBigQuestions() != null)
                .map(QuestionsLists::getBigQuestions)
                .flatMapMany(Flux::fromIterable)
                .map(BigQuestion::getId)
                .count();
        return setMono.zipWith(longMono)
                .flatMap(t -> {
                    criteria.and("bigQuestions._id").is(req.getQuestionId());
                    Update update = Update.update("uDate", DateUtil.now());
                    update.addToSet("questionIds", req.getQuestionId());
                    if (t.getT1().intValue() == t.getT2().intValue()) {
                        update.set("isAnswerCompleted", IS_ANSWER_COMPLETED_Y);
                    }
                    if (right != null) {
                        update.set("bigQuestions.$.right", right);
                    }
                    if (StrUtil.isNotBlank(req.getStuAnswer())) {
                        update.set("bigQuestions.$.stuAnswer", req.getStuAnswer());
                    }
                    return reactiveMongoTemplate.upsert(Query.query(criteria), update, QuestionsLists.class)
                            .map(UpdateResult::wasAcknowledged)
                            .flatMap(r -> MyAssert.isFalse(r, DefineCode.ERR0012, "保存失败"));
                });
    }

    /**
     * 判断学生答题结果
     *
     * @param answer
     * @param stuAnswer
     * @return
     */
    private Mono<Boolean> checkResult(final String answer, final String stuAnswer) {
        if (answer.equals(stuAnswer)) {
            return Mono.just(true);
        } else {
            return Mono.just(false);
        }
    }

    /**
     * 分页查询学生回答的结果及对应的习题
     *
     * @param req
     * @return
     */
    public Mono<Page<BigQuestionAnswer>> findAnswerStudent(final FindAnswerReq req) {
        Query query = Query.query(buildCriteriaQuestionId(req.getChapterId(), req.getCourseId(), req.getClassId(), req.getQuestionId(), req.getStudentId()));
        req.queryPaging(query);
        Mono<Long> count = reactiveMongoTemplate.count(query, BigQuestionAnswer.class);
        Mono<List<BigQuestionAnswer>> list = reactiveMongoTemplate.find(query, BigQuestionAnswer.class).collectList();
        return list.zipWith(count).map(t -> new PageImpl<>(t.getT1(), PageRequest.of(req.getPage(), req.getSize()), t.getT2()));
    }

    public Mono<Page<QuestionsLists>> findStudentAnswerStudent(final FindAnswerReq req) {
        Query query = Query.query(buildCriteria(req.getChapterId(), req.getCourseId(), req.getClassId(), req.getStudentId()));
        req.queryPaging(query);
        Mono<Long> count = reactiveMongoTemplate.count(query, QuestionsLists.class);
        Mono<List<QuestionsLists>> list = reactiveMongoTemplate.find(query, QuestionsLists.class).collectList();
        return list.zipWith(count).map(t -> new PageImpl<>(t.getT1(), PageRequest.of(req.getPage(), req.getSize()), t.getT2()));
    }

    private Criteria createCriteria(final String chapterId, final String courseId) {
        Criteria criteria = new Criteria();
        if (StrUtil.isNotBlank(chapterId)) {
            criteria.and("chapterId").is(chapterId);
        }
        if (StrUtil.isNotBlank(courseId)) {
            criteria.and("courseId").is(courseId);
        }
        return criteria;
    }

    /**
     * 设置查询条件
     *
     * @return
     */
    private Criteria buildCriteriaQuestionId(final String chapterId, final String courseId, final String classId, final String questionId, final String studentId) {
        Criteria criteria = buildCriteria(chapterId, courseId, classId, studentId);
        if (StrUtil.isNotBlank(questionId)) {
            criteria.and("questionId").is(questionId);
        }
        return criteria;
    }

    private Criteria buildCriteria(final String chapterId, final String courseId, final String classId, final String studentId) {
        Criteria criteria = createCriteria(chapterId, courseId);
        if (StrUtil.isNotBlank(classId)) {
            criteria.and("classId").is(classId);
        }
        if (StrUtil.isNotBlank(studentId)) {
            criteria.and("studentId").is(studentId);
        }
        return criteria;
    }

    private Update updateQuery(final String chapterId, final String chapterName, final String courseId, final String classId, final String questionId, final String studentId) {
        // 修改答题记录
        Update update = Update.update("uDate", DateUtil.now());
        if (StrUtil.isNotBlank(chapterId)) {
            update.set("chapterId", chapterId);
        }
        if (StrUtil.isNotBlank(courseId)) {
            update.set("courseId", courseId);
        }
        if (StrUtil.isNotBlank(questionId)) {
            update.set("questionId", questionId);
        }
        if (StrUtil.isNotBlank(studentId)) {
            update.set("studentId", studentId);
        }
        if (StrUtil.isNotBlank(classId)) {
            update.set("classId", classId);
        }
        if (StrUtil.isNotBlank(studentId)) {
            update.set("studentId", studentId);
        }
        if (StrUtil.isNotBlank(chapterName)) {
            update.set("chapterName", chapterName);
        }
        return update;
    }
}