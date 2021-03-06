package com.project.quiz.practiser.web.req;

import com.project.quiz.web.vo.SortVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author: zhangyy
 * @email: zhang10092009@hotmail.com
 * @date: 19-8-19 11:30
 * @version: 1.0
 * @description:
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FindAnswerReq extends SortVo implements Serializable {

    @ApiModelProperty(name = "studentId", value = "学生id", dataType = "string")
    private String studentId;

    @ApiModelProperty(name = "courseId", value = "课程id", dataType = "string", required = true)
    private String courseId;

    @ApiModelProperty(name = "chapterId", value = "章节id", dataType = "string", required = true)
    private String chapterId;

    @ApiModelProperty(name = "questionId", value = "问题id", dataType = "string")
    private String questionId;

    @ApiModelProperty(name = "classId", value = "班级id", dataType = "string", required = true)
    private String classId;
}