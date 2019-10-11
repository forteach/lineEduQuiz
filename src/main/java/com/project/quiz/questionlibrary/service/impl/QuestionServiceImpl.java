package com.project.quiz.questionlibrary.service.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.mongodb.client.result.UpdateResult;
import com.project.quiz.common.DefineCode;
import com.project.quiz.common.MyAssert;
import com.project.quiz.exceptions.CustomException;
import com.project.quiz.exceptions.ExamQuestionsException;
import com.project.quiz.questionlibrary.domain.BigQuestion;
import com.project.quiz.questionlibrary.domain.base.AbstractExam;
import com.project.quiz.questionlibrary.reflect.QuestionReflect;
import com.project.quiz.questionlibrary.repository.base.BaseQuestionMongoRepository;
import com.project.quiz.questionlibrary.service.QuestionService;
import com.project.quiz.questionlibrary.web.req.FindQuestionsReq;
import com.project.quiz.questionlibrary.web.req.QuestionBankReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.ParameterizedType;
import java.time.Duration;
import java.util.List;
import java.util.Map;

import static com.project.quiz.common.Dic.*;

/**
 * @Description: Question 结构的通用实现
 * @author: liu zhenming
 * @version: V1.0
 * @date: 2019/1/10  11:53
 */
@Service
@Slf4j
public class QuestionServiceImpl<T extends AbstractExam> implements QuestionService<T> {

    private final BaseQuestionMongoRepository repository;
    private final ReactiveMongoTemplate reactiveMongoTemplate;
    private final ReactiveRedisTemplate reactiveRedisTemplate;
    private final QuestionReflect questionReflect;

    public QuestionServiceImpl(ReactiveMongoTemplate reactiveMongoTemplate, ReactiveRedisTemplate reactiveRedisTemplate,
                               QuestionReflect questionReflect,
                               BaseQuestionMongoRepository repository) {
        this.repository = repository;
        this.reactiveMongoTemplate = reactiveMongoTemplate;
        this.questionReflect = questionReflect;
        this.reactiveRedisTemplate = reactiveRedisTemplate;
    }

    /**
     * 修改题目
     *
     * @param bigQuestion
     * @return
     */
    @Override
    public Mono<T> editQuestion(final T bigQuestion, final Class obj) {
        return editQuestion(setQuestionUUID(bigQuestion, obj)).flatMap(t -> {
            Mono<UpdateResult> questionBankMono = questionBankAssociation(t.getId(), t.getTeacherId());
            return questionBankMono.flatMap(
                    updateResult -> {
                        if (updateResult.isModifiedCountAvailable()) {
                            return Mono.just(t);
                        } else {
                            return Mono.error(new ExamQuestionsException("保存 题目 作者失败"));
                        }
                    }
            );
        });
    }

    @Override
    public Mono<T> editQuestion(final T bigQuestion) {
        bigQuestion.setUDate(DateUtil.now());
        return repository.save(bigQuestion);
    }

    /**
     * 设置问题id
     *
     * @param bigQuestion
     * @return
     */
    private T setQuestionUUID(final T bigQuestion, final Class obj) {
        questionReflect.buildAttribute(JSON.parseObject(JSON.toJSONString(bigQuestion), obj));
        return bigQuestion;
    }

    /**
     * 更新题目与教师关系信息
     *
     * @param questionBankId
     * @param teacherId
     * @return
     */
    @Override
    public Mono<UpdateResult> questionBankAssociation(final String questionBankId, final String teacherId) {
        return reactiveMongoTemplate.upsert(Query.query(Criteria.where(MONGDB_ID).is(questionBankId)),
                new Update().set(MONGDB_COLUMN_QUESTION_TEACHER_ID, teacherId), AbstractExam.class);
    }

