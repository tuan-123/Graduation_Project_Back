package com.zsc.springboot.service.impl;

import com.zsc.springboot.entity.Notice;
import com.zsc.springboot.mapper.NoticeMapper;
import com.zsc.springboot.service.NoticeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsc.springboot.vo.NoticeVo;
import com.zsc.springboot.vo.admin.AdminNoticeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author HGT
 * @since 2021-03-19
 */
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {

    @Autowired
    private NoticeMapper noticeMapper;


    @Override
    public Integer addNotice(String content, String address, Date startTime, Date endTime) {
        Notice notice = new Notice();
        notice.setContent(content);
        notice.setAddress(address);
        notice.setStartTime(startTime);
        notice.setEndTime(endTime);
        return noticeMapper.insert(notice);
    }

    @Override
    public AdminNoticeVo adminGetNotice() {
        return noticeMapper.adminGetNotice();
    }

    @Transactional
    @Override
    public Integer deleteNoticeById(Integer id) {
        return noticeMapper.deleteNoticeById(id);
    }

    @Override
    public Integer updateNoticeById(Integer id, String content, String address, Date startTime, Date endTime) {
        return noticeMapper.updateNoticeById(id, content, address, startTime, endTime);
    }

    @Override
    public NoticeVo getNotice() {
        return noticeMapper.getNotice();
    }
}
