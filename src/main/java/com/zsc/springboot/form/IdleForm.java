package com.zsc.springboot.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 *
 *
 *@Author：黄港团
 *@Since：2021/2/25 20:25
 */
@Setter
@Getter
public class IdleForm {
    @NotEmpty(message = "用户Id不能为空")
    private String userId;
    @NotEmpty(message = "标题不能为空")
    private String title;
    @NotEmpty(message = "描述不能为空")
    private String describe;
    private String[] tab;
    @NotNull(message = "价格不能为空")
    private BigDecimal price;
    @NotNull(message = "数量不能为空")
    private Integer num;
    @NotEmpty(message = "联系方式不能为空")
    private String phone;
    @NotEmpty(message = "状态不能为空")
    private String state;
    @NotEmpty(message = "物品图片不能为空")
    private String[] photo;
}
