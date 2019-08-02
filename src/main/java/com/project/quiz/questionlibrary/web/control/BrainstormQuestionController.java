package com.project.quiz.questionlibrary.web.control;


import com.project.quiz.questionlibrary.domain.BrainstormQuestion;
import com.project.quiz.questionlibrary.service.KeywordService;
import com.project.quiz.questionlibrary.service.base.BaseQuestionService;
import com.project.quiz.questionlibrary.web.control.base.BaseSubjectiveController;
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
 * @date: 2019/1/11  16:28
 */
@Slf4j
@RestController
@Api(value = "头脑风暴的问题 题目", tags = {"头脑风暴的问题 题库内容操作"})
@RequestMapping(path = "/brainstormQuestion", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class BrainstormQuestionController extends BaseSubjectiveController<BrainstormQuestion> {

    public BrainstormQuestionController(BaseQuestionService<BrainstormQuestion> service, KeywordService<BrainstormQuestion> keywordService, TokenService tokenService) {
        super(service, keywordService, tokenService);
    }
}
