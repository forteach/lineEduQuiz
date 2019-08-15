package com.project.quiz.practiser.web.req.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: zhangyy
 * @email: zhang10092009@hotmail.com
 * @date: 19-6-4 10:50
 * @version: 1.0
 * @description:
 */
@Data
public abstract class AbstractReq {

    @ApiModelProperty(name = "courseId", value = "课程id", dataType = "string", required = true)
    private String courseId;

    @ApiModelProperty(name = "chapterId", value = "章节id", dataType = "string", required = true)
    private String chapterId;

    @ApiModelProperty(name = "chapterName", value = "章节名称", dataType = "string")
    private String chapterName;

    @ApiModelProperty(name = "questionId", value = "问题id", dataType = "string")
    private String questionId;

    @ApiModelProperty(name = "classId", value = "班级id", dataType = "string", required = true)
    private String classId;
}
