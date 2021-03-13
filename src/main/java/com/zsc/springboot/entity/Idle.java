package com.zsc.springboot.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
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
 * @since 2021-02-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Idle对象", description="")
public class Idle implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    @ApiModelProperty(value = "发布用户id")
    private String userId;

    @ApiModelProperty(value = "发布者学校Id")
    private Integer schoolId;

    @ApiModelProperty(value = "物品标题")
    private String title;

    @ApiModelProperty(value = "物品描述")
    private String descr;

    @ApiModelProperty(value = "物品标签")
    private String tab;

    @ApiModelProperty(value = "物品价格")
    private BigDecimal price;

    @ApiModelProperty(value = "物品数量")
    private Integer num;

    @ApiModelProperty(value = "联系方式")
    private String phone;

    @ApiModelProperty(value = "1表示上架，0表示下架")
    private String state;

    @ApiModelProperty(value = "图片路径")
    private String photo;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:sss", timezone="GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "逻辑删除(0表示未删除，1表示删除)")
    @TableLogic
    private Integer deleted;


}
