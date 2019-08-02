package com.project.quiz.problemsetlibrary.web.control;


import com.project.quiz.problemsetlibrary.domain.SurveyQuestionExerciseBook;
import com.project.quiz.problemsetlibrary.domain.SurveyQuestionProblemSet;
import com.project.quiz.problemsetlibrary.service.SurveyQuestionExerciseBookService;
import com.project.quiz.problemsetlibrary.service.base.BaseProblemSetService;
import com.project.quiz.problemsetlibrary.web.control.base.BaseProblemSetController;
import com.project.quiz.questionlibrary.domain.SurveyQuestion;
import com.project.quiz.service.TokenService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @author: liu zhenming
 * @version: V1.0
 * @date: 2019/1/13  23:00
 */
@Slf4j
@RestController
@Api(value = "问卷库 练习册,题集相关", tags = {"问卷库 练习册,题集相关操作"})
@RequestMapping(path = "/surveyExerciseBook", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class SurveyQuestionProblemSetController extends BaseProblemSetController<SurveyQuestionProblemSet, SurveyQuestion, SurveyQuestionExerciseBook> {

    public SurveyQuestionProblemSetController(BaseProblemSetService<SurveyQuestionProblemSet, SurveyQuestion> service,
                                              SurveyQuestionExerciseBookService exerciseBookService, TokenService tokenService) {
        super(service, exerciseBookService, tokenService);
    }
}
