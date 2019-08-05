package com.project.quiz.problemsetlibrary.domain;

import com.project.quiz.problemsetlibrary.domain.base.ExerciseBook;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @Description:
 * @author: liu zhenming
 * @version: V1.0
 * @date: 2019/1/13  21:23
 */
@EqualsAndHashCode(callSuper = true)
@Document(collection = "taskQuestionExerciseBook")
@Data
public class TaskQuestionExerciseBook extends ExerciseBook {
}