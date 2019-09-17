package com.project.quiz.practiser.web.req;

import com.project.quiz.practiser.web.req.base.AbstractReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import static com.project.quiz.practiser.constant.Dic.STUDENT_QUESTIONS;

/**
 * @author: zhangyy
 * @email: zhang10092009@hotmail.com
 * @date: 19-6-3 09:43
 * @version: 1.0
 * @description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "回答记录")
@EqualsAndHashCode(callSuper = true)
public class AnswerReq extends AbstractReq implements Serializable {

    @ApiModelProperty(name = "stuAnswer", value = "回答内容", dataType = "string", required = true)
    private String stuAnswer;

    /**
     * 回答的学生
     */
    @ApiModelProperty(hidden = true)
    private String studentId;

    public String getKey(){
//        return STUDENT_QUESTIONS.concat(studentId).concat("#").concat(super.getCourseId()).concat("#").concat(super.getChapterId());
        return STUDENT_QUESTIONS.concat(studentId).concat("#").concat(super.getChapterId());
    }
}