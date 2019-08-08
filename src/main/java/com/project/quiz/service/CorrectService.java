package com.project.quiz.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.project.quiz.common.DefineCode;
import com.project.quiz.common.MyAssert;
import com.project.quiz.exceptions.ExamQuestionsException;
import com.project.quiz.interaction.service.Key.SingleQueKey;
import com.project.quiz.questionlibrary.domain.BigQuestion;
import com.project.quiz.questionlibrary.domain.question.ChoiceQst;
import com.project.quiz.questionlibrary.domain.question.TrueOrFalse;
import com.project.quiz.questionlibrary.repository.BigQuestionRepository;
import com.project.quiz.repository.ProblemSetBackupRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.time.Duration;

import static com.project.quiz.common.Dic.*;

/**
 * @Description: 判断题目信息相关操作
 * @author: zjw
 * @version: V1.0
 * @date: 2018/11/20  16:55
 */
@Slf4j
@Service
public class CorrectService {

    private final ProblemSetBackupRepository problemSetBackupRepository;

    private final BigQuestionRepository bigQuestionRepository;

    private final ReactiveStringRedisTemplate stringRedisTemplate;

    public CorrectService(ProblemSetBackupRepository problemSetBackupRepository, BigQuestionRepository bigQuestionRepository, ReactiveStringRedisTemplate stringRedisTemplate) {
        this.problemSetBackupRepository = problemSetBackupRepository;
        this.bigQuestionRepository = bigQuestionRepository;
        this.stringRedisTemplate = stringRedisTemplate;
    }


    //TODO  题目回答更正回答记录
    public Mono<Boolean> correcting(final String key, final String questionId, final String answer) {
        //找到题目信息 TODO OLD
        return getBigQuestion(key, questionId)
                .flatMap(bigQuestion -> {
                    return result(bigQuestion, answer);
                });
    }

    /**
     * 从Redis或Mongo获得题目内容
     *
     * @param questionId
     * @return
     */
    public Mono<BigQuestion> getBigQuestion(String key, String questionId) {
        //String key= BigQueKey.questionsNow(questionId);
        return stringRedisTemplate.hasKey(key)
                .flatMap(r -> r.booleanValue() ? stringRedisTemplate.opsForValue().get(key).flatMap(str -> Mono.just(JSON.parseObject(str, BigQuestion.class))) : bigQuestionRepository.findById(questionId).filterWhen(obj -> stringRedisTemplate.opsForValue().set(SingleQueKey.questionsNow(questionId), JSON.toJSONString(obj), Duration.ofSeconds(60 * 60 * 2))));
    }


    private boolean choice(final ChoiceQst choiceQst, final String answer) {
        switch (choiceQst.getChoiceType()) {
            //单选
            case QUESTION_CHOICE_OPTIONS_SINGLE:
                return radio(choiceQst, answer);
            //多选
            case QUESTION_CHOICE_MULTIPLE_SINGLE:
//                return multiple(choiceQst, answer);
                return radio(choiceQst, answer);
            default:
                MyAssert.isFalse(false, DefineCode.ERR0002, "题目答案类型错误！");
                return false;
        }
    }

    //直接对比答案
    private boolean radio(final ChoiceQst choiceQst, final String answer) {
        return answer.equals(choiceQst.getChoiceQstAnsw());
    }


