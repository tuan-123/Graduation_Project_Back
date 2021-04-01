package com.zsc.springboot.mapper;

import com.zsc.springboot.entity.Idle;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author HGT
 * @since 2021-02-26
 */
@Repository
public interface IdleMapper extends BaseMapper<Idle> {
    @Select("select user_id,title,descr,tab,price,num,phone,photo,create_time from idle where id = #{id} and deleted = 0 limit 1")
    public Idle getIdleById(Long id);

    @Select("select user_id,title,descr,tab,price,num,phone,photo,create_time from idle where id = #{id} and state = 1 and deleted = 0 limit 1")
    public Idle getUpIdleById(Long id);

    @Update("update idle set state = 0 where id = #{id} and state = 1 limit 1")
    public Integer downIdleById(Long id);

    @Update("update idle set state = 1 where id = #{id} and state = 0 limit 1")
    public Integer upIdleById(Long id);

    @Update("update idle set deleted = 1 where id = #{id} limit 1")
    public Integer deletedIdleById(Long id);

    @Select("select count(id) from idle")
    public long getCount();

}
