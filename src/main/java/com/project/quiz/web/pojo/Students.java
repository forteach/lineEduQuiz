package com.project.quiz.web.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 学生信息
 * @author: liu zhenming
 * @version: V1.0
 * @date: 2018/12/5  23:19
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "学生信息", description = "学生的简略信息")
public class Students {

    /**
     * 学生id
     */
    @ApiModelProperty(value = "学生id", name = "id")
    private String id;

    /**
     * 学生名字
     */
    @ApiModelProperty(value = "学生名字", name = "name")
    private String name;

    /**
     * 学生头像
     */
    @ApiModelProperty(value = "学生头像", name = "portrait")
    private String portrait;

}
