package com.project.quiz.problemsetlibrary.service.base;


import com.mongodb.client.result.UpdateResult;
import com.project.quiz.problemsetlibrary.domain.base.ExerciseBook;
import com.project.quiz.problemsetlibrary.web.req.ExerciseBookReq;
import com.project.quiz.problemsetlibrary.web.vo.DelExerciseBookPartVo;
import com.project.quiz.problemsetlibrary.web.vo.ProblemSetVo;
import com.project.quiz.questionlibrary.domain.base.QuestionExamEntity;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @Description:
 * @author: liu zhenming
 * @version: V1.0
 * @date: 2019/1/13  21:49
 */
public interface BaseExerciseBookService<T extends ExerciseBook, R extends QuestionExamEntity> {

    /**
     * 按照顺序 保存练习册
     *
     * @param problemSetVo
     * @return
     */
    Mono<T> buildBook(final ProblemSetVo problemSetVo);

    /**
     * 查找挂接的课堂练习题
     *
     * @param sortVo
     * @return
     */
    Mono<List<R>> findExerciseBook(final ExerciseBookReq sortVo);

    /**
     * 删除课堂练习题部分子文档
     *
     * @param delVo
     * @return
     */
    Mono<UpdateResult> delExerciseBookPart(final DelExerciseBookPartVo delVo);

    /**
     * 查找详细的挂接的课堂练习题
     *
     * @param sortVo
     * @return
     */
    Mono<List<R>> findDetailedExerciseBook(final ExerciseBookReq sortVo);


}
