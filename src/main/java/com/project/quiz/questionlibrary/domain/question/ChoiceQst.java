package com.project.quiz.questionlibrary.domain.question;


import com.fasterxml.jackson.annotation.JsonView;
import com.project.quiz.questionlibrary.domain.base.AbstractExam;
import com.project.quiz.web.vo.BigQuestionView;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @Description: 选择题
 * @author: liu zhenming
 * @version: V1.0
 * @date: 2018/11/15  10:00
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "选择题", description = "选择题的子项 多选及单选")
public class ChoiceQst extends AbstractExam {


}
