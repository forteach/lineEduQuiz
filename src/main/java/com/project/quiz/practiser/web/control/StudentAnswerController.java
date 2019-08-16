package com.project.quiz.practiser.web.control;

import com.project.quiz.common.WebResult;
import com.project.quiz.practiser.service.StudentAnswerService;
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

//    @ApiOperation(value = "学生回答作业", notes = "学生端学生提交答案")
//    @PostMapping("/saveAnswer")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "courseId", value = "课程id", dataType = "string", required = true, paramType = "form"),
//            @ApiImplicitParam(name = "chapterId", value = "章节id", dataType = "string", required = true, paramType = "form"),
//            @ApiImplicitParam(name = "chapterName", value = "章节名称", dataType = "string", paramType = "chapterName"),
//            @ApiImplicitParam(name = "exeBookType", value = "练习册类型: 1、提问册 2、练习册3、作业册", example = "3", required = true, paramType = "form"),
//            @ApiImplicitParam(name = "questionId", value = "问题id", dataType = "string", required = true, paramType = "form"),
//            @ApiImplicitParam(name = "preview", value = "习题类型  before/预习 now/课堂 after/课后练习", dataType = "string", required = true, paramType = "form"),
//            @ApiImplicitParam(name = "answer", value = "回答内容", dataType = "string", required = true, paramType = "form"),
//            @ApiImplicitParam(name = "classId", value = "班级id", required = true, paramType = "query"),
//            @ApiImplicitParam(name = "fileList", value = "附件列表", paramType = "form"),
//            @ApiImplicitParam(name = "answerImageList", value = "答案图片列表", paramType = "form"),
//    })
//    public Mono<WebResult> saveAnswer(@RequestBody AnswerReq answerReq, ServerHttpRequest request){
//        answerVerify.verify(answerReq);
//        MyAssert.isNull(answerReq.getQuestionId(), DefineCode.ERR0010, "题目id不为空");
//        MyAssert.isNull(answerReq.getAnswer(), DefineCode.ERR0010, "答案不为空");
//        answerReq.setStudentId(tokenService.getStudentId(request));
//        AnswerVo answerVo = new AnswerVo();
//        BeanUtils.copyProperties(answerReq, answerVo);
//        return exerciseBookSnapshotService.saveSnapshot(answerVo, answerReq)
//                .map(WebResult::okResult);
//    }


//    @ApiOperation(value = "学生端查询自己回答")
//    @PostMapping(path = "/findAnswer")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "courseId", value = "课程id", dataType = "string", required = true, paramType = "form"),
//            @ApiImplicitParam(name = "chapterId", value = "章节id", dataType = "string", required = true, paramType = "form"),
//            @ApiImplicitParam(name = "exeBookType", value = "练习册类型: 1、提问册 2、练习册3、作业册", example = "3", required = true, paramType = "form"),
//            @ApiImplicitParam(name = "preview", value = "习题类型  before/预习 now/课堂 after/课后练习", required = true, paramType = "form"),
//            @ApiImplicitParam(name = "classId", value = "班级id", required = true, paramType = "query"),
//            @ApiImplicitParam(name = "questionId", value = "问题id", dataType = "string", paramType = "form"),
//    })
//    public Mono<WebResult> findAnswer(@RequestBody AnswerReq answerReq, ServerHttpRequest request){
//        answerVerify.verify(answerReq);
//        answerReq.setStudentId(tokenService.getStudentId(request));
//        return exerciseAnswerService
//                .findAnswerStudent(answerReq)
//                .map(WebResult::okResult);
//    }

    /**
     * 学生端查询习题集,如果学生没有答题查询最新的题集,答过返回题库的快照
     * @param req
     * @param request
     * @return
     */
    @ApiOperation(value = "学生端查询习题", notes = "学生查询习题集,学生答题后是快照,没有答题返回最新的答题的题库")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "章节id", name = "chapterId", example = "章节id", dataType = "string", paramType = "query"),
            @ApiImplicitParam(value = "课程id", name = "courseId", example = "章节id", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "number", value = "随机题目数量", dataType = "int", required = true, paramType = "query"),
    })
    @PostMapping("/findQuestions")
    public Mono<WebResult> findExerciseBook(@RequestBody StudentFindQuestionsReq req, ServerHttpRequest request) {
//        req.setStudentId(tokenService.getStudentId(request));
        req.setStudentId("");
        return studentAnswerService.findStudentQuestions(req).map(WebResult::okResult);
    }
}
