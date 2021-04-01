package com.zsc.springboot.mapper;

import com.zsc.springboot.entity.Ask;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author HGT
 * @since 2021-03-01
 */
@Repository
public interface AskMapper extends BaseMapper<Ask> {
    @Select("select count(id) from ask")
    long getCount();
}
