package com.project.quiz.questionlibrary.domain.base;

import com.fasterxml.jackson.annotation.JsonView;
import com.project.quiz.domain.BaseEntity;
import com.project.quiz.web.vo.BigQuestionView;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description:
 * @author: liu zhenming
 * @version: V1.0
 * @date: 2018/11/15  0:15
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractExam extends BaseEntity {

    /**
     * 题目id
     */
    @ApiModelProperty(value = "章节id", name = "chapterId", required = true, dataType = "string", example = "463bcd8e5fed4a33883850c14f877271")
    protected String chapterId;

    @ApiModelProperty(name = "courseId", value = "课程id", required = true, dataType = "string")
    private String courseId;

    @ApiModelProperty(name = "courseName", value = "章节名称", required = true, dataType = "string")
    private String courseName;

    /**
     * 题目分数
     */
    @ApiModelProperty(value = "题目分数", name = "score", required = true, dataType = "number", example = "2.0")
    protected Double score;

    /**
     * 创作老师
     */
    @ApiModelProperty(value = "创作老师id", name = "teacherId", example = "463bcd8e5fed4a33883850c14f877271")
    protected String teacherId;

    /**
     * 考题类型   single  multiple trueOrFalse  design  bigQuestion
     */
    @ApiModelProperty(value = "考题类型  single  multiple trueOrFalse  design  bigQuestion", name = "examType", required = true, example = "single")
    protected String examType;

    /**
     * 题目题干
     */
    @ApiModelProperty(value = "题目题干", name = "choiceQstTxt", required = true, example = "1+1 = ?")
    private String choiceQstTxt;

    /**
     * 回答答案
     */
    @JsonView(BigQuestionView.Summary.class)
    @ApiModelProperty(value = "题目答案", name = "answer", required = true, example = "A")
    private String answer;

    @JsonView(BigQuestionView.Summary.class)
    @ApiModelProperty(value = "题目解析", name = "analysis", example = "A选项正确")
    private String analysis;

    /**
     * 难易度id
     */
    @ApiModelProperty(value = "难易度id", name = "levelId", example = "0")
    private String levelId;
}