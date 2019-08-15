package com.project.quiz.practiser.web.resp;

import com.project.quiz.practiser.domain.AskAnswerExercise;
import com.project.quiz.web.pojo.Students;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * @author: zhangyy
 * @email: zhang10092009@hotmail.com
 * @date: 19-6-4 14:05
 * @version: 1.0
 * @description:
 */
@Data
@ApiModel(value = "学生回答的情况")
@EqualsAndHashCode(callSuper = true)
public class AnswerStudentResp extends Students implements Serializable {

    @ApiModelProperty(name = "questionIds", value = "回答/没有回答 的问题id")
    private List<String> questionIds;

    @ApiModelProperty(name = "isAnswerCompleted", value = "是否答完题 Y/N")
    private String isAnswerCompleted;

    @ApiModelProperty(name = "isCorrectCompleted", value = "是否批改完作业　Y/N", dataType = "string", required = true)
    private String isCorrectCompleted;

    @ApiModelProperty(name = "isReward", value = "是否奖励", dataType = "string")
    private String isReward;

}
