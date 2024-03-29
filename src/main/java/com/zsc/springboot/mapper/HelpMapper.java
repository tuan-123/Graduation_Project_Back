package com.zsc.springboot.mapper;

import com.zsc.springboot.entity.Help;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author HGT
 * @since 2021-03-02
 */
@Repository
public interface HelpMapper extends BaseMapper<Help> {
    @Select("select count(id) from help")
    long getCount();

    @Select("select email from user where phone = (select user_id from help where id = #{id} limit 1) limit 1")
    String getUserEmailByHelpId(Long id);
}
