package com.project.quiz.common;

/**
 * @Description:
 * @author: liu zhenming
 * @version: V1.0
 * @date: 2018/11/9 11:03
 */
public class Dic {

    /**
     * 默认错误代码
     */
    public static final int DEFAULT_ERROR_CODE = 9999;

    /**
     * mongodb _id
     */
    public static final String MONGDB_ID = "_id";

    /**
     * mongodb
     */
    public static final String MONGDB_COLUMN_QUESTION_TEACHER_ID = "teacherId";

    /**
     *
     */
    public static final String STATUS_SUCCESS = "0";

    public static final String DISTINCT_INITIAL = "initial";

    public static final String COMMIT_EXERCISE_BOOK_SHEET_COMMIT = "commit";

    public static final String COMMIT_EXERCISE_BOOK_SHEET_MODIFY = "modify";

    public static final String COMMIT_EXERCISE_BOOK_SHEET_CORRECT = "correct";

    public static final String ID = "id";

    public static final String BIG_QUESTION_EXAM_CHILDREN_TYPE = "examType";

    public static final String BIG_QUESTION_EXAM_CHILDREN_TYPE_CHOICE = "choice";

    //单选
    public static final String QUESTION_CHOICE_OPTIONS_SINGLE = "single";
    //多选
    public static final String QUESTION_CHOICE_MULTIPLE_SINGLE = "multiple";

    public static final String BIG_QUESTION_EXAM_CHILDREN_TYPE_TRUEORFALSE = "trueOrFalse";

    public static final String BIG_QUESTION_EXAM_CHILDREN_TYPE_DESIGN = "design";

    public static final Double QUESTION_ZERO = 0.0;

    public static final Double QUESTION_ONE = 1.0;

    public static final String QUESTION_ACCURACY_TRUE = "true";

    public static final String QUESTION_ACCURACY_FALSE = "false";

    public static final String QUESTION_ACCURACY_HALFOF = "halfOf";

    public static final String QUESTION_TRUE_OR_FALSE_Y = "Y";

    public static final String QUESTION_TRUE_OR_FALSE_N = "N";

    public static final int COVER_QUESTION_BANK = 1;

    public static final int UN_COVER_QUESTION_BANK = 0;

    public static final String CATEGORY_TEAM = "team";

    /**
     * 已回答
     */
    public static final String ASK_CIRCLE_ANSWER_DID = "2";

    /**
     * 未回答
     */
    public static final String ASK_CIRCLE_ANSWER_ALREADY = "1";

    public static final String QUESTION_SINGLE = "single";

    public static final String QUESTION_MULTIPLE = "multiple";

    public static final String QUESTION_TRUEORFALSE = "trueOrFalse";


    public static final String PARAMETER_ALL = "all";

    public static final String PARAMETER_PART = "part";

    public static final String PREVIEW_BEFORE = "before";

    public static final String PREVIEW_NOW = "now";

    public static final String PREVIEW_ALL = "before,now";



    /**----------微信登录token设置----------*/
    public final static String USER_PREFIX = "userToken$";

    public final static Long TOKEN_VALIDITY_TIME = 3600L * 24 * 7;
    /**
     * 微信登录用户是否绑定信息标识 0 (绑定)　1(未绑定)
     */
    public final static String WX_INFO_BINDIND_0 = "0";
    public final static String WX_INFO_BINDIND_1 = "1";

    /**
     * 微信端学生类型
     */
    public final static String TOKEN_STUDENT ="student";
    /**
     * 登录认证的教师类型
     */
    public final static String TOKEN_TEACHER ="teacher";

    /**
     * 学生信息从redis 取出通过从[oracle 数据库取出保存进入(redis) hash]
     */
    public static final String STUDENT_ADO = "studentsData$";

    public static final String QUESTION_ID = "questionId$";
    public static final String QUESTION_CHAPTER = "questionChapter$";
    public static final String QUESTIONS_VERIFY = "questionsVerify";
}