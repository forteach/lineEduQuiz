package com.project.quiz.questionlibrary.service;


/**
 * @Description:
 * @author: liu zhenming
 * @version: V1.0
 * @date: 2018/11/13  11:45
 */
//@Service
//public class BigQuestionService extends BaseQuestionServiceImpl<BigQuestion> {


//    private final BigQuestionRepository bigQuestionRepository;
//
//    private final BigQuestionProblemSetRepository bigQuestionProblemSetRepository;
//
//    public BigQuestionService(QuestionMongoRepository<BigQuestion> repository,
//                              ReactiveMongoTemplate reactiveMongoTemplate,
//                              QuestionReflect questionReflect,
//                              BigQuestionRepository bigQuestionRepository,
//                              BigQuestionProblemSetRepository bigQuestionProblemSetRepository) {
//
//        super(repository, reactiveMongoTemplate, questionReflect);
//        this.bigQuestionRepository = bigQuestionRepository;
//        this.bigQuestionProblemSetRepository = bigQuestionProblemSetRepository;
//    }

    /**
     * 批量保存修改
     *
     * @param questionList
     * @return
     */
//    private Flux<BigQuestion> editBigQuestion(final List<BigQuestion> questionList) {
//        return bigQuestionRepository.saveAll(questionList);
//    }

    /**
     * 更改问题 (根据需要是否修改至练习册)
     *
     * @param relate
     * @param questionList
     * @return
     */
//    public Flux<BigQuestion> editExerciseBookQuestion(int relate, final List<BigQuestion> questionList) {
//        if (relate == COVER_QUESTION_BANK) {
//            return editBigQuestion(questionList);
//        }
//        return Flux.fromIterable(questionList);
//    }

    /**
     * 设置大题的id及属性
     *
     * @param bigQuestion
     * @return
     */
//    private BigQuestion setBigQuestionUUID(final BigQuestion bigQuestion) {
//        bigQuestion.setExamChildren((List) bigQuestion.getExamChildren()
//                .stream()
//                .map(obj -> {
//                    JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(obj));
//                    String type = jsonObject.getString(BIG_QUESTION_EXAM_CHILDREN_TYPE);
//                    switch (type) {
//                        case QUESTION_CHOICE_OPTIONS_SINGLE:
//                        case QUESTION_CHOICE_MULTIPLE_SINGLE:
//                            ChoiceQst choiceQst = JSON.parseObject(jsonObject.toJSONString(), ChoiceQst.class);
//                            if (StrUtil.isEmpty(choiceQst.getId())) {
//                                choiceQst.setId(IdUtil.fastSimpleUUID());
//                            }
//                            choiceQst.setExamType("bigQuestion");
//                            return choiceQst;
//                        case BIG_QUESTION_EXAM_CHILDREN_TYPE_TRUEORFALSE:
//                            TrueOrFalse trueOrFalse = JSON.parseObject(jsonObject.toJSONString(), TrueOrFalse.class);
//                            if (StrUtil.isEmpty(trueOrFalse.getId())) {
//                                trueOrFalse.setId(IdUtil.fastSimpleUUID());
//                            }
//                            trueOrFalse.setExamType("bigQuestion");
//                            return trueOrFalse;
//                        case BIG_QUESTION_EXAM_CHILDREN_TYPE_DESIGN:
//                            Design design = JSON.parseObject(jsonObject.toJSONString(), Design.class);
//                            if (StrUtil.isEmpty(design.getId())) {
//                                design.setId(IdUtil.fastSimpleUUID());
//                            }
//                            design.setExamType("bigQuestion");
//                            return design;
//                        default:
//                            throw new ExamQuestionsException("非法参数 错误的题目类型");
//                    }
//
//                })
//                .collect(Collectors.toList()));
//        return bigQuestion;
//    }

    /**
     * 更新大题中的某项
     *
//     * @param childrenId
//     * @param json
//     * @return
     */
