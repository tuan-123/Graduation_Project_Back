package com.zsc.springboot.service;

import com.zsc.springboot.entity.Notice;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zsc.springboot.vo.NoticeVo;
import com.zsc.springboot.vo.admin.AdminNoticeVo;

import java.util.Date;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author HGT
 * @since 2021-03-19
 */
public interface NoticeService extends IService<Notice> {

    Integer addNotice(String content, String address, Date startTime,Date endTime);
    AdminNoticeVo adminGetNotice();
    Integer deleteNoticeById(Integer id);
    Integer updateNoticeById(Integer id, String content, String address, Date startTime,Date endTime);
    NoticeVo getNotice();
}
