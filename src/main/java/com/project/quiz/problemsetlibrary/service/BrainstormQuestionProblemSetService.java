package com.project.quiz.problemsetlibrary.service;


import com.project.quiz.problemsetlibrary.domain.BrainstormQuestionProblemSet;
import com.project.quiz.problemsetlibrary.repository.base.ProblemSetMongoRepository;
import com.project.quiz.problemsetlibrary.service.base.BaseProblemSetServiceImpl;
import com.project.quiz.questionlibrary.domain.BrainstormQuestion;
import com.project.quiz.questionlibrary.repository.base.QuestionMongoRepository;
import com.project.quiz.questionlibrary.service.base.BaseQuestionService;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @author: liu zhenming
 * @version: V1.0
 * @date: 2019/1/13  18:57
 */
@Service
public class BrainstormQuestionProblemSetService extends BaseProblemSetServiceImpl<BrainstormQuestionProblemSet, BrainstormQuestion> {


    public BrainstormQuestionProblemSetService(ReactiveMongoTemplate reactiveMongoTemplate,
                                               ProblemSetMongoRepository<BrainstormQuestionProblemSet> repository,
                                               QuestionMongoRepository<BrainstormQuestion> questionRepository,
                                               BaseQuestionService<BrainstormQuestion> questionService) {

        super(reactiveMongoTemplate, repository, questionRepository, questionService);
    }
}
