package com.project.quiz.questionlibrary.domain;

import com.project.quiz.questionlibrary.domain.base.AbstractExam;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author: zhangyy
 * @email: zhang10092009@hotmail.com
 * @date: 19-8-9 14:38
 * @version: 1.0
 * @description:
 */
@Data
@Document(collection = "questionList")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "题对象", description = "单个题目集合")
public class QuestionList extends AbstractExam {

    public QuestionList() {
    }

}
