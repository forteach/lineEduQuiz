package com.project.quiz.questionlibrary.service;


import com.mongodb.client.result.UpdateResult;
import com.project.quiz.questionlibrary.domain.BigQuestion;
import com.project.quiz.questionlibrary.domain.base.AbstractExam;
import com.project.quiz.questionlibrary.web.req.FindQuestionsReq;
import com.project.quiz.questionlibrary.web.req.QuestionBankReq;
import org.springframework.data.domain.Page;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @Description: 问题结构的基础服务
 * @author: liu zhenming
 * @version: V1.0
 * @date: 2019/1/10  11:52
 */
public interface QuestionService<T extends AbstractExam> {

    /**
     * 修改新增判断题
     *
     * @param question
     * @param obj
     * @return
     */
    Mono<T> editQuestion(final T question, final Class obj);

    /**
     * 题目分享
     *
     * @param questionBankId
     * @param teacherId
     * @return
     */
    Mono<Boolean> questionBankAssociationAdd(final String questionBankId, final String teacherId);

    /**
     * 删除单道题
     *
     * @param id
     * @return
     */
    Mono<Boolean> delQuestion(final String id);

    /**
     * 查询详细或全部字段的问题
     *
     * @param sortVo
     * @return
     */
    Flux<T> findAllDetailed(final QuestionBankReq sortVo);

    /**
     * 根据id查询详细
     *
     * @param id
     * @return
     */
    Mono<T> findOneDetailed(final String id);

    /**
     * 修改是是否更新到课后练习册
     *
     * @param question
     * @return
     */
    Mono<T> editQuestion(final T question);


    /**
     * 更新题目与教师关系信息
     *
     * @param questionBankId
     * @param teacherId
     * @return
     */
    Mono<UpdateResult> questionBankAssociation(final String questionBankId, final String teacherId);

    /**
     * 查找题用于
     *
     * @param ids
     * @return
     */
    Flux<T> findBigQuestionInId(final List<String> ids);

    Mono<Boolean> editBigQuestion(final BigQuestion bigQuestion);

    Mono<Page<BigQuestion>> findPageAll(final FindQuestionsReq req);

    Mono<Page<BigQuestion>> findAllPageQuestion(final FindQuestionsReq req);

    Mono<BigQuestion> findQuestionById(String questionId);
}