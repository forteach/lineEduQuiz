package com.project.quiz.practiser.service;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.project.quiz.practiser.web.req.StudentFindQuestionsReq;
import com.project.quiz.practiser.web.vo.AnswerVo;
import com.project.quiz.questionlibrary.domain.BigQuestion;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.SampleOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Date;
import java.util.List;

import static com.project.quiz.common.Dic.MONGDB_ID;
import static com.project.quiz.practiser.constant.Dic.STUDENT_QUESTIONS;
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

//    private

    public Mono<List<BigQuestion>> findStudentQuestions(final StudentFindQuestionsReq req) {
        final String key = STUDENT_QUESTIONS.concat(req.getStudentId());
        Mono<Boolean> booleanMono = reactiveRedisTemplate.hasKey(key);
        return booleanMono.flatMap(b -> {
            if (!b) {
                Criteria criteria = new Criteria();
                if (StrUtil.isNotBlank(req.getChapterId())) {
                    criteria.and("chapterId").is(req.getChapterId());
                }
                if (StrUtil.isNotBlank(req.getCourseId())) {
                    criteria.and("courseId").is(req.getCourseId());
                }
                SampleOperation matchStage = Aggregation.sample(req.getNumber());
                Aggregation aggregation = Aggregation.newAggregation(matchStage, match(criteria));

//        Mono<Long> count = reactiveMongoTemplate.count(Query.query(criteria), BigQuestion.class);
                return reactiveMongoTemplate.aggregate(aggregation, "bigQuestion", BigQuestion.class)
                        .collectList()
                        .filterWhen(list -> setRedisStudentQuestions(list, key))
                        .flatMapMany(Flux::fromIterable)
                        .map(bigQuestion -> {
                            //答案置空
                            bigQuestion.setAnswer("");
                            bigQuestion.setAnalysis("");
                            return bigQuestion;
                        }).collectList();
            } else {
                return reactiveRedisTemplate.opsForValue().get(key)
                        .flatMap(s -> Mono.just(JSONUtil.toList(JSONUtil.parseArray(s), BigQuestion.class)));
            }
        });
//        return count.zipWith(output)
//                .map(t -> new PageImpl<>(t.getT2(), PageRequest.of(req.getPage(), req.getSize()), t.getT1()));
    }

    private Mono<Boolean> setRedisStudentQuestions(final List<BigQuestion> bigQuestions, final String key) {
        return reactiveRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(bigQuestions), Duration.ofHours(24));
    }

    /**
     * 设置查询条件
     *
     * @return
     */
    Criteria buildExerciseBook(final AnswerVo answerVo) {

        Criteria criteria = new Criteria();
        if (StrUtil.isNotBlank(answerVo.getChapterId())) {
            criteria.and("chapterId").is(answerVo.getChapterId());
        }
        if (StrUtil.isNotBlank(answerVo.getCourseId())) {
            criteria.and("courseId").is(answerVo.getCourseId());
        }
        if (StrUtil.isNotBlank(answerVo.getClassId())) {
            criteria.and("classId").is(answerVo.getClassId());
        }
        if (StrUtil.isNotBlank(answerVo.getStudentId())) {
            criteria.and("studentId").is(answerVo.getStudentId());
        }
        return criteria;
    }

    Criteria queryCriteria(final AnswerVo answerVo, final String questionId) {
        //设置查询条件
        Criteria criteria = buildExerciseBook(answerVo);

        if (StrUtil.isNotBlank(questionId)) {
            criteria.and("bigQuestionExerciseBook.questionChildren.".concat(MONGDB_ID)).is(new ObjectId(questionId));
        }
        return criteria;
    }

    Update updateQuery(final AnswerVo answerVo) {
        // 修改答题记录
        Update update = Update.update("uDate", DateUtil.formatDateTime(new Date()));

        if (StrUtil.isNotBlank(answerVo.getChapterId())) {
            update.set("chapterId", answerVo.getChapterId());
        }
        if (StrUtil.isNotBlank(answerVo.getCourseId())) {
            update.set("courseId", answerVo.getCourseId());
        }

        if (StrUtil.isNotBlank(answerVo.getClassId())) {
            update.set("classId", answerVo.getClassId());
        }
        if (StrUtil.isNotBlank(answerVo.getStudentId())) {
            update.set("studentId", answerVo.getStudentId());
        }
        return update;
    }
}