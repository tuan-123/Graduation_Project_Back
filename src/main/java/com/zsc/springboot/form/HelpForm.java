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
 *@Since：2021/3/3 10:45
 */
@Setter
@Getter
public class HelpForm {
    @NotEmpty(message = "用户Id不能为空")
    private String userId;
    @NotEmpty(message = "物品不能为空")
    private String helpArticle;
    @NotEmpty(message = "时间不能为空")
    private String helpTime;
    @NotEmpty(message = "地点不能为空")
    private String helpPlace;
    private String helpTo;
    @NotNull(message = "佣金不能为空")
    private BigDecimal helpFee;
    private String helpDescr;
    @NotEmpty(message = "联系方式不能为空")
    private String helpPhone;
    private String[] helpPhoto;
}
