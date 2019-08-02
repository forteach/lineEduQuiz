package com.project.quiz.repository;

import com.project.quiz.interaction.execute.domain.AskAnswer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * @Description:
 * @author: liu zhenming
 * @version: V1.0
 * @date: 2018/12/3  11:07
 */
public interface AskAnswerRepository extends ReactiveMongoRepository<AskAnswer, String> {
}
