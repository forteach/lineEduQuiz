package com.project.quiz.interaction.web.control;


import com.project.quiz.interaction.service.MoreQue.SendAnswerQuestBookService;
import com.project.quiz.service.TokenService;
import io.swagger.annotations.Api;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 课堂问题发布
 * @author: zjw
 * @version: V1.0
 * @date: 2018/11/19  14:46
 */
@RestController
@Api(value = "题库 考题 互动交互", tags = {"题库 考题等交互"})
@RequestMapping(value = "/stuInteract", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class StudentInteractController {
    /**
     * 课堂问题回答
     */
//    private final SendAnswerService sendAnswerService;

    /**
     * 练习册回答
     */
    private final SendAnswerQuestBookService sendAnswerQuestBookService;

    private final TokenService tokenService;

    public StudentInteractController(
//            SendAnswerService sendAnswerService,
            SendAnswerQuestBookService sendAnswerQuestBookService,
            TokenService tokenService) {
        this.tokenService = tokenService;
//        this.sendAnswerService = sendAnswerService;
        this.sendAnswerQuestBookService = sendAnswerQuestBookService;
    }

    /**
     * 提交答案
     *
     * @param interactAnswerVo
     * @return
     */
//    @PostMapping("/send/answer")
//    @ApiOperation(value = "提交答案", notes = "学生提交答案 只有符合规则的学生能够正确提交")
//    @ApiImplicitParams({
//            @ApiImplicitParam(value = "课堂圈子id", name = "circleId", dataType = "string", required = true, paramType = "from"),
//            @ApiImplicitParam(value = "问题id", name = "questionId", dataType = "string", required = true, paramType = "from"),
//            @ApiImplicitParam(value = "答案", name = "answer", dataType = "string", required = true, paramType = "from"),
//            @ApiImplicitParam(value = "切换提问类型过期标识  接收的该题cut", name = "cut", dataType = "string", required = true, paramType = "from")
//    })
//    public Mono<WebResult> sendAnswer(@ApiParam(value = "提交答案", required = true) @RequestBody InteractAnswerVo interactAnswerVo, ServerHttpRequest serverHttpRequest) {
//        interactAnswerVo.setExamineeId(tokenService.getStudentId(serverHttpRequest));
//        MyAssert.blank(interactAnswerVo.getCircleId(), DefineCode.ERR0010, "课堂编号不能为空");
//        MyAssert.blank(interactAnswerVo.getQuestionId(), DefineCode.ERR0010, "课堂问题ID不能为空");
//        MyAssert.blank(interactAnswerVo.getAnswer(), DefineCode.ERR0010, "题目回答内容不能为空");
//        return sendAnswerService.sendAnswer(interactAnswerVo.getCircleId(),
//                interactAnswerVo.getExamineeId(),
//                interactAnswerVo.getQuestionId(),
//                interactAnswerVo.getAnswer(),
//                interactAnswerVo.getQuestionType(),
//                interactAnswerVo.getFileList()).map(r -> String.valueOf(r)).map(WebResult::okResult);
//    }

    /**
     * 提交课堂练习答案
     *
     * @param interactAnswerVo
     * @return
     */
//    @PostMapping("/sendBook/answer")
//    @ApiImplicitParams({
////            @ApiImplicitParam(value = "学生id", name = "examineeId", dataType = "string", required = true, paramType = "from"),
//            @ApiImplicitParam(value = "课堂圈子id", name = "circleId", dataType = "string", required = true, paramType = "from"),
//            @ApiImplicitParam(value = "问题id", name = "questionId", dataType = "string", required = true, paramType = "from"),
//            @ApiImplicitParam(value = "答案", name = "answer", dataType = "string", required = true, paramType = "from"),
//            @ApiImplicitParam(value = "切换提问类型过期标识  接收的该题cut", name = "cut", dataType = "string", required = true, paramType = "from")
//    })
//    @ApiOperation(value = "提交课堂练习答案", notes = "提交课堂练习答案 只有符合规则的学生能够正确提交")
//    public Mono<WebResult> sendBookAnswer(@ApiParam(value = "提交答案", required = true) @RequestBody InteractAnswerVo interactAnswerVo, ServerHttpRequest serverHttpRequest) {
//
//        interactAnswerVo.setExamineeId(tokenService.getStudentId(serverHttpRequest));
//        return sendAnswerQuestBookService.sendAnswer(
//                interactAnswerVo.getInteractive(),
//                QuestionType.LianXi.name(),
//                interactAnswerVo.getCircleId(),
//                interactAnswerVo.getExamineeId(),
//                interactAnswerVo.getQuestionId(),
//                interactAnswerVo.getAnswer(),
//                interactAnswerVo.getFileList()
//        ).map(WebResult::okResult);
//    }
}