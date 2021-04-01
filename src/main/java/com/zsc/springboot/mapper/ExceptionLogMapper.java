package com.zsc.springboot.mapper;

import com.zsc.springboot.entity.ExceptionLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zsc.springboot.vo.admin.ExceptionLogVo;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author HGT
 * @since 2021-03-31
 */
@Repository
public interface ExceptionLogMapper extends BaseMapper<ExceptionLog> {

    @Select("select exc_id as id, exc_requ_param as excRequParam, exc_name as excName, exc_message as excMessage, oper_user_id as operUserId, oper_method as operMethod, oper_uri as operUri, oper_ip as operIp, create_time as createTime from exception_log where exc_id = #{id} and deleted = 0 limit 1")
    ExceptionLogVo getExceptionLogById(Long id);

}
