package com.project.quiz.questionlibrary.web.control;

import com.project.quiz.questionlibrary.domain.base.AbstractExam;
import com.project.quiz.questionlibrary.service.QuestionService;
import com.project.quiz.questionlibrary.web.control.base.BaseAllController;
import com.project.quiz.service.TokenService;
import io.swagger.annotations.Api;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: zhangyy
 * @email: zhang10092009@hotmail.com
 * @date: 19-8-9 15:21
 * @version: 1.0
 * @description:
 */
@RestController
@Api(value = "题目管理", tags = {"教师端题目管理"})
@RequestMapping(path = "/question", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class QuestionController<T extends AbstractExam> extends BaseAllController<T> {

    public QuestionController(QuestionService<T> service, TokenService tokenService) {
        super(service, tokenService);
    }
}
