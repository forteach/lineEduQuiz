package com.project.quiz.practiser.domain;


import com.project.quiz.practiser.domain.base.AbstractAnswer;
import com.project.quiz.practiser.domain.base.QuestionAnswer;
import com.project.quiz.questionlibrary.domain.BigQuestion;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @author: zhangyy
 * @email: zhang10092009@hotmail.com
 * @date: 19-6-9 16:52
 * @version: 1.0
 * @description:　学生答题的题目信息快照表
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "questionsLists")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "学生答题的题目信息快照表", description = "学生答题的题目信息快照表")
public class QuestionsLists extends AbstractAnswer {

    @ApiModelProperty(name = "isAnswerCompleted", value = "是否回答完毕 Y/N", dataType = "string")
    private String isAnswerCompleted;

    @ApiModelProperty(name = "bigQuestions", value = "学生生成题目信息", dataType = "list")
    private List<QuestionAnswer> bigQuestions;

    @ApiModelProperty(name = "questionIds", value = "回答过的题目id集合", dataType = "list")
    private List<String> questionIds;
}