package com.zsc.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author HGT
 * @since 2021-03-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ExceptionLog对象", description="")
public class ExceptionLog implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键Id")
    @TableId(value = "exc_id", type = IdType.ID_WORKER)
    private Long excId;

    @ApiModelProperty(value = "请求参数")
    private String excRequParam;

    @ApiModelProperty(value = "异常名称")
    private String excName;

    @ApiModelProperty(value = "异常信息")
    private String excMessage;

    @ApiModelProperty(value = "操作员Id")
    private String operUserId;

    @ApiModelProperty(value = "操作方法")
    private String operMethod;

    @ApiModelProperty(value = "请求URI")
    private String operUri;

    @ApiModelProperty(value = "请求IP")
    private String operIp;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "逻辑删除")
    @TableLogic
    private Integer deleted;


}
