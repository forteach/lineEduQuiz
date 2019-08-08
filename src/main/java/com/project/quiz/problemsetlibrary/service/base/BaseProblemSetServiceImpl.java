//package com.project.quiz.problemsetlibrary.service.base;
//
//
//import cn.hutool.core.bean.BeanUtil;
//import cn.hutool.core.util.StrUtil;
//import com.project.quiz.domain.BaseEntity;
//import com.project.quiz.domain.QuestionIds;
//import com.project.quiz.exceptions.CustomException;
//import com.project.quiz.exceptions.ExamQuestionsException;
//import com.project.quiz.problemsetlibrary.domain.base.ProblemSet;
//import com.project.quiz.problemsetlibrary.repository.base.ProblemSetMongoRepository;
//import com.project.quiz.problemsetlibrary.web.req.ProblemSetReq;
//import com.project.quiz.questionlibrary.domain.base.QuestionExamEntity;
//import com.project.quiz.questionlibrary.repository.base.QuestionMongoRepository;
//import com.project.quiz.questionlibrary.service.base.BaseQuestionService;
//import com.project.quiz.questionlibrary.web.req.QuestionBankReq;
//import com.project.quiz.questionlibrary.web.req.QuestionProblemSetReq;
//import com.project.quiz.web.pojo.ProblemSetDet;
//import com.project.quiz.web.vo.QuestionProblemSetVo;
//import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.data.mongodb.core.query.Query;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//import java.lang.reflect.ParameterizedType;
//import java.util.Comparator;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//import static com.project.quiz.common.Dic.PARAMETER_ALL;
//import static com.project.quiz.common.Dic.PARAMETER_PART;
//
///**
// * @Description:
// * @author: liu zhenming
// * @version: V1.0
// * @date: 2019/1/13  19:12
// */
//public abstract class BaseProblemSetServiceImpl<T extends ProblemSet, R extends QuestionExamEntity> implements BaseProblemSetService<T, R> {
//
//    protected final ReactiveMongoTemplate reactiveMongoTemplate;
//
//    private final ProblemSetMongoRepository<T> repository;
//
//    private final QuestionMongoRepository<R> questionRepository;
//
//    private final BaseQuestionService<R> questionService;
//
//    public BaseProblemSetServiceImpl(ReactiveMongoTemplate reactiveMongoTemplate, ProblemSetMongoRepository<T> repository,
//                                     QuestionMongoRepository<R> questionRepository, BaseQuestionService<R> questionService) {
//        this.reactiveMongoTemplate = reactiveMongoTemplate;
//        this.repository = repository;
//        this.questionRepository = questionRepository;
//        this.questionService = questionService;
//    }
//
//    /**
//     * 保存题册
//     *
//     * @param problemSet
//     * @return
//     */
//    @Override
//    public Mono<T> buildExerciseBook(final T problemSet) {
//        return repository.save(problemSet);
//    }
//
//    /**
//     * 删除练习册
//     *
//     * @param id
//     * @return
//     */
//    @Override
//    public Mono<Void> delExerciseBook(final String id) {
//        return repository.deleteById(id);
//    }
//
//    /**
//     * 根据id 获取练习册 基本信息
//     *
//     * @param exerciseBookId
//     * @return
//     */
//    @Override
//    public Mono<T> findOne(final String exerciseBookId) {
//        return repository.findById(exerciseBookId);
//    }
//
//    /**
//     * 查找出详情 (所有大题全部数据)
//     *
//     * @param exerciseBookId
//     * @return
//     */
//    @Override
//    public Mono<T> findAllDetailed(final String exerciseBookId) {
//        return findOne(exerciseBookId)
//                .flatMap(set -> {
//                    /*
//                     * 提取所有的问题id集
//                     * 提取对照的index坐标 后续排序
//                     * */
//                    final List<String> list = set.getQuestionIds().stream()
//                            .map(QuestionIds::getBigQuestionId).collect(Collectors.toList());
//                    final Map<String, Integer> indexMap = set.getQuestionIds().stream()
//                            .collect(Collectors.toMap(QuestionIds::getBigQuestionId, QuestionIds::getIndex));
//
//                    return findByIdQuestion(list)
//                            .sort(Comparator.comparing(question -> indexMap.get(question.getId())))
//                            .collectList()
//                            .map(monoList -> (T) (new ProblemSetDet<R>(set, monoList)));
//                });
//    }
//
//    /**
//     * 根据id批量查询
//     *
//     * @param id
//     * @return
//     */
//    private Flux<R> findByIdQuestion(final List<String> id) {
//        return questionRepository.findAllById(id);
//    }
//
//    /**
//     * 分页查询基本数据 (大题不含题干)
//     *
//     * @param sortVo
//     * @return
//     */
//    private Flux<T> findPratProblemSet(final ProblemSetReq sortVo) {
//
//        Criteria criteria = Criteria.where("teacherId").is(sortVo.getOperatorId());
//
//        Query query = new Query(criteria);
//
//        if (StrUtil.isNotEmpty(sortVo.getExeBookType())) {
//            criteria.and("exeBookType").in(Integer.parseInt(sortVo.getExeBookType()));
//        }
//        if (StrUtil.isNotEmpty(sortVo.getChapterId())) {
//            criteria.and("chapterId").in(sortVo.getChapterId());
//        }
//        if (StrUtil.isNotEmpty(sortVo.getLevelId())) {
//            criteria.and("levelId").in(sortVo.getLevelId());
//        }
//        if (StrUtil.isNotEmpty(sortVo.getCourseId())) {
//            criteria.and("courseId").in(sortVo.getCourseId());
//        }
//
//        sortVo.queryPaging(query);
//
//        return reactiveMongoTemplate.find(query, entityClass());
//    }
//
//    /**
//     * 分页查询详细数据
//     *
//     * @param sortVo
//     * @return
//     */
//    private Flux<T> findAllProblemSet(final ProblemSetReq sortVo) {
//        return findPratProblemSet(sortVo).flatMap(obj -> findAllDetailed(obj.getId()));
//    }
//
//
//    /**
//     * 根据分页信息查询
//     *
//     * @param sortVo
//     * @return
//     */
//    @Override
//    public Flux<T> findProblemSet(final ProblemSetReq sortVo) {
//
//        if (PARAMETER_PART.equals(sortVo.getAllOrPart())) {
//            return findPratProblemSet(sortVo);
//        } else if (PARAMETER_ALL.equals(sortVo.getAllOrPart())) {
//            return findAllProblemSet(sortVo);
//        }
//
//        return Flux.error(new ExamQuestionsException("错误的查询条件"));
//
//    }
//
//
//    /**
//     * 获取泛型的class
//     *
//     * @return
//     */
//    private Class<T> entityClass() {
//        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
//    }
//
//    /**
//     * 获取Question泛型的class
//     *
//     * @return
//     */
//    private Class<R> questionClass() {
//        return (Class<R>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
//    }
//
//    private <C> C instantiate(Class<C> c) {
//        try {
//            return c.newInstance();
//        } catch (Exception e) {
//            throw new CustomException("反射实例化泛型出错" + e);
//        }
//    }
//
//    /**
//     * 通过id查找题集及包含的题目全部信息
//     *
//     * @param questionProblemSetReq
//     * @return
//     */
//    @Override
//    public Mono<QuestionProblemSetVo> questionProblemSet(final QuestionProblemSetReq questionProblemSetReq) {
//
//        QuestionBankReq questionBankReq = new QuestionProblemSetReq();
//        BeanUtil.copyProperties(questionProblemSetReq, questionBankReq);
//
//        Mono<List<R>> questionFlux = questionService.findAllDetailed(questionBankReq).collectList();
//
//        return questionFlux.zipWith(findProblemSet(questionProblemSetReq.getProblemSetId()), (questionList, problemSet) -> {
//
//            List<String> target = problemSet.getQuestionIds().stream().map(QuestionIds::getBigQuestionId).collect(Collectors.toList());
//            List<String> origin = questionList.stream().map(BaseEntity::getId).collect(Collectors.toList());
//            //交集
//            List<String> intersection = origin.stream().filter(target::contains).collect(Collectors.toList());
//            //差集
//            List<String> difference = origin.stream().filter(item -> !target.contains(item)).collect(Collectors.toList());
//            return QuestionProblemSetVo.builder().bigQuestionList(questionList).problemSet(problemSet).intersection(intersection).difference(difference).build();
//        });
//    }
//
//    /**
//     * 根据id 获取练习册 基本信息
//     *
//     * @param exerciseBookId
//     * @return
//     */
//    private Mono<T> findProblemSet(final String exerciseBookId) {
//        return repository.findById(exerciseBookId);
//    }
//}
