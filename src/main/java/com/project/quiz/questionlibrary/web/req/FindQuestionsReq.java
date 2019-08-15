package com.project.quiz.questionlibrary.web.req;

import com.project.quiz.web.vo.SortVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author: zhangyy
 * @email: zhang10092009@hotmail.com
 * @date: 19-8-14 16:12
 * @version: 1.0
 * @description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FindQuestionsReq extends SortVo implements Serializable {

    /**
     * 课程id
     */
    @ApiModelProperty(value = "章节id", name = "chapterId", dataType = "string", example = "463bcd8e5fed4a33883850c14f877271")
    protected String chapterId;

    @ApiModelProperty(name = "courseId", value = "课程id", dataType = "string")
    private String courseId;

    @ApiModelProperty(value = "创作老师id", name = "teacherId", dataType = "string",  example = "463bcd8e5fed4a33883850c14f877271")
    protected String teacherId;

    @ApiModelProperty(value = "考题类型  single  multiple trueOrFalse  design  bigQuestion", name = "examType", example = "single")
    protected String examType;
}