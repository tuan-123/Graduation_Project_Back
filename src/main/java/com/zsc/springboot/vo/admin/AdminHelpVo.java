package com.zsc.springboot.vo.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 *
 *@Author：黄港团
 *@Since：2021/3/31 19:15
 */
@Setter
@Getter
public class AdminHelpVo {
    private Long id;
    private String userId;
    private String school;
    private String helpArticle;
    private String helpTime;
    private String helpPlace;
    private String helpTo;
    private BigDecimal helpFee;
    private String helpDescr;
    private String helpPhone;
    private Object[] helpPhoto;
    private Integer helpState;
    private String acceptUserId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:sss",timezone = "GMT+8")
    private Date acceptTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:sss",timezone = "GMT+8")
    private Date createTime;

}
