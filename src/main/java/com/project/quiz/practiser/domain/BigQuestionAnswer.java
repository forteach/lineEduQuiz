package com.project.quiz.practiser.domain;

import com.project.quiz.practiser.domain.base.AbstractAnswer;
import com.project.quiz.questionlibrary.domain.BigQuestion;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author: zhangyy
 * @email: zhang10092009@hotmail.com
 * @date: 19-8-19 10:26
 * @version: 1.0
 * @description:
 */
@Data
@Builder
@AllArgsConstructor
@Document(collection = "bigQuestionAnswer")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "学生练习回答详情信息", description = "学生练习作业回答详情信息")
public class BigQuestionAnswer extends AbstractAnswer {
    @Indexed
    @ApiModelProperty(name = "questionId", value = "练习题id", dataType = "string")
    private String questionId;

    @ApiModelProperty(name = "right", value = "回答结果正确与否", dataType = "string")
    private String right;

    @ApiModelProperty(name = "stuAnswer", value = "学生回答内容", dataType = "string")
    private String stuAnswer;

    @ApiModelProperty(name = "bigQuestion", value = "题目信息详情")
    private BigQuestion bigQuestion;
}