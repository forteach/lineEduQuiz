package com.project.quiz.domain;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

/**
 * @Description:
 * @author: liu zhenming
 * @version: V1.0
 * @date: 2018/11/15  16:57
 */
@Data
public abstract class BaseEntity implements Serializable {

    @Id
    @ApiModelProperty(value = "id", name = "id", example = "5c06d23sz8737b1dc8068da8", notes = "传入id为修改  不传id为新增")
    protected String id;

    @ApiModelProperty(value = "更新时间", name = "uDate", example = "1543950907881", notes = "时间戳")
    protected String uDate = StrUtil.isBlank(this.getUDate()) ? DateUtil.now() : this.getUDate();

}