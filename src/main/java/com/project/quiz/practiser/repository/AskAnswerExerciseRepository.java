package com.project.quiz.practiser.repository;


import com.project.quiz.practiser.domain.AskAnswerExercise;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * @author: zhangyy
 * @email: zhang10092009@hotmail.com
 * @date: 19-6-5 10:36
 * @version: 1.0
 * @description:　学生回答详情记录表(包含老师评价)
 */
public interface AskAnswerExerciseRepository extends ReactiveMongoRepository<AskAnswerExercise, String> {

}
