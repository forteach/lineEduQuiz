package com.project.quiz.interaction.execute.dto;


import com.project.quiz.interaction.execute.domain.record.InteractQuestionsRecord;

import java.util.List;

/**
 * @author: zhangyy
 * @email: zhang10092009@hotmail.com
 * @date: 19-3-5 15:02
 * @version: 1.0
 * @description:
 */
public class ExerciseBooksDto {
    private List<InteractQuestionsRecord> exerciseBooks;

    public List<InteractQuestionsRecord> getExerciseBooks() {
        return exerciseBooks;
    }
}
