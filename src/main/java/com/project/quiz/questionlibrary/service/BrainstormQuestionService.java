package com.project.quiz.questionlibrary.service;


import com.project.quiz.questionlibrary.domain.BrainstormQuestion;
import com.project.quiz.questionlibrary.reflect.QuestionReflect;
import com.project.quiz.questionlibrary.repository.base.QuestionMongoRepository;
import com.project.quiz.questionlibrary.service.base.BaseQuestionServiceImpl;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @author: liu zhenming
 * @version: V1.0
 * @date: 2019/1/11  16:09
 */
@Service
public class BrainstormQuestionService extends BaseQuestionServiceImpl<BrainstormQuestion> {

    public BrainstormQuestionService(QuestionMongoRepository<BrainstormQuestion> repository,
                                     ReactiveMongoTemplate reactiveMongoTemplate,
                                     QuestionReflect questionReflect) {

        super(repository, reactiveMongoTemplate, questionReflect);
    }
}
