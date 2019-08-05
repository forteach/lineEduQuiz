package com.project.quiz.questionlibrary.web.control.base;


import com.project.quiz.common.WebResult;
import com.project.quiz.questionlibrary.domain.base.QuestionExamEntity;
import com.project.quiz.questionlibrary.domain.question.Design;
import com.project.quiz.questionlibrary.service.KeywordService;
import com.project.quiz.questionlibrary.service.base.BaseQuestionService;
import com.project.quiz.service.TokenService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

/**
 * @Description:
 * @author: liu zhenming
 * @version: V1.0
 * @date: 2019/1/14  16:24
 */
public abstract class BaseSubjectiveController<T extends QuestionExamEntity> extends BaseQuestionController<T> {

    public BaseSubjectiveController(BaseQuestionService<T> service, KeywordService<T> keywordService, TokenService tokenService) {
        super(service, keywordService, tokenService);
    }

    /**
     * 编辑简答思考题
     *
     * @param design
     * @return BigQuestion
     */
    @PostMapping("/design/edit")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "relate", value = "此操作是否修改到快照", dataType = "string", required = true, defaultValue = "0", paramType = "form"),
            @ApiImplicitParam(name = "examChildren", value = "保存,修改的简答题对象", dataType = "json", required = true, paramType = "form"),
    })
    @ApiOperation(value = "编辑简答思考题", notes = "新增数据时 不添加id 修改时数据添加id")
    public Mono<WebResult> editDesign(@Valid @RequestBody @ApiParam(value = "简答思考题", required = true) T design) {
        return service.editQuestion(design, Design.class).map(WebResult::okResult);
    }










}