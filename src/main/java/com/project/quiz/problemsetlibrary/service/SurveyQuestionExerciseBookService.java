package com.project.quiz.problemsetlibrary.service;


import com.project.quiz.problemsetlibrary.domain.SurveyQuestionExerciseBook;
import com.project.quiz.problemsetlibrary.repository.base.ExerciseBookMongoRepository;
import com.project.quiz.problemsetlibrary.service.base.BaseExerciseBookServiceImpl;
import com.project.quiz.questionlibrary.domain.SurveyQuestion;
import com.project.quiz.questionlibrary.service.base.BaseQuestionServiceImpl;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @author: liu zhenming
 * @version: V1.0
 * @date: 2019/1/13  22:43
 */
@Service
public class SurveyQuestionExerciseBookService extends BaseExerciseBookServiceImpl<SurveyQuestionExerciseBook, SurveyQuestion> {

    public SurveyQuestionExerciseBookService(ExerciseBookMongoRepository<SurveyQuestionExerciseBook> repository,
                                             ReactiveMongoTemplate template, BaseQuestionServiceImpl<SurveyQuestion> questionRepository) {
        super(repository, template, questionRepository);
    }
}