//    public Mono<Boolean> updateChildren(final String childrenId, final String json, final String teacherId) {
//
//        JSONObject jsonObject = JSON.parseObject(json);
//
//        String type = jsonObject.getString(BIG_QUESTION_EXAM_CHILDREN_TYPE);
//
//        Query query = Query.query(Criteria.where(BIG_QUESTION_EXAM_CHILDREN + "." + MONGDB_ID).is(childrenId));
//        Update update = new Update();
//
//        switch (type) {
//            case QUESTION_CHOICE_OPTIONS_SINGLE:
//            case QUESTION_CHOICE_MULTIPLE_SINGLE:
//                ChoiceQst choiceQst = JSON.parseObject(jsonObject.toJSONString(), ChoiceQst.class);
//                choiceQst.setId(childrenId);
//
//                update.set("examChildren.$.score", choiceQst.getScore());
//                update.set("examChildren.$.teacherId", teacherId);
//                update.set("examChildren.$.examType", "bigQuestion");
//                update.set("examChildren.$.choiceQstTxt", choiceQst.getChoiceQstTxt());
//                update.set("examChildren.$.choiceQstAnsw", choiceQst.getChoiceQstAnsw());
//                update.set("examChildren.$.choiceQstAnalysis", choiceQst.getChoiceQstAnalysis());
//                update.set("examChildren.$.choiceType", choiceQst.getChoiceType());
//                update.set("examChildren.$.optChildren", choiceQst.getOptChildren());
//
//                break;
//            case BIG_QUESTION_EXAM_CHILDREN_TYPE_TRUEORFALSE:
//                TrueOrFalse trueOrFalse = JSON.parseObject(jsonObject.toJSONString(), TrueOrFalse.class);
//                trueOrFalse.setId(childrenId);
//
//                update.set("examChildren.$.score", trueOrFalse.getScore());
//                update.set("examChildren.$.teacherId", teacherId);
//                update.set("examChildren.$.examType", "bigQuestion");
//                update.set("examChildren.$.trueOrFalseInfo", trueOrFalse.getTrueOrFalseInfo());
//                update.set("examChildren.$.trueOrFalseAnsw", trueOrFalse.getTrueOrFalseAnsw());
//                update.set("examChildren.$.trueOrFalseAnalysis", trueOrFalse.getTrueOrFalseAnalysis());
//                break;
//            case BIG_QUESTION_EXAM_CHILDREN_TYPE_DESIGN:
//                Design design = JSON.parseObject(jsonObject.toJSONString(), Design.class);
//                design.setId(childrenId);
//
//                update.set("examChildren.$.score", design.getScore());
//                update.set("examChildren.$.teacherId", teacherId);
//                update.set("examChildren.$.examType", "bigQuestion");
//                update.set("examChildren.$.designQuestion", design.getDesignQuestion());
//                update.set("examChildren.$.designAnsw", design.getDesignAnsw());
//                update.set("examChildren.$.designAnalysis", design.getDesignAnalysis());
//                break;
//            default:
//                throw new ExamQuestionsException("非法参数 错误的题目类型");
//        }
//
//        return reactiveMongoTemplate.updateFirst(query, update, BigQuestion.class).map(UpdateResult::wasAcknowledged);
//
//
//    }

    /**
     * 删除大题中的某项
     *
//     * @param childrenId
//     * @return
     */
//    public Mono<Boolean> deleteChildren(final String childrenId) {
//
//        Query query = Query.query(Criteria.where(BIG_QUESTION_EXAM_CHILDREN + "." + MONGDB_ID).is(childrenId));
//
//        Update update = new Update();
//        update.pull("examChildren", new BasicDBObject(MONGDB_ID, childrenId));
//        return reactiveMongoTemplate.updateFirst(query, update, BigQuestion.class).map(Objects::nonNull);
//    }

    /**
     * 新增大题子题目一项
     *
//     * @param questionId
//     * @return
     */
//    public Mono<Boolean> addChildren(final String questionId, final String json, final String teacherId) {
//
//        JSONObject jsonObject = JSON.parseObject(json);
//
//        Query query = Query.query(Criteria.where(MONGDB_ID).is(questionId));
//
//        Update update = new Update();
//
//        String type = jsonObject.getString(BIG_QUESTION_EXAM_CHILDREN_TYPE);
//
//        BigQuestion bigQuestion = new BigQuestion();
//
//        switch (type) {
//            case QUESTION_CHOICE_OPTIONS_SINGLE:
//            case QUESTION_CHOICE_MULTIPLE_SINGLE:
//                ChoiceQst choiceQst = JSON.parseObject(jsonObject.toJSONString(), ChoiceQst.class);
//                choiceQst.setTeacherId(teacherId);
//                bigQuestion.setExamChildren(Collections.singletonList(choiceQst));
//                break;
//            case BIG_QUESTION_EXAM_CHILDREN_TYPE_TRUEORFALSE:
//                TrueOrFalse trueOrFalse = JSON.parseObject(jsonObject.toJSONString(), TrueOrFalse.class);
//                trueOrFalse.setTeacherId(teacherId);
//                bigQuestion.setExamChildren(Collections.singletonList(trueOrFalse));
//                break;
//            case BIG_QUESTION_EXAM_CHILDREN_TYPE_DESIGN:
//                Design design = JSON.parseObject(jsonObject.toJSONString(), Design.class);
//                design.setTeacherId(teacherId);
//                bigQuestion.setExamChildren(Collections.singletonList(design));
//                break;
//            default:
//                throw new ExamQuestionsException("非法参数 错误的题目类型");
//        }
//        setBigQuestionUUID(bigQuestion);
//        update.push("examChildren", bigQuestion.getExamChildren().get(0));
//        return reactiveMongoTemplate.updateFirst(query, update, BigQuestion.class).map(Objects::nonNull);
//    }

    /**
     * 修改新增大题
     *
//     * @param bigQuestion
//     * @return
     */
//    public Mono<BigQuestion> editBigQuestion(final BigQuestion bigQuestion) {
//        return editQuestions(setBigQuestionUUID(bigQuestion)).flatMap(t -> {
//            Mono<UpdateResult> questionBankMono = questionBankAssociation(t.getId(), t.getTeacherId());
//            return questionBankMono.flatMap(
//                    updateResult -> {
//                        if (updateResult.isModifiedCountAvailable()) {
//                            return Mono.just(t);
//                        } else {
//                            return Mono.error(new ExamQuestionsException("保存 大题 作者失败"));
//                        }
//                    }
//            );
//        });
//    }

//}