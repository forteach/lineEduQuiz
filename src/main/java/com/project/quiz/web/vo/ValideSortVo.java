package com.project.quiz.web.vo;

import com.project.quiz.common.DefineCode;
import com.project.quiz.common.MyAssert;

/**
 * @author: zhangyy
 * @email: zhang10092009@hotmail.com
 * @date: 19-7-31 15:20
 * @version: 1.0
 * @description:
 */
public class ValideSortVo {

    public static void valideSort(Integer page, Integer size) {
        MyAssert.blank(String.valueOf(page), DefineCode.ERR0010, "页码参数不为空");
        MyAssert.blank(String.valueOf(size), DefineCode.ERR0010, "每页条数不为空");
        MyAssert.gt(page, 0, DefineCode.ERR0010, "页码参数不正确");
        MyAssert.gt(size, 0, DefineCode.ERR0010, "每页显示条数不正确");
    }
}
