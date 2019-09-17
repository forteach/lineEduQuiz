package com.project.quiz.practiser.web.control;

import com.project.quiz.common.DefineCode;
import com.project.quiz.common.MyAssert;
import com.project.quiz.common.WebResult;
import com.project.quiz.practiser.service.StudentAnswerService;
import com.project.quiz.practiser.web.req.AnswerReq;
import com.project.quiz.practiser.web.req.FindAnswerReq;
import com.project.quiz.practiser.web.req.StudentFindQuestionsReq;
import com.project.quiz.practiser.web.req.verify.AnswerVerify;
import com.project.quiz.service.TokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import static com.project.quiz.web.vo.ValideSortVo.valideSort;

/**
 * @author: zhangyy
 * @email: zhang10092009@hotmail.com
 * @date: 19-6-3 11:11
 * @version: 1.0
 * @description:
 */
@Slf4j
@RestController
@Api(value = "学生提交答案", description = "学生回答提交作业练习答案接口", tags = {"学生提交答案"})
@RequestMapping(path = "/studentAnswer", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class StudentAnswerController {

    private final TokenService tokenService;
    private final AnswerVerify answerVerify;
    private final StudentAnswerService studentAnswerService;

    @Autowired
    public StudentAnswerController(StudentAnswerService studentAnswerService,
                                   TokenService tokenService, AnswerVerify answerVerify) {
        this.tokenService = tokenService;
        this.answerVerify = answerVerify;
        this.studentAnswerService = studentAnswerService;
    }

    @ApiOperation(value = "学生回答作业", notes = "学生端学生提交答案")
    @PostMapping("/saveAnswer")
    @ApiImplicitParams({
//            @ApiImplicitParam(name = "courseId", value = "课程id", dataType = "string", required = true, paramType = "form"),
            @ApiImplicitParam(name = "chapterId", value = "章节id", dataType = "string", required = true, paramType = "form"),
            @ApiImplicitParam(name = "questionId", value = "问题id", dataType = "string", required = true, paramType = "form"),
//            @ApiImplicitParam(name = "chapterName", value = "章节名称", dataType = "string", required = true, paramType = "form"),
            @ApiImplicitParam(name = "stuAnswer", value = "回答内容", required = true, paramType = "form"),
//            @ApiImplicitParam(name = "classId", value = "班级id", required = true, paramType = "form"),
            @ApiImplicitParam(name = "studentId", value = "学生id", required = true, paramType = "form")
    })
    public Mono<WebResult> saveAnswer(@RequestBody AnswerReq req, ServerHttpRequest request){
        answerVerify.verifyChapterId(req.getChapterId());
        MyAssert.isNull(req.getQuestionId(), DefineCode.ERR0010, "题目id不为空");
        MyAssert.isNull(req.getStuAnswer(), DefineCode.ERR0010, "答案不为空");
//        MyAssert.isNull(req.getChapterName(), DefineCode.ERR0010, "章节名称不为空");
        req.setStudentId(tokenService.getStudentId(request));
        return studentAnswerService.saveStudentAnswer(req).map(WebResult::okResult);
    }


    @ApiOperation(value = "学生端分页查询自己回答结果与解析")
    @PostMapping(path = "/findAnswerPageAll")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "courseId", value = "课程id", dataType = "string", required = true, paramType = "form"),
            @ApiImplicitParam(name = "chapterId", value = "章节id", dataType = "string", required = true, paramType = "form"),
            @ApiImplicitParam(name = "classId", value = "班级id", required = true, paramType = "query"),
            @ApiImplicitParam(name = "questionId", value = "问题id", dataType = "string", paramType = "form"),
            @ApiImplicitParam(value = "分页", dataType = "int", name = "page", example = "0", required = true),
            @ApiImplicitParam(value = "每页数量", dataType = "int", name = "size", example = "20", required = true)
    })
    public Mono<WebResult> findAnswerPageAll(@RequestBody FindAnswerReq req, ServerHttpRequest request){
        valideSort(req.getPage(), req.getSize());
        answerVerify.verifyChapterId(req.getCourseId(), req.getChapterId());
        req.setStudentId(tokenService.getStudentId(request));
        return studentAnswerService.findAnswerStudent(req).map(WebResult::okResult);
    }

    @ApiOperation(value = "学生端分页查询回答情况")
    @PostMapping(path = "/findStudentAnswerPageAll")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "courseId", value = "课程id", dataType = "string", required = true, paramType = "query"),
            @ApiImplicitParam(name = "chapterId", value = "章节id", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "studentId", value = "学生id", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "classId", value = "班级id", required = true, paramType = "query"),
            @ApiImplicitParam(value = "分页", dataType = "int", name = "page", example = "0", required = true, paramType = "query"),
            @ApiImplicitParam(value = "每页数量", dataType = "int", name = "size", example = "20", required = true, paramType = "query")
    })
    public Mono<WebResult> findStudentAnswerPageAll(@RequestBody FindAnswerReq req){
        valideSort(req.getPage(), req.getSize());
        MyAssert.isNull(req.getCourseId(), DefineCode.ERR0010, "课程不能为空");
        return studentAnswerService.findStudentAnswerStudent(req).map(WebResult::okResult);
    }

    /**
     * 学生端查询习题集,如果学生没有答题查询最新的题集,答过返回题库的快照
     * @param req
     * @param request
     * @return
     */
    @ApiOperation(value = "学生端查询习题", notes = "学生查询习题集,学生答题后是快照,没有答题返回最新的答题的题库")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "章节id", name = "chapterId", example = "章节id", dataType = "string", required = true, paramType = "query"),
            //@ApiImplicitParam(value = "章节名称", name = "chapterName", example = "章节名称", dataType = "string", required = true, paramType = "query"),
//            @ApiImplicitParam(value = "课程id", name = "courseId", example = "课程id", dataType = "string", required = true, paramType = "query"),
            @ApiImplicitParam(value = "班级id", name = "classId", example = "班级id", dataType = "string", required = true, paramType = "query"),
            @ApiImplicitParam(name = "number", value = "随机题目数量", dataType = "int", required = true, paramType = "query")
    })
    @PostMapping("/findQuestions")
    public Mono<WebResult> findExerciseBook(@RequestBody StudentFindQuestionsReq req, ServerHttpRequest request) {
        answerVerify.verifyChapterId(req.getChapterId());
//        MyAssert.isNull(req.getNumber(), DefineCode.ERR0010, "随机获取题目数量不能为空");
        if (req.getNumber() == null){
            req.setNumber(5);
        }
        req.setStudentId(tokenService.getStudentId(request));
        return studentAnswerService.findStudentQuestions(req).map(WebResult::okResult);
    }
}