package com.project.quiz.problemsetlibrary.web.control;


import com.project.quiz.problemsetlibrary.domain.TaskQuestionExerciseBook;
import com.project.quiz.problemsetlibrary.domain.TaskQuestionProblemSet;
import com.project.quiz.problemsetlibrary.service.TaskQuestionExerciseBookService;
import com.project.quiz.problemsetlibrary.service.TaskQuestionProblemSetService;
import com.project.quiz.problemsetlibrary.web.control.base.BaseProblemSetController;
import com.project.quiz.questionlibrary.domain.TaskQuestion;
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
@Api(value = "任务库 练习册,题集相关", tags = {"任务库 练习册,题集相关操作"})
@RequestMapping(path = "/taskExerciseBook", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class TaskQuestionProblemSetController extends BaseProblemSetController<TaskQuestionProblemSet, TaskQuestion, TaskQuestionExerciseBook> {

    public TaskQuestionProblemSetController(TaskQuestionProblemSetService service,
                                            TaskQuestionExerciseBookService exerciseBookService, TokenService tokenService) {
        super(service, exerciseBookService, tokenService);
    }
}
