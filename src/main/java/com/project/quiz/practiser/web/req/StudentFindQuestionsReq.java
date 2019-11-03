package com.project.quiz.practiser.web.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

import static com.project.quiz.practiser.constant.Dic.STUDENT_QUESTIONS;

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

//    @ApiModelProperty(name = "chapterName", value = "章节名称", dataType = "string", required = true)
//    private String chapterName;

    @ApiModelProperty(name = "number", value = "随机题目数量", dataType = "int", required = true)
    private Integer number = 5;

    @ApiModelProperty(name = "classId", value = "班级id", dataType = "string")
    private String classId;

    @ApiModelProperty(hidden = true)
    private String studentId;

    public String getKey(){
//        return STUDENT_QUESTIONS.concat(studentId).concat("#").concat(courseId).concat("#").concat(chapterId);
        return STUDENT_QUESTIONS.concat(studentId).concat("#").concat(chapterId);
    }
}
