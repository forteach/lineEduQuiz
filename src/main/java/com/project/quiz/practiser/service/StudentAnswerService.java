package com.project.quiz.practiser.service;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.mongodb.client.result.UpdateResult;
import com.project.quiz.common.DefineCode;
import com.project.quiz.common.MyAssert;
import com.project.quiz.practiser.domain.BigQuestionAnswer;
import com.project.quiz.practiser.domain.QuestionsLists;
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
import java.util.*;

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

    public Mono<List<BigQuestion>> findStudentQuestions(final StudentFindQuestionsReq req) {
        final String key = req.getKey();
        Mono<Boolean> booleanMono = reactiveRedisTemplate.hasKey(key);
        return booleanMono.flatMap(b -> {
            if (!b) {
                return findBigQuestions(req, key)
                        .filterWhen(bigQuestions -> setRedisStudentQuestions(bigQuestions, key))
                        .flatMapMany(Flux::fromIterable)
                        .map(this::setAnswerIsNull)
                        .collectList();
            } else {
                return findRedisQuestions(key);
            }
        });
    }

    /**
     * 随机获取学生要回答的题目信息
     *
     * @param req
     * @param key
     * @return
     */
    private Mono<List<BigQuestion>> randomBigQuestions(final StudentFindQuestionsReq req, final String key) {
        final Criteria criteria = createCriteria(req.getChapterId(), req.getCourseId());
        Aggregation aggregation = Aggregation.newAggregation(match(criteria), Aggregation.sample(req.getNumber()));
        return reactiveMongoTemplate.aggregate(aggregation, "bigQuestion", BigQuestion.class)
                .collectList()
                .filterWhen(list -> setRedisStudentQuestions(list, key))
                .filterWhen(list -> setQuestions(list, req))
                .flatMapMany(Flux::fromIterable)
                .map(this::setAnswerIsNull)
                .collectList();
    }

    /**
     * 保存学生回答需要回答的习题快照信息
     *
     * @param list
     * @param req
     * @return
     */
    private Mono<Boolean> setQuestions(final List<BigQuestion> list, final StudentFindQuestionsReq req) {
        Criteria criteria = buildCriteriaQuestionId(req.getChapterId(), req.getCourseId(), req.getClassId(), null, req.getStudentId());
        Update update = updateQuery(req.getChapterId(), req.getCourseId(), req.getClassId(), null, req.getStudentId());
        update.set("bigQuestions", list);
        update.set("isAnswerCompleted", IS_ANSWER_COMPLETED_N);
        return reactiveMongoTemplate.upsert(Query.query(criteria), update, QuestionsLists.class)
                .map(UpdateResult::wasAcknowledged)
                .flatMap(r -> MyAssert.isFalse(r, DefineCode.ERR0012, "保存失败"));
    }

    private BigQuestion setAnswerIsNull(BigQuestion bigQuestion) {
        bigQuestion.setAnalysis("");
        bigQuestion.setAnswer("");
        return bigQuestion;
    }

    /**
     * 查询学生需要回答习题信息，如果没有快照，去获取随机题库获取题目集合
     *
     * @param req
     * @param key
     * @return
     */
    private Mono<List<BigQuestion>> findBigQuestions(final StudentFindQuestionsReq req, final String key) {
        Criteria criteria = createCriteria(req.getChapterId(), req.getCourseId());
        if (StrUtil.isNotBlank(req.getStudentId())) {
            criteria.and("studentId").is(req.getStudentId());
        }
        return reactiveMongoTemplate.findOne(Query.query(criteria), QuestionsLists.class)
                .switchIfEmpty(Mono.justOrEmpty(new QuestionsLists()))
                .flatMap(questionsLists -> {
                    if (questionsLists.getBigQuestions() != null && questionsLists.getBigQuestions().size() > 0) {
                        return Mono.just(questionsLists.getBigQuestions());
                    } else {
                        return randomBigQuestions(req, key);
                    }
                });
    }

    /**
     * 查询题目保存快照信息
     *
     * @param key
     * @return
     */
    private Mono<List<BigQuestion>> findRedisQuestions(final String key) {
        return reactiveRedisTemplate
                .opsForValue()
                .get(key)
                .flatMap(s -> Mono.just(JSONUtil.toList(JSONUtil.parseArray(s), BigQuestion.class)))
                .flatMapMany(Flux::fromIterable)
                .map(this::setAnswerIsNull)
                .collectList();
    }

    /**
     * 设置redis保存快照题目信息
     *
     * @param bigQuestions
     * @param key
     * @return
     */
    private Mono<Boolean> setRedisStudentQuestions(final List<BigQuestion> bigQuestions, final String key) {
        return reactiveRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(bigQuestions), Duration.ofHours(24));
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
                .filter(bigQuestion -> req.getQuestionId().equals(bigQuestion.getId()))
                .next()
                .switchIfEmpty(Mono.justOrEmpty(new BigQuestion()))
                .flatMap(bigQuestion -> {
                    MyAssert.isNull(bigQuestion.getId(), DefineCode.ERR0010, "不存在相关习题信息");
                    return checkResult(bigQuestion.getAnswer(), req.getStuAnswer())
                            .flatMap(b -> {
                                Criteria criteria = buildCriteriaQuestionId(req.getChapterId(), req.getCourseId(), req.getClassId(), req.getQuestionId(), req.getStudentId());
                                Update update = updateQuery(req.getChapterId(), req.getCourseId(), req.getClassId(), req.getQuestionId(), req.getStudentId());
                                update.set("right", b);
                                update.set("bigQuestion", bigQuestion);
                                return reactiveMongoTemplate.upsert(Query.query(criteria), update, BigQuestionAnswer.class)
                                        .map(UpdateResult::wasAcknowledged)
                                        .flatMap(r -> MyAssert.isFalse(r, DefineCode.ERR0012, "保存失败"))
                                        .filterWhen(r -> addQuestionsAnswer(req));
                            });
                });
    }

    private Mono<Boolean> addQuestionsAnswer(final AnswerReq req) {
        Criteria criteria = buildCriteria(req.getChapterId(), req.getCourseId(), req.getClassId(), req.getStudentId());
        Update update = Update.update("uDate", DateUtil.now());
        update.addToSet("questionIds", req.getQuestionId());
        Mono<QuestionsLists> questionsListsMono = reactiveMongoTemplate.findOne(Query.query(criteria), QuestionsLists.class);
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
        Mono<Long> longMono = questionsListsMono.map(QuestionsLists::getBigQuestions).flatMapMany(Flux::fromIterable).map(BigQuestion::getId).count();
        return setMono.zipWith(longMono)
                .flatMap(t -> {
                    if (t.getT1().longValue() == t.getT2().longValue()) {
                        update.set("isAnswerCompleted", IS_ANSWER_COMPLETED_Y);
                    }
                    return reactiveMongoTemplate.upsert(Query.query(criteria), update, QuestionsLists.class).map(UpdateResult::wasAcknowledged);
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

    private Update updateQuery(final String chapterId, final String courseId, final String classId, final String questionId, final String studentId) {
        // 修改答题记录
        Update update = Update.update("uDate", DateUtil.formatDateTime(new Date()));
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
        return update;
    }
}