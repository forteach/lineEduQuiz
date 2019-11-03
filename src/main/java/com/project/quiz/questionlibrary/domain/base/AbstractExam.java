package com.project.quiz.questionlibrary.domain.base;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonView;
import com.project.quiz.domain.BaseEntity;
import com.project.quiz.questionlibrary.domain.question.ChoiceQstOption;
import com.project.quiz.web.vo.BigQuestionView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.List;

import static com.project.quiz.common.Dic.VERIFY_STATUS_APPLY;

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
     * 课程id
     */
    @Indexed
    @ApiModelProperty(value = "章节id", name = "chapterId", required = true, dataType = "string", example = "463bcd8e5fed4a33883850c14f877271")
    protected String chapterId;

    @Indexed
    @ApiModelProperty(name = "courseId", value = "课程id", required = true, dataType = "string")
    protected String courseId;

    @ApiModelProperty(name = "chapterName", value = "章节名称", required = true, dataType = "string")
    protected String chapterName;

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

    @ApiModelProperty(name = "teacherName", value = "教师姓名", dataType = "string")
    protected String teacherName;

    /**
     * 考题类型   single  multiple trueOrFalse
     */
    @ApiModelProperty(value = "考题类型  single  multiple trueOrFalse", name = "examType", required = true, example = "single")
    protected String examType;

    /**
     * 题目题干
     */
    @ApiModelProperty(value = "题目题干", name = "choiceQstTxt", required = true, example = "1+1 = ?")
    protected String choiceQstTxt;

    /**
     * 回答答案
     */
    @JsonView(BigQuestionView.Summary.class)
    @ApiModelProperty(value = "题目答案", name = "answer", required = true, example = "A")
    protected String answer;

    @JsonView(BigQuestionView.Summary.class)
    @ApiModelProperty(value = "题目解析", name = "analysis", example = "A选项正确")
    protected String analysis;

    /**
     * 难易度id
     */
    @ApiModelProperty(value = "难易度id", name = "levelId", example = "0")
    protected String levelId;

    @ApiModelProperty(name = "courseName", value = "课程名称", dataType = "string")
    protected String courseName;


    @ApiModelProperty(name = "centerAreaId", value = "学习中心Id", dataType = "string")
    protected String centerAreaId;

    @ApiModelProperty(name = "centerName", value = "学习中心名称", dataType = "string")
    protected String centerName;

    /**
     * 选项集
     */
    @JsonView(BigQuestionView.Summary.class)
    @ApiModelProperty(value = "选项集", name = "optChildren", required = true)
    protected List<ChoiceQstOption> optChildren;

    /**
     * 是否有效 0,有效, 1 无效，2 拒绝
     */
    private String verifyStatus = StrUtil.isNotBlank(this.verifyStatus) ? this.verifyStatus : VERIFY_STATUS_APPLY;
}