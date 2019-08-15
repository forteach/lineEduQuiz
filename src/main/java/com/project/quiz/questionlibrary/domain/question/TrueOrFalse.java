package com.project.quiz.questionlibrary.domain.question;


import com.project.quiz.questionlibrary.domain.base.AbstractExam;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description: 判断题
 * @author: liu zhenming
 * @version: V1.0
 * @date: 2018/11/13  11:38
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "判断题", description = "BigQuestion的子项")
public class TrueOrFalse extends AbstractExam {

}
