package com.project.quiz.practiser.repository;


import com.project.quiz.practiser.domain.AskAnswerExercise;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * @author: zhangyy
 * @email: zhang10092009@hotmail.com
 * @date: 19-6-9 16:59
 * @version: 1.0
 * @description: 学生回答记录信息表
 */
public interface AnswerListsRepository extends ReactiveMongoRepository<AskAnswerExercise, String> {

}