    /**
     * 判断判断是否回答正确
     *  TODO 此处判端　判断题需要重新修改属性
     * @param trueOrFalse
     * @param answer
     * @return
     */
    private boolean trueOrFalse(final TrueOrFalse trueOrFalse, final String answer) {
        if (trueOrFalse.getTrueOrFalseAnsw().equals(QUESTION_TRUE_OR_FALSE_Y.equals(answer) ? true : false)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断学生答题的结果
     *
     * @param bigQuestion
     * @param answer
     * @return
     */
    public Mono<Boolean> result(final BigQuestion bigQuestion, final String answer) {
        return Mono.just(bigQuestion)
                .flatMap(question -> {
                    final JSONObject json = JSON.parseObject(JSON.toJSONString(question));
                    switch (String.valueOf(JSONPath.eval(json, "$.examChildren[0].examType"))) {
                        //单选
                        case QUESTION_CHOICE_OPTIONS_SINGLE:
                            //选择题
                            ChoiceQst choiceQst1 = JSON.parseObject(JSONPath.eval(json, "$.examChildren[0]").toString(), ChoiceQst.class);
                            return Mono.just(choice(choiceQst1, answer));
                        //多选
                        case QUESTION_CHOICE_MULTIPLE_SINGLE:
                            ChoiceQst choiceQst = JSON.parseObject(JSONPath.eval(json, "$.examChildren[0]").toString(), ChoiceQst.class);
                            return Mono.just(choice(choiceQst, answer));
                        //判断
                        case BIG_QUESTION_EXAM_CHILDREN_TYPE_TRUEORFALSE:
                            TrueOrFalse trueOrFalse = JSON.parseObject(JSONPath.eval(json, "$.examChildren[0]").toString(), TrueOrFalse.class);
                            return Mono.just(trueOrFalse(trueOrFalse, answer));
                        //简单题
                        case BIG_QUESTION_EXAM_CHILDREN_TYPE_DESIGN:
                            // TODO 简答主观题 人工手动批改，应增加认为的评判对错结果
                            return Mono.just(true);
                        default:
                            log.error("非法参数 错误的题目类型 : {}", String.valueOf(JSONPath.eval(json, "$.examChildren[0].examType")));
                            throw new ExamQuestionsException("非法参数 错误的题目类型");
                    }
                });
    }

//
//
//    /**
//     * @param sheetMono
//     * @return
//     */
//    Mono<ExerciseBookSheet> exerciseBookCorrect(final Mono<ExerciseBookSheet> sheetMono) {
//        return sheetMono.flatMap(seet -> problemSetBackupRepository.findById(seet.getBackupId())
//                .map(b -> JSON.parseObject(b.getBackup(), new TypeReference<ExerciseBook<BigQuestion>>() {
//                }))
//                .map(exerciseBook -> {
//                    //遍历答案 批改客观题
//                    seet.setAnsw(seet.getAnsw().stream().peek(answ -> answCorrect(answ, exerciseBook)).collect(Collectors.toList()));
//                    return exerciseBook;
//                }).flatMap(exerciseBook -> {
//                    if (exerciseBook != null) {
//                        return Mono.just(seet);
//                    } else {
//                        return Mono.empty();
//                    }
//                }));
//    }
//
//    Mono<ExerciseBookSheet> subjectiveCorrect(final ExerciseBookSheet sheet, final ExerciseBookSheetVo correctVo) {
//        if (log.isDebugEnabled()){
//            log.debug("练习册 答题卡 参数　sheet : {}, correctVo : {}", sheet.toString(), correctVo.toString());
//        }
//        JSONObject json = JSON.parseObject(JSON.toJSONString(correctVo));
//        sheet.setEvaluation(correctVo.getEvaluation());
//        sheet.setAnsw(
//                sheet.getAnsw().stream().peek(answ ->
//                        answ.setChildrenList(answ.getChildrenList().stream().peek(answChildren ->
//                                answChildren.setEvaluation(String.valueOf(JSONPath.eval(json, "$.answ.childrenList[questionId = '" + answChildren.getQuestionId() + "'].evaluation[0]")))
//                        ).collect(toList()))
//                ).collect(toList())
//        );
//        return Mono.just(sheet);
//    }
//
//    /**
//     * @param answ
//     * @return
//     */
//    private void answCorrect(final Answ answ, final ExerciseBook<BigQuestion> exerciseBook) {
//        if (log.isDebugEnabled()){
//            log.debug("遍历答案 批改客观题 参数 ==> answ : {}, exerciseBook : {}", answ.toString(), exerciseBook.toString());
//        }
//        BigQuestion question = exerciseBook.getQuestionChildren().stream()
//                .filter(bigQuestion -> bigQuestion.getId().equals(answ.getBigQuestionId()))
//                .findFirst()
//                .get();
//
//        answ.setChildrenList(answ.getChildrenList().stream().peek(answChildren -> correcting(answChildren, question)).collect(Collectors.toList()));
//
//        answ.setScore(answ.getChildrenList().stream().filter(answChildren -> answChildren.getScore() != null).mapToDouble(AnswChildren::getScore).sum());
//    }
//
//    private void correcting(final AnswChildren answChildren, final BigQuestion question) {
//
//        question.getExamChildren().stream().forEach(obj -> {
//            JSONObject jsonObject = (JSONObject) obj;
//            if (jsonObject.getString(ID).equals(answChildren.getQuestionId())) {
//                String type = jsonObject.getString(BIG_QUESTION_EXAM_CHILDREN_TYPE);
//
//                switch (type) {
//                    case QUESTION_CHOICE_OPTIONS_SINGLE:
//                    case QUESTION_CHOICE_MULTIPLE_SINGLE:
//                        ChoiceQst choiceQst = JSON.parseObject(jsonObject.toJSONString(), ChoiceQst.class);
//                        answChildren.setScore(choice(choiceQst, answChildren));
//                        break;
//                    case BIG_QUESTION_EXAM_CHILDREN_TYPE_TRUEORFALSE:
//                        TrueOrFalse trueOrFalse = JSON.parseObject(jsonObject.toJSONString(), TrueOrFalse.class);
//                        answChildren.setScore(trueOrFalse(trueOrFalse, answChildren));
//                        break;
//                    case BIG_QUESTION_EXAM_CHILDREN_TYPE_DESIGN:
//                        //简答主观题 人工手动批改
//                        break;
//                    default:
//                        log.error("非法参数 错误的题目类型 : {}", type);
//                        throw new ExamQuestionsException("非法参数 错误的题目类型");
//                }
//            }
//        });
//    }

//
//    private Double choice(final ChoiceQst choiceQst, final AnswChildren answChildren) {
//        switch (choiceQst.getChoiceType()) {
//            //单选
//            case QUESTION_CHOICE_OPTIONS_SINGLE:
//                return radio(choiceQst, answChildren);
//            //多选
//            case QUESTION_CHOICE_MULTIPLE_SINGLE:
//                return multiple(choiceQst, answChildren);
//            default:
//                log.error("非法参数 错误的选择题选项类型 : {}", choiceQst.getChoiceType());
//                throw new ExamQuestionsException("非法参数 错误的选择题选项类型");
//        }
//    }

//    private boolean multiple(final ChoiceQst choiceQst, final String solution) {
//        //正确答案集
//        List<String> answer = Arrays.asList(",".split(choiceQst.getChoiceQstAnsw()));
//        //回答集
//        List<String> exAnswer = Arrays.asList(",".split(solution));
//        //交集
//        List<String> intersection = answer.stream().filter(exAnswer::contains).collect(Collectors.toList());
//        //差集
//        List<String> reduce1 = exAnswer.stream().filter(item -> !answer.contains(item)).collect(toList());
//
//        //交集与正确集与答案集一致  满分
//        if (answer.size() == intersection.size() && exAnswer.size() == intersection.size()) {
//            return true;
//        } else if (exAnswer.size() <= answer.size()) {
//            if (reduce1.size() >= 1 || exAnswer.size() == 1) {
//                return false;
//            } else {
//                return false;
//            }
//        } else {
//            return false;
//        }
//    }
//
//    private Double radio(final ChoiceQst choiceQst, final AnswChildren answChildren) {
//
//        if (answChildren.getAnswer().equals(choiceQst.getChoiceQstAnsw())) {
//            answChildren.setScore(choiceQst.getScore());
//            answChildren.setEvaluation(QUESTION_ACCURACY_TRUE);
//        } else {
//            answChildren.setScore(QUESTION_ZERO);
//            answChildren.setEvaluation(QUESTION_ACCURACY_FALSE);
//        }
//        return answChildren.getScore();
//    }
//
//    private Double multiple(final ChoiceQst choiceQst, final AnswChildren answChildren) {
//
//        //正确答案集
//        List<String> answer = Arrays.asList(",".split(choiceQst.getChoiceQstAnsw()));
//        //回答集
//        List<String> exAnswer = Arrays.asList(",".split(answChildren.getAnswer()));
//        //交集
//        List<String> intersection = answer.stream().filter(exAnswer::contains).collect(Collectors.toList());
//        //差集
//        List<String> reduce1 = exAnswer.stream().filter(item -> !answer.contains(item)).collect(toList());
//
//        //交集与正确集与答案集一致  满分
//        if (answer.size() == intersection.size() && exAnswer.size() == intersection.size()) {
//            answChildren.setScore(choiceQst.getScore());
//            answChildren.setEvaluation(QUESTION_ACCURACY_TRUE);
//        } else if (exAnswer.size() <= answer.size()) {
//            if (reduce1.size() >= 1 || exAnswer.size() == 1) {
//                answChildren.setScore(QUESTION_ZERO);
//                answChildren.setEvaluation(QUESTION_ACCURACY_FALSE);
//            } else {
//                answChildren.setScore(QUESTION_ONE);
//                answChildren.setEvaluation(QUESTION_ACCURACY_HALFOF);
//            }
//        } else {
//            answChildren.setScore(QUESTION_ZERO);
//            answChildren.setEvaluation(QUESTION_ACCURACY_FALSE);
//        }
//        return answChildren.getScore();
//    }
//
//    private Double trueOrFalse(final TrueOrFalse trueOrFalse, final AnswChildren answChildren) {
//        if (trueOrFalse.getTrueOrFalseAnsw().equals(Boolean.valueOf(answChildren.getAnswer()))) {
//            answChildren.setScore(trueOrFalse.getScore());
//            answChildren.setEvaluation(QUESTION_ACCURACY_TRUE);
//        } else {
//            answChildren.setScore(QUESTION_ZERO);
//            answChildren.setEvaluation(QUESTION_ACCURACY_FALSE);
//        }
//        return answChildren.getScore();
//    }

}
