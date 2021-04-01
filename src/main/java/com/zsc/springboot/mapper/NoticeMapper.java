package com.zsc.springboot.mapper;

import com.zsc.springboot.entity.Notice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zsc.springboot.vo.NoticeVo;
import com.zsc.springboot.vo.admin.AdminNoticeVo;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author HGT
 * @since 2021-03-19
 */
@Repository
public interface NoticeMapper extends BaseMapper<Notice> {
    @Select("select id,content,address,start_time,end_time,create_time from notice where deleted = 0 limit 1")
    AdminNoticeVo adminGetNotice();

    @Update("update notice set deleted = 1 where id = #{id} limit 1")
    Integer deleteNoticeById(Integer id);

    @Update("update notice set content = #{content},address = #{address},start_time = #{startTime},end_time = #{endTime} where id = #{id} limit 1")
    Integer updateNoticeById(Integer id, String content, String address, Date startTime,Date endTime);

    @Select("select content,address from notice where deleted = 0 and start_time <= now() and end_time >= now() limit 1")
    NoticeVo getNotice();
}
