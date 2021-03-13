package com.zsc.springboot.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
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
 * @since 2021-03-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Help对象", description="")
public class Help implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "学校id")
    private Integer schoolId;

    @ApiModelProperty(value = "帮代物品")
    private String helpArticle;

    @ApiModelProperty(value = "帮代时间")
    private String helpTime;

    @ApiModelProperty(value = "帮代地点")
    private String helpPlace;

    @ApiModelProperty(value = "帮代送达")
    private String helpTo;

    @ApiModelProperty(value = "佣金")
    private BigDecimal helpFee;

    @ApiModelProperty(value = "描述")
    private String helpDescr;

    @ApiModelProperty(value = "联系方式")
    private String helpPhone;

    @ApiModelProperty(value = "图片路径(多张用;分隔)本次课设最多允许三张图片")
    private String helpPhoto;

    @ApiModelProperty(value = "状态(0表示未接单,1表示已接单)")
    private Integer helpState;

    @ApiModelProperty(value = "接单用户id")
    private String acceptUserId;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "逻辑删除(0表示未删除，1表示删除)")
    @TableLogic
    private Integer deleted;

    @ApiModelProperty(value = "乐观锁")
    @Version
    private Integer version;


}
