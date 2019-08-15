package com.project.quiz.questionlibrary.repository;

import com.project.quiz.questionlibrary.domain.BigQuestion;
import com.project.quiz.questionlibrary.repository.base.BaseQuestionMongoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;

/**
 * @author: zhangyy
 * @email: zhang10092009@hotmail.com
 * @date: 19-8-9 15:52
 * @version: 1.0
 * @description:
 */
public interface QuestionRepository extends BaseQuestionMongoRepository <BigQuestion>{

}
