package com.project.quiz.practiser.web.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: zhangyy
 * @email: zhang10092009@hotmail.com
 * @date: 19-8-15 17:05
 * @version: 1.0
 * @description:
 */
@Data
public class StudentFindQuestionsReq implements Serializable {

    @ApiModelProperty(name = "courseId", value = "课程id", dataType = "string", required = true)
    private String courseId;

    @ApiModelProperty(name = "chapterId", value = "章节id", dataType = "string", required = true)
    private String chapterId;

    @ApiModelProperty(name = "number", value = "随机题目数量", dataType = "int", required = true)
    private Integer number;

    @ApiModelProperty(hidden = true)
    private String studentId;
}
