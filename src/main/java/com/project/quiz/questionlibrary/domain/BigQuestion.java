package com.project.quiz.questionlibrary.domain;

import com.project.quiz.questionlibrary.domain.base.AbstractExam;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @Description: 所有的题目类型 全部由大题外部封装   由examChildren展示具体的题目信息
 * @author: liu zhenming
 * @version: V1.0
 * @date: 2018/11/15  11:04
 */
@Data
@Document(collection = "bigQuestion")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "题对象", description = "所有的题目类型 全部由大题外部封装 由examChildren展示具体的题目信息")
public class BigQuestion extends AbstractExam {

}
