package com.project.quiz.practiser.web.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;

import java.io.Serializable;

/**
 * @author: zhangyy
 * @email: zhang10092009@hotmail.com
 * @date: 19-6-10 13:45
 * @version: 1.0
 * @description:
 */
@Data
public class AskAnswerExerciseResp implements Serializable {
    /**
     * 习题id
     */
    @ApiModelProperty(value = "习题id", name = "questionId")
    @Indexed
    private String questionId;



    /**
     * 学生答案
     */
    @ApiModelProperty(value = "回答内容", name = "answer")
    private String stuAnswer;

}
