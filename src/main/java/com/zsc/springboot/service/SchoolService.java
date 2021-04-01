package com.zsc.springboot.service;

import com.zsc.springboot.entity.School;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zsc.springboot.vo.SchoolSelectVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author HGT
 * @since 2021-02-18
 */
public interface SchoolService extends IService<School> {
    public boolean addProvince(String province);
    public boolean addSchool(String provinceName,String schoolName);
    public List<SchoolSelectVo> getAllSchoolSelect();
    public String getSchoolNameBySchoolId(Integer schoolId);
}
