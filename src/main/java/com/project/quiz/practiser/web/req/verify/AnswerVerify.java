package com.project.quiz.practiser.web.req.verify;

import com.project.quiz.common.DefineCode;
import com.project.quiz.common.MyAssert;
import org.springframework.stereotype.Component;

/**
 * @author: zhangyy
 * @email: zhang10092009@hotmail.com
 * @date: 19-6-4 11:18
 * @version: 1.0
 * @description:
 */
@Component
public class AnswerVerify {

//    public void verify(AbstractReq abstractReq){
//        MyAssert.isNull(abstractReq.getCourseId(), DefineCode.ERR0010, "课程不为空");
//        MyAssert.isNull(abstractReq.getClassId(), DefineCode.ERR0010, "班级不为空");
//    }

    public void verifyChapterId(final String courseId, final String chapterId) {
        MyAssert.isNull(courseId, DefineCode.ERR0010, "课程不为空");
        MyAssert.isNull(chapterId, DefineCode.ERR0010, "章节不为空");
    }

    public void verifyChapterId(final String chapterId) {
        MyAssert.isNull(chapterId, DefineCode.ERR0010, "章节不为空");
    }
}
