package com.project.quiz.practiser.domain.base;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.quiz.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.index.Indexed;

/**
 * @author: zhangyy
 * @email: zhang10092009@hotmail.com
 * @date: 19-6-9 16:43
 * @version: 1.0
 * @description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class AbstractAnswer extends BaseEntity {

    @ApiModelProperty(name = "courseId", value = "课程id", dataType = "string")
    @Indexed
    private String courseId;

    @ApiModelProperty(value = "章节id", name = "chapterId", example = "463bcd8e5fed4a33883850c14f877271")
    @Indexed
    protected String chapterId;

    @ApiModelProperty(name = "chapterName", value = "章节名称", dataType = "string")
    private String chapterName;

    @ApiModelProperty(name = "classId", value = "班级id", dataType = "string")
    private String classId;
    /**
     * 学生id
     */
    @Indexed
    @JsonIgnore
    @ApiModelProperty(value = "学生id", name = "studentId", dataType = "string")
    private String studentId;
}
