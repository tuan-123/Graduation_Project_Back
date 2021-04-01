package com.zsc.springboot.service.impl;

import com.zsc.springboot.entity.School;
import com.zsc.springboot.mapper.SchoolMapper;
import com.zsc.springboot.service.SchoolService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsc.springboot.vo.SchoolSelectVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author HGT
 * @since 2021-02-18
 */
@Service
public class SchoolServiceImpl extends ServiceImpl<SchoolMapper, School> implements SchoolService {
    @Autowired
    private SchoolMapper schoolMapper;

    @Override
    public boolean addProvince(String provinceName) {
        School school = new School();
        school.setParentId(0);
        school.setName(provinceName);
        schoolMapper.insert(school);
        return true;
    }

    @Override
    public boolean addSchool(String provinceName, String schoolName) {
        Integer id = schoolMapper.getProvinceIdByProvinceName(provinceName);
        School school = new School();
        school.setParentId(id);
        school.setName(schoolName);
        schoolMapper.insert(school);
        return true;
    }

    @Override
    public List<SchoolSelectVo> getAllSchoolSelect() {
        List<SchoolSelectVo> allSchool = schoolMapper.getAllProvince();
        for(SchoolSelectVo schoolSelectVo : allSchool){
            schoolSelectVo.setChildren(schoolMapper.getSchoolsByProvinceId(schoolSelectVo.getValue()));
        }
        return allSchool;
    }

    @Override
    public String getSchoolNameBySchoolId(Integer schoolId) {
        if(schoolId == -1)
            return "-1";
        return schoolMapper.getSchoolNameBySchoolId(schoolId);
    }
}
