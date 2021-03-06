package com.project.quiz.questionlibrary.web.control.base;


import cn.hutool.core.util.StrUtil;
import com.project.quiz.common.DefineCode;
import com.project.quiz.common.MyAssert;
import com.project.quiz.common.WebResult;
import com.project.quiz.questionlibrary.domain.BigQuestion;
import com.project.quiz.questionlibrary.domain.base.AbstractExam;
import com.project.quiz.questionlibrary.service.QuestionService;
import com.project.quiz.questionlibrary.web.req.FindQuestionsReq;
import com.project.quiz.service.TokenService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.List;

import static com.project.quiz.web.vo.ValideSortVo.valideSort;

/**
 * @Description:
 * @author: liu zhenming
 * @version: V1.0
 * @date: 2019/1/14  16:39
 */
public abstract class BaseAllController<T extends AbstractExam> {

    private final QuestionService<T> service;
    private final TokenService tokenService;

    public BaseAllController(QuestionService<T> service, TokenService tokenService) {
        this.service = service;
        this.tokenService = tokenService;
    }

    @DeleteMapping("/delete/{id}")
    @ApiImplicitParam(name = "id", value = "id", paramType = "form", dataType = "string", example = "新增数据时 不添加id 修改时数据添加id")
    @ApiOperation(value = "删除题目", notes = "删除对应的题目信息")
    public Mono<WebResult> delQuestions(@Valid @PathVariable String id) {
        MyAssert.isNull(id, DefineCode.ERR0010, "id不能为空");
        return service.delQuestion(id).map(WebResult::okResult);
    }

    @ApiOperation(value = "编辑保存题信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "id", name = "id", example = "传入id为修改  不传id为新增", dataType = "string", paramType = "form"),
            @ApiImplicitParam(value = "章节id", name = "chapterId", dataType = "string", paramType = "form", required = true),
            @ApiImplicitParam(value = "章节名称", name = "chapterName", dataType = "string", paramType = "form", required = true),
            @ApiImplicitParam(value = "课程id", name = "courseId", dataType = "string", paramType = "form", required = true),
            @ApiImplicitParam(value = "课程名称", name = "courseName", dataType = "string", paramType = "form", required = true),
            @ApiImplicitParam(value = "题目分数", name = "score", dataType = "number", example = "2.0", paramType = "form"),
            @ApiImplicitParam(value = "老师id", name = "teacherId", paramType = "form", required = true),
            @ApiImplicitParam(value = "老师名称", name = "teacherName", paramType = "form", required = true),
            @ApiImplicitParam(value = "考题类型  single  multiple trueOrFalse ", name = "examType", required = true, example = "single", paramType = "form"),
            @ApiImplicitParam(value = "题目题干", name = "choiceQstTxt", example = "1+1 = ?", paramType = "form"),
            @ApiImplicitParam(value = "题目答案", name = "answer", example = "A", paramType = "form"),
            @ApiImplicitParam(value = "题目解析", name = "analysis", example = "A选项正确", paramType = "form"),
            @ApiImplicitParam(value = "难易度id", name = "levelId", example = "0", paramType = "form"),
            @ApiImplicitParam(name = "centerAreaId", value = "学习中心Id", dataType = "string", paramType = "form", required = true),
            @ApiImplicitParam(name = "centerName", value = "学习中心名称", dataType = "string", paramType = "form", required = true),
//            @ApiImplicitParam(value = "单选与多选区分 single  multiple", name = "choiceType, 只有是选择题才传值", dataType = "string", example = "single", paramType = "form"),
            @ApiImplicitParam(value = "选项集", name = "optChildren", example = "single 只有是选择题才传值", dataTypeClass = List.class, paramType = "form")
    })
    @PostMapping(path = "/editSaveQuestion")
    public Mono<WebResult> editBigQuestion(@RequestBody BigQuestion bigQuestion, ServerHttpRequest request) {
        MyAssert.isNull(bigQuestion.getCourseId(), DefineCode.ERR0010, "课程Id不为空");
        MyAssert.isNull(bigQuestion.getCourseName(), DefineCode.ERR0010, "课程名称不为空");
        MyAssert.isNull(bigQuestion.getTeacherName(), DefineCode.ERR0010, "教师名称不为空");
        MyAssert.isNull(bigQuestion.getCourseId(), DefineCode.ERR0010, "章节Id不为空");
        MyAssert.isNull(bigQuestion.getCourseName(), DefineCode.ERR0010, "章节名称不为空");
        MyAssert.isNull(bigQuestion.getCenterAreaId(), DefineCode.ERR0010, "学习中心不为空");
        MyAssert.isNull(bigQuestion.getExamType(), DefineCode.ERR0010, "考题类型不为空");
        MyAssert.isTrue(StrUtil.isBlank(bigQuestion.getCenterAreaId()), DefineCode.ERR0010, "学习中心Id不能为空");
        MyAssert.isNull(bigQuestion.getCenterName(), DefineCode.ERR0010, "学习中心名称不为空");
        tokenService.getTeacherId(request).ifPresent(bigQuestion::setTeacherId);
        return service.editBigQuestion(bigQuestion).map(WebResult::okResult);
    }

    @ApiOperation(value = "多条件分页查询题库信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "章节id", name = "chapterId", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "courseId", value = "课程id", dataType = "string", paramType = "query"),
            @ApiImplicitParam(value = "创作老师id", name = "teacherId", dataType = "string", paramType = "query"),
            @ApiImplicitParam(value = "考题类型  single  multiple trueOrFalse", name = "examType", example = "single", dataType = "query"),
            @ApiImplicitParam(value = "分页", dataType = "int", name = "page", example = "0", required = true),
            @ApiImplicitParam(value = "每页数量", dataType = "int", name = "size", example = "20", required = true)
    })
    @PostMapping(path = "/findQuestionPageAll")
    public Mono<WebResult> findQuestionPageAll(@RequestBody FindQuestionsReq req, ServerHttpRequest request) {
        valideSort(req.getPage(), req.getSize());
        if (tokenService.isStudent(request)) {
            return service.findPageAll(req).map(WebResult::okResult);
        }else {
            return service.findAllPageQuestion(req).map(WebResult::okResult);
        }
    }

    @ApiOperation(value = "查询习题详细信息")
    @GetMapping(path = "/{questionId}")
    public Mono<WebResult> findQuestionById(@PathVariable String questionId){
        MyAssert.isNull(questionId, DefineCode.ERR0010, "习题Id不能为空");
        return service.findQuestionById(questionId).map(WebResult::okResult);
    }
}
