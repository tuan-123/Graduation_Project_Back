package com.zsc.springboot.mapper;

import com.zsc.springboot.entity.OperLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zsc.springboot.vo.admin.OperLogVo;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author HGT
 * @since 2021-03-30
 */
@Repository
public interface OperLogMapper extends BaseMapper<OperLog> {

    @Select("select oper_id as id, oper_modul as modul, oper_type as type, oper_desc as descr, oper_requ_param as requestParam, oper_user_id as userId, oper_method as method, oper_uri as uri, oper_ip as ip, create_time as createTime from oper_log where oper_id = #{id} and deleted = 0 limit 1")
    OperLogVo getOperLogById(Long id);

}