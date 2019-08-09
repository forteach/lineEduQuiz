package com.project.quiz.questionlibrary.repository.base;


import com.project.quiz.questionlibrary.domain.base.AbstractExam;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @Description:
 * @author: liu zhenming
 * @version: V1.0
 * @date: 2019/1/10  11:10
 */
@NoRepositoryBean
public interface BaseQuestionMongoRepository<T extends AbstractExam> extends ReactiveMongoRepository<T, String> {

}