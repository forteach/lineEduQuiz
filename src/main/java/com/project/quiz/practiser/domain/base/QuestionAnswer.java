package com.project.quiz.practiser.domain.base;

import com.project.quiz.questionlibrary.domain.BigQuestion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionAnswer extends BigQuestion {
    /**
     * 学生回答结果
     */
    private Boolean right;
    /**
     * 学生回答内容
     */
    private String stuAnswer;
}
