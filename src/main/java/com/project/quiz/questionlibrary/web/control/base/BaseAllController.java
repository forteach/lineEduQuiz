package com.project.quiz.questionlibrary.web.control.base;


import com.project.quiz.common.WebResult;
import com.project.quiz.questionlibrary.domain.base.AbstractExam;
import com.project.quiz.questionlibrary.domain.question.ChoiceQst;
import com.project.quiz.questionlibrary.domain.question.ChoiceQstOption;
import com.project.quiz.questionlibrary.domain.question.TrueOrFalse;
import com.project.quiz.questionlibrary.service.base.BaseQuestionService;
import com.project.quiz.service.TokenService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

/**
 * @Description:
 * @author: liu zhenming
 * @version: V1.0
 * @date: 2019/1/14  16:39
 */
public abstract class BaseAllController<T extends AbstractExam> {

    private final BaseQuestionService<T> service;
    private final TokenService tokenService;

    public BaseAllController(BaseQuestionService<T> service, TokenService tokenService) {
        this.service = service;
        this.tokenService = tokenService;
    }

    /**
     * 编辑判断题
     *
     * @param trueOrFalse
     * @return BigQuestion
     */
//    @PostMapping("/trueOrFalse/edit")
//    @ApiImplicitParams({
//            @ApiImplicitParam(value = "题目题干", name = "trueOrFalseInfo", required = true, example = "亚特兰蒂斯是否存在", paramType = "form"),
//            @ApiImplicitParam(value = "题目答案", name = "trueOrFalseAnsw", required = true, example = "true", paramType = "form")
//    })
//    @ApiOperation(value = "编辑判断题", notes = "新增数据时 不添加id 修改时数据添加id")
//    public Mono<WebResult> editTrueOrFalse(@Valid @RequestBody T trueOrFalse) {
//        return service.editQuestion(trueOrFalse, TrueOrFalse.class).map(WebResult::okResult);
//    }

    /**
     * 编辑选择题
     *
//     * @param bigQuestion
     * @return BigQuestion
     */
//    @PostMapping("/choiceQst/edit")
//    @ApiOperation(value = "编辑选择题", notes = "新增数据时 不添加id 修改时数据添加id")
//    @ApiImplicitParams({
//            @ApiImplicitParam(value = "题目题干", name = "choiceQstTxt", required = true, example = "1+1 = ?", paramType = "form"),
//            @ApiImplicitParam(value = "题目答案", name = "choiceQstAnsw", required = true, example = "A", dataType = "string", paramType = "form"),
//            @ApiImplicitParam(value = "单选与多选区分 single  multiple", name = "choiceType", required = true, example = "single", paramType = "form"),
//            @ApiImplicitParam(value = "选项集", name = "optChildren", required = true, dataType = "list", dataTypeClass = ChoiceQstOption.class, paramType = "form")
//    })
//    public Mono<WebResult> editChoiceQst(@Valid @RequestBody T bigQuestion) {
//        return service.editQuestion(bigQuestion, ChoiceQst.class).map(WebResult::okResult);
//    }

    @GetMapping("/delete/{id}")
    @ApiImplicitParam(name = "id", value = "id", paramType = "form", dataType = "string", example = "新增数据时 不添加id 修改时数据添加id")
    @ApiOperation(value = "删除题目", notes = "新增数据时 不添加id 修改时数据添加id")
    public Mono<WebResult> delQuestions(@Valid @PathVariable String id) {
        return service.delQuestion(id).map(WebResult::okResult);
    }
}
