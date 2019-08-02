package com.project.quiz.interaction.execute.service.Key;

/**
 * @Description:
 * @author: liu zhenming
 * @version: V1.0
 * @date: 2018/11/9 11:03
 */
public class ClassRoomKey {

    /**
     * 当前课堂当前互动名称
     */
    public static final String CLASSROOM_NOW_INTERACT= "NowInteract";

    /**
     * 课堂相关信息ID-Redis的编码前缀
     */
    public static final String CLASS_ROOM_QR_CODE_PREFIX = "RoomMember";

    /**
     * 老师创建临时课堂前缀
     */
    public static final String CLASSROOM_TEACHER = "RoomTeacher";

    /**
     * 老师创建临时课堂前缀
     */
    public static final String CLASSROOM_CHAPTER = "RoomChapter";

    /**
     * 老师创建临时课堂前缀
     */
    public static final String OPEN_CLASSROOM = "OpenRoom";


    /**
     * 互动方式为加入学生
     */
    public static final String CLASSROOM_JOIN_QUESTIONS_ID = "JoinStu";

    /**
     * 设置当前活动KEY
     *
     * @return
     */
    public static String setInteractionType(final String circleId) {
        return circleId.concat(CLASSROOM_NOW_INTERACT);
    }

    /**
     * 课堂所有学生
     * @param circleId
     * @return
     */
    public static String getInteractiveIdQra(String circleId){
        return circleId.concat(ClassRoomKey.CLASS_ROOM_QR_CODE_PREFIX);
    }


    /**
     * 课堂的上课教师
     * @param circleId
     * @return
     */
    public static String getRoomTeacherKey(String circleId){
        return circleId.concat(ClassRoomKey.CLASSROOM_TEACHER);
    }

    /**
     * 课堂的授课章节
     * @param circleId
     * @return
     */
    public static String getRoomChapterKey(String circleId){
        return circleId.concat(ClassRoomKey.CLASSROOM_CHAPTER);
    }

}