    /**
     * 题目分享
     *
     * @param questionBankId
     * @param teacherId
     * @return Mono<Boolean>
     */
    @Override
    public Mono<Boolean> questionBankAssociationAdd(final String questionBankId, final String teacherId) {
        return questionBankAssociation(questionBankId, teacherId).map(UpdateResult::isModifiedCountAvailable);
    }

    /**
     * 获取泛型的class
     *
     * @return
     */
    private Class<T> entityClass() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * 删除题目关系
     *
     * @param id
     * @return
     */
//    private Mono<DeleteResult> delBankAssociation(final List<String> id) {
//        return reactiveMongoTemplate.remove(Query.query(Criteria.where(MONGDB_ID).is(id)), BigQuestion.class);
//    }

    /**
     * 删除单道题
     *
     * @param id
     * @return
     */
    @Override
    public Mono<Boolean> delQuestion(final String id) {
        return repository.deleteById(id)
                .doOnError(b -> MyAssert.isFalse(false, DefineCode.ERR0012, "删除失败"))
                .then(Mono.just(true));
    }

    /**
     * 查询详细或全部字段的问题
     *
     * @param sortVo
     * @return
     */
    @Override
    public Flux<T> findAllDetailed(final QuestionBankReq sortVo) {

        if (PARAMETER_PART.equals(sortVo.getAllOrPart())) {
            return findPartQuestion(sortVo);
        } else if (PARAMETER_ALL.equals(sortVo.getAllOrPart())) {
            return findAllQuestion(sortVo);
        }
        return Flux.error(new ExamQuestionsException("错误的查询条件"));
    }

    /**
     * 查询问题,只返回部分字段
     *
     * @param sortVo
     * @return
     */
    private Flux<T> findPartQuestion(final QuestionBankReq sortVo) {

        Query query = buildFindQuestion(sortVo);

        query.fields()
                .include(MONGDB_ID)
                .include("courseId")
                .include("chapterId")
                .include("levelId")
                .include("examType")
                .include("teacherId")
                .include("uDate");

        sortVo.queryPaging(query);

        return reactiveMongoTemplate.find(query, entityClass()).switchIfEmpty(Flux.just());
    }

    /**
     * 查询所有详细的问题
     *
     * @param sortVo
     * @return
     */
    private Flux<T> findAllQuestion(final QuestionBankReq sortVo) {

        Query query = buildFindQuestion(sortVo);

        sortVo.queryPaging(query);

        return reactiveMongoTemplate.find(query, entityClass()).switchIfEmpty(Flux.just());
    }

    /**
     * 构建查询问题条件
     *
     * @param sortVo
     * @return
     */
    @SuppressWarnings(value = "all")
    private Query buildFindQuestion(final QuestionBankReq sortVo) {
        Criteria criteria = Criteria.where("teacherId").is(sortVo.getOperatorId());
        if (StrUtil.isNotEmpty(sortVo.getLevelId())) {
            criteria.and("levelId").is(sortVo.getLevelId());
        }
        if (StrUtil.isNotEmpty(sortVo.getChapterId())) {
            criteria.and("chapterId").is(sortVo.getChapterId());
        }
        if (StrUtil.isNotEmpty(sortVo.getCourseId())) {
            criteria.and("courseId").is(sortVo.getCourseId());
        }
        if (StrUtil.isNotEmpty(sortVo.getExamType())) {
            criteria.and("examType").is(sortVo.getExamType());
        }
        return new Query(criteria);
    }

    /**
     * 根据id查询详细
     *
     * @param id
     * @return
     */
    @Override
    public Mono<T> findOneDetailed(final String id) {
        return repository.findById(id).switchIfEmpty(Mono.error(new CustomException("没有找到考题")));
    }

    /**
     * 查找题 in
     * <p>
     * //     * @param ids
     *
     * @return
     */
    @Override
    public Flux<T> findBigQuestionInId(final List<String> ids) {
        return reactiveMongoTemplate.find(Query.query(Criteria.where(MONGDB_ID).in(ids)), entityClass());
    }

