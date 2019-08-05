package com.project.quiz.problemsetlibrary.service;


import com.project.quiz.problemsetlibrary.domain.SurveyQuestionProblemSet;
import com.project.quiz.problemsetlibrary.repository.base.ProblemSetMongoRepository;
import com.project.quiz.problemsetlibrary.service.base.BaseProblemSetServiceImpl;
import com.project.quiz.questionlibrary.domain.SurveyQuestion;
import com.project.quiz.questionlibrary.repository.base.QuestionMongoRepository;
import com.project.quiz.questionlibrary.service.base.BaseQuestionService;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @author: liu zhenming
 * @version: V1.0
 * @date: 2019/1/13  18:56
 */
@Service
public class SurveyQuestionProblemSetService extends BaseProblemSetServiceImpl<SurveyQuestionProblemSet, SurveyQuestion> {

    public SurveyQuestionProblemSetService(ReactiveMongoTemplate reactiveMongoTemplate,
                                           ProblemSetMongoRepository<SurveyQuestionProblemSet> repository,
                                           QuestionMongoRepository<SurveyQuestion> questionRepository,
                                           BaseQuestionService<SurveyQuestion> questionService) {

        super(reactiveMongoTemplate, repository, questionRepository, questionService);
    }
}