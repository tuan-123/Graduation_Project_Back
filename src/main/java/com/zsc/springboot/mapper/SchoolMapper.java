package com.zsc.springboot.mapper;

import com.zsc.springboot.entity.School;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zsc.springboot.vo.SchoolSelectVo;
import com.zsc.springboot.vo.SchoolVo;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author HGT
 * @since 2021-02-18
 */
@Repository
public interface SchoolMapper extends BaseMapper<School> {
    @Select("select id from school where parent_id = 0 and deleted = 0 and name = #{provinceName}")
    Integer getProvinceIdByProvinceName(String provinceName);//根据省份名查询id

    @Select("select id as value,name as text,parent_id from school where deleted = 0 and parent_id = 0 ")
    List<SchoolSelectVo> getAllProvince();  //查询所有省份,不包括学校

    @Select("select id as value,name as text,parent_id from school where parent_id = #{id} and deleted = 0")   // 数据库用的是name，Vo实体用的是schoolName
    List<SchoolVo> getSchoolsByProvinceId(Integer id);// 根据省份id查询该省份的所有学校

}
