package com.project.quiz.practiser.domain.base;

import com.project.quiz.questionlibrary.domain.BigQuestion;
import lombok.*;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
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
