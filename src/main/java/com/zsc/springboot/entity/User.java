package com.zsc.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
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
 * @since 2021-02-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="User对象", description="")
public class User implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "手机号作为主键")
    @TableId(value = "phone", type = IdType.ID_WORKER)
    private String phone;

    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    @ApiModelProperty(value = "用户邮箱，用于接收验证码，不能为空")
    private String email;

    @ApiModelProperty(value = "0学生，1教师，2管理员")
    private Integer role;

    @ApiModelProperty(value = "学号")
    private String schoolNum;

    @ApiModelProperty(value = "用户所在学校id")
    private Integer schoolId;

    @ApiModelProperty(value = "用户名密码，密文")
    private String password;

    @ApiModelProperty(value = "0表示封号，1表示正常使用")
    private Integer state;

    @ApiModelProperty(value = "用户头像")
    private String image;

    @ApiModelProperty(value = "是否开启人脸登录；0未开启，1开启")
    private Integer faceLogin;

    @ApiModelProperty(value = "face_token")
    private String faceToken;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "逻辑删除(0表示未删除，1表示删除)")
    @TableLogic
    private Integer deleted;

    @ApiModelProperty(value = "乐观锁")
    @Version
    private Integer version;


}
