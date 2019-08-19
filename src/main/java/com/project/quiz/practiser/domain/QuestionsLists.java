package com.project.quiz.practiser.domain;


import com.project.quiz.practiser.domain.base.AbstractAnswer;
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

    @ApiModelProperty(name = "isAnswerCompleted", value = "是否回答完毕 Y/N")
    private String isAnswerCompleted;

    @ApiModelProperty(name = "bigQuestions", value = "学生生成题目信息")
    private List<BigQuestion> bigQuestions;

//    @ApiModelProperty(name = "questions", value = "已经回答过的问题信息", dataType = "list")
//    private List<String> questions;

//    @ApiModelProperty(name = "isCorrectCompleted", value = "是否批改完　Y/N", dataType = "list")
//    private String isCorrectCompleted;

//    @ApiModelProperty(name = "correctQuestionIds", value = "批改的集合列表", dataType = "list")
//    private List<String> correctQuestionIds;

//    @ApiModelProperty(name = "isReward", value = "是否奖励", dataType = "string")
//    private String isReward;
}
