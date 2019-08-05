package com.project.quiz.interaction.execute.service.Key;

/**
 * @Description:
 * @author: zjw
 * @version: V1.0
 * @date: 2018/11/9 11:03
 */
public class MoreQueKey {

    /**
     * 互动练习册发布
     */
    public static final String CLASSROOM_BOOK_QUESTIONS_LIST = "BookList";

    /**
     * 课堂练习册
     */
    public static final String CLASSROOM_BOOK_NOW = "NowBook";

    /**
     * 互动练习册的题目
     */
    public static final String CLASSROOM_BOOK_QUESTIONS_MAP = "BookMap";

    /**
     * 互动方式为练习
     */
//    public static final String CLASSROOM_BOOK_QUESTIONS_ID = "Book";

    /**
     * 加入课堂，已推送过得学生回答
     */
    public static final String ROOM_JOIN_MORE_TS = "RoomJoinMore";

    /**
     * 课堂题目当前前缀
     *
     * @return now+课堂Id=map
     */
    public static String questionsBookNowMap(final String typeName, final String circleId) {
        return circleId.concat(CLASSROOM_BOOK_NOW).concat(typeName);
    }

    /**
     * 课堂练习测题目
     * @param circleId
     * @return
     */
    public static String bookQuestionMap(final String typeName,final String circleId){
        return circleId.concat(CLASSROOM_BOOK_QUESTIONS_MAP).concat(typeName);
    }

    /**
     * 课堂多题目活动互动前缀
     *
     * @return 题目列表List
     */
    public static String bookTypeQuestionsList(final String typeName, final String circleId) {
        return circleId.concat(CLASSROOM_BOOK_QUESTIONS_LIST).concat(typeName);
    }

    /**
     * 加入课堂，已推送过的学生题目回答
     * @param circleId
     * @param questBookId  册子ID
     * @param pushType  推送类型  pushQe：提问   pushAw：回答
     * @return
     */
    public static String cleanTuiSong(String circleId,String questBookId,String pushType,String questionType){
        return circleId.concat(questBookId).concat(ROOM_JOIN_MORE_TS).concat(questionType).concat(pushType);
    }

}
