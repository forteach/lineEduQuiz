package com.project.quiz.questionlibrary.repository;

import com.project.quiz.questionlibrary.domain.QuestionList;
import com.project.quiz.questionlibrary.domain.base.AbstractExam;
import com.project.quiz.questionlibrary.repository.base.BaseQuestionMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author: zhangyy
 * @email: zhang10092009@hotmail.com
 * @date: 19-8-9 15:52
 * @version: 1.0
 * @description:
 */
@Repository
public interface QuestionRepository extends BaseQuestionMongoRepository <QuestionList>{

}
