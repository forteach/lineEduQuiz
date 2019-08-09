package com.project.quiz.questionlibrary.service;

import com.mongodb.client.result.UpdateResult;
import com.project.quiz.questionlibrary.domain.QuestionList;
import com.project.quiz.questionlibrary.service.base.BaseQuestionService;
import com.project.quiz.questionlibrary.web.req.QuestionBankReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author: zhangyy
 * @email: zhang10092009@hotmail.com
 * @date: 19-8-9 16:05
 * @version: 1.0
 * @description:
 */
@Service
@Slf4j
public class QuestionService implements BaseQuestionService<QuestionList> {

    @Override
    public Mono<QuestionList> editQuestion(QuestionList question, Class obj) {
        return null;
    }

    @Override
    public Mono<Boolean> questionBankAssociationAdd(String questionBankId, String teacherId) {
        return null;
    }

    @Override
    public Mono<Void> delQuestion(String id) {
        return null;
    }

    @Override
    public Flux<QuestionList> findAllDetailed(QuestionBankReq sortVo) {
        return null;
    }

    @Override
    public Mono<QuestionList> findOneDetailed(String id) {
        return null;
    }

    @Override
    public Mono<QuestionList> editQuestion(QuestionList question) {
        return null;
    }

    @Override
    public Mono<UpdateResult> questionBankAssociation(String questionBankId, String teacherId) {
        return null;
    }

    @Override
    public Flux<QuestionList> findBigQuestionInId(List<String> ids) {
        return null;
    }
}
