package com.project.quiz.practiser.service;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.project.quiz.practiser.web.req.StudentFindQuestionsReq;
import com.project.quiz.practiser.web.vo.AnswerVo;
import com.project.quiz.questionlibrary.domain.BigQuestion;
import com.project.quiz.questionlibrary.web.req.FindQuestionsReq;
import com.project.quiz.web.vo.BigQuestionView;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.SampleOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.List;

import static com.project.quiz.common.Dic.MONGDB_ID;
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

    public StudentAnswerService(ReactiveMongoTemplate reactiveMongoTemplate) {
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }

//    private

    public Mono<Page<BigQuestion>> findStudentQuestions(final StudentFindQuestionsReq req){
        Criteria criteria = new Criteria();
        if (StrUtil.isNotBlank(req.getChapterId())){
            criteria.and("chapterId").is(req.getChapterId());
        }
        if (StrUtil.isNotBlank(req.getCourseId())){
            criteria.and("courseId").is(req.getCourseId());
        }
        SampleOperation matchStage = Aggregation.sample(req.getNumber());
        Aggregation aggregation = Aggregation.newAggregation(matchStage, match(criteria));

//        AggregationResults<BigQuestion> output = reactiveMongoTemplate.aggregate(aggregation, "bigQuestion", BigQuestion.class);

        Mono<Long> count = reactiveMongoTemplate.count(Query.query(criteria), BigQuestion.class);
        Mono<List<BigQuestion>> output = reactiveMongoTemplate.aggregate(aggregation, "bigQuestion", BigQuestion.class)
                .collectList();
        return count.zipWith(output)
                .map(t -> new PageImpl<>(t.getT2(), PageRequest.of(req.getPage(), req.getSize()), t.getT1()));
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