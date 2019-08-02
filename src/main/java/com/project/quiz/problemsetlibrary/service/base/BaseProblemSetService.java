package com.project.quiz.problemsetlibrary.service.base;


import com.project.quiz.problemsetlibrary.domain.base.ProblemSet;
import com.project.quiz.problemsetlibrary.web.req.ProblemSetReq;
import com.project.quiz.questionlibrary.domain.base.QuestionExamEntity;
import com.project.quiz.questionlibrary.web.req.QuestionProblemSetReq;
import com.project.quiz.web.vo.QuestionProblemSetVo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @Description:
 * @author: liu zhenming
 * @version: V1.0
 * @date: 2019/1/13  19:11
 */
public interface BaseProblemSetService<T extends ProblemSet, R extends QuestionExamEntity> {

    /**
     * 保存题册
     *
     * @param problemSet
     * @return
     */
    Mono<T> buildExerciseBook(final T problemSet);

    /**
     * 删除练习册
     *
     * @param id
     * @return
     */
    Mono<Void> delExerciseBook(final String id);

    /**
     * 根据id 获取练习册 基本信息
     *
     * @param exerciseBookId
     * @return
     */
    Mono<T> findOne(final String exerciseBookId);

    /**
     * 根据分页信息查询
     *
     * @param sortVo
     * @return
     */
    Flux<T> findProblemSet(final ProblemSetReq sortVo);

    /**
     * 查找出详情 (所有大题全部数据)
     *
     * @param exerciseBookId
     * @return
     */
    Mono<T> findAllDetailed(final String exerciseBookId);

    /**
     * 通过id查找题集及包含的题目全部信息
     *
     * @param questionProblemSetReq
     * @return
     */
    Mono<QuestionProblemSetVo> questionProblemSet(final QuestionProblemSetReq questionProblemSetReq);


}
