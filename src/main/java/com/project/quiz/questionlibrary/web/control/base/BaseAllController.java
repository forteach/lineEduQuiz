package com.project.quiz.questionlibrary.web.control.base;


import com.project.quiz.common.WebResult;
import com.project.quiz.questionlibrary.domain.BigQuestion;
import com.project.quiz.questionlibrary.domain.base.AbstractExam;
import com.project.quiz.questionlibrary.domain.question.ChoiceQstOption;
import com.project.quiz.questionlibrary.service.QuestionService;
import com.project.quiz.service.TokenService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.List;

/**
 * @Description:
 * @author: liu zhenming
 * @version: V1.0
 * @date: 2019/1/14  16:39
 */
public abstract class BaseAllController<T> {

    private final QuestionService<T> service;
    private final TokenService tokenService;

    public BaseAllController(QuestionService<T> service, TokenService tokenService) {
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

    @ApiOperation(value = "编辑保存题信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "id", name = "id", example = "传入id为修改  不传id为新增", dataType = "string", paramType = "form"),
            @ApiImplicitParam(value = "章节id", name = "id", dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "courseId", value = "课程id", dataType = "string", paramType = "form"),
            @ApiImplicitParam(value = "题目分数", name = "score", dataType = "number", example = "2.0", paramType = "form"),
            @ApiImplicitParam(value = "创作老师id", name = "teacherId", example = "463bcd8e5fed4a33883850c14f877271", paramType = "form"),
            @ApiImplicitParam(value = "考题类型  single  multiple trueOrFalse  design  bigQuestion", name = "examType", required = true, example = "single", paramType = "form"),
            @ApiImplicitParam(value = "题目题干", name = "choiceQstTxt", example = "1+1 = ?", paramType = "form"),
            @ApiImplicitParam(value = "题目答案", name = "answer", example = "A", paramType = "form"),
            @ApiImplicitParam(value = "题目解析", name = "analysis", example = "A选项正确", paramType = "form"),
            @ApiImplicitParam(value = "难易度id", name = "levelId", example = "0", paramType = "form"),
            @ApiImplicitParam(value = "单选与多选区分 single  multiple", name = "choiceType, 只有是选择题才传值", dataType = "string", example = "single", paramType = "form"),
            @ApiImplicitParam(value = "选项集", name = "optChildren", example = "single 只有是选择题才传值", dataTypeClass = List.class, paramType = "form")
    })
    @PostMapping(path = "/editSave")
    public Mono<WebResult> editBigQuestion(@RequestBody T bigQuestion, ServerHttpRequest request){
        tokenService.getTeacherId(request).ifPresent(((BigQuestion) bigQuestion)::setTeacherId);
        return service.editBigQuestion(bigQuestion).map(WebResult::okResult);
    }
}
