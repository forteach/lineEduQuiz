package com.project.quiz.repository;


import com.project.quiz.domain.ExerciseBookSheet;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * @Description:
 * @author: liu zhenming
 * @version: V1.0
 * @date: 2018/11/19  14:00
 */
public interface ExerciseBookSheetRepository extends ReactiveMongoRepository<ExerciseBookSheet, String> {
}
