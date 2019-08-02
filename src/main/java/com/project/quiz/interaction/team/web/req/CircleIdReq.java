package com.project.quiz.interaction.team.web.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

import static com.project.quiz.common.Dic.ASK_GROUP;

/**
 * @Description:
 * @author: liu zhenming
 * @version: V1.0
 * @date: 2019/1/28  18:50
 */
@Data
@ApiModel(value = "课堂id", description = "{查询课堂相关信息}")
public class CircleIdReq implements Serializable {

    /**
     * 课堂圈子id
     */
    @ApiModelProperty(value = "课堂圈子id/课程id", name = "circleId", required = true)
    private String circleId;

    @ApiModelProperty(value = "班级id", name = "classId", dataType = "string", notes = "如果是课程必传")
    private String classId;

    public String getGroupKey() {
        return circleId.concat(ASK_GROUP).concat(classId);
    }
}
