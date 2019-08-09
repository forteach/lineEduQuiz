//package com.project.quiz.questionlibrary.domain.base;
//
//import com.fasterxml.jackson.annotation.JsonInclude;
//import com.project.quiz.domain.BaseEntity;
//import io.swagger.annotations.ApiModelProperty;
//import lombok.Data;
//import lombok.EqualsAndHashCode;
//import org.springframework.data.annotation.Transient;
//
///**
// * @author: zhangyy
// * @email: zhang10092009@hotmail.com
// * @date: 19-8-9 14:33
// * @version: 1.0
// * @description:
// */
//@Data
//@EqualsAndHashCode(callSuper = true)
//public class QuestionEntity<T> extends BaseEntity {
//
//    /**
//     * 难易度id
//     */
//    @ApiModelProperty(value = "难易度id", name = "levelId", example = "0")
//    private String levelId;
//    /**
//     * 下标
//     * 不保存数据库
//     */
//    @Transient
//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    @ApiModelProperty(value = "下标", name = "index", example = "")
//    protected String index;
//}
