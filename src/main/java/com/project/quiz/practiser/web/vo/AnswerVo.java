package com.project.quiz.practiser.web.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: zhangyy
 * @email: zhang10092009@hotmail.com
 * @date: 19-6-12 11:23
 * @version: 1.0
 * @description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerVo {

    private String  chapterId, chapterName, courseId, classId, studentId;

    public AnswerVo(String chapterId, String courseId,  String studentId) {
        this.chapterId = chapterId;
        this.courseId = courseId;
        this.studentId = studentId;
    }
}