    @Override
    public Mono<Boolean> editBigQuestion(BigQuestion bigQuestion) {
        return Mono.just(bigQuestion).flatMap(b -> {
            if (StrUtil.isBlank(b.getId())) {
                b.setId(IdUtil.objectId());
            }
            return reactiveRedisTemplate.opsForSet().add(QUESTIONS_VERIFY, b.getId()).log("type")
                    .doOnError(o -> MyAssert.isNull(null, DefineCode.ERR0010, "添加记录失败"))
                    .map(o -> {
                        if (Integer.parseInt(String.valueOf(o)) == 0) {
                            return false;
                        }
                        return true;
                    })
                    .filterWhen(o -> {
                        Map<String, String> map = CollUtil.newHashMap();
                        map.put("questionId", b.getId());
                        map.put("courseId", b.getCourseId());
                        map.put("courseName", b.getCourseName());
                        map.put("teacherId", b.getTeacherId());
                        map.put("teacherName", b.getTeacherName());
                        map.put("centerAreaId", b.getCenterAreaId());
                        map.put("centerName", b.getCenterName());
                        map.put("chapterId", b.getChapterId());
                        map.put("choiceQstTxt", b.getChoiceQstTxt());
                        return reactiveRedisTemplate.opsForHash().putAll(QUESTION_CHAPTER.concat(b.getId()), map).flatMap(t -> {
                            return reactiveRedisTemplate.expire(QUESTION_CHAPTER.concat(b.getId()), Duration.ofDays(14));
                        });
                    }).flatMap(o -> {
                        return reactiveMongoTemplate.save(bigQuestion).map(obj -> {
                            MyAssert.isNull(obj, DefineCode.ERR0013, "保存失败");
                            return true;
                        });
                    });
        });
    }

    @Override
    public Mono<BigQuestion> findQuestionById(String questionId) {
        return reactiveMongoTemplate.findById(questionId, BigQuestion.class)
                .switchIfEmpty(Mono.error(new CustomException("没有找到考题")));
    }

    @Override
    public Mono<Page<BigQuestion>> findPageAll(final FindQuestionsReq req) {
        return findBigQuestion(setWherePageAll(req.getCourseId(), req.getChapterId(), req.getTeacherId(), req.getExamType(), VERIFY_STATUS_AGREE), req);
    }

    @Override
    public Mono<Page<BigQuestion>> findAllPageQuestion(FindQuestionsReq req) {
        return findBigQuestion(setWherePageAll(req.getCourseId(), req.getChapterId(), req.getTeacherId(), req.getExamType(), ""), req);
    }

    private Mono<Page<BigQuestion>> findBigQuestion(Query query, final FindQuestionsReq req) {
        req.queryPaging(query);
        Mono<Long> count = reactiveMongoTemplate.count(query, BigQuestion.class);
        Mono<List<BigQuestion>> list = reactiveMongoTemplate.find(query, BigQuestion.class).collectList();
        return list.zipWith(count).map(t -> new PageImpl<>(t.getT1(), PageRequest.of(req.getPage(), req.getSize()), t.getT2()));
    }

    private Query setWherePageAll(final String courseId, final String chapterId, final String teacherId, final String examType, String verifyStatus) {
        Criteria criteria = new Criteria();
        if (StrUtil.isNotBlank(courseId)) {
            criteria.and("courseId").is(courseId);
        }
        if (StrUtil.isNotBlank(chapterId)) {
            criteria.and("chapterId").is(chapterId);
        }
        if (StrUtil.isNotBlank(teacherId)) {
            criteria.and("teacherId").is(teacherId);
        }
        if (StrUtil.isNotBlank(examType)) {
            criteria.and("examType").is(examType);
        }
        if (StrUtil.isNotBlank(verifyStatus)) {
            criteria.and("verifyStatus").is(verifyStatus);
        }
        return Query.query(criteria);
    }
}