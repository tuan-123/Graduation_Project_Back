package com.zsc.springboot.mapper;

import com.zsc.springboot.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zsc.springboot.vo.UserDetailInfoVo;
import com.zsc.springboot.vo.UserIndexVo;
import com.zsc.springboot.vo.UserVo;
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
 * @since 2021-02-18
 */
@Repository
public interface UserMapper extends BaseMapper<User> {
    @Select("select u.phone,u.nick_name,s.name as school, u.email,u.role,u.account,u.state,u.image,u.brief_introduction,u.face_login,u.last_login_time,u.create_time from user u,school s where u.deleted = 0 and s.deleted = 0 and u.phone = #{phone} and password = #{password} and u.school_id = s.id")
    UserVo login(String phone, String password);

    @Select("select count('phone') from user where phone = #{userId} and password = #{password} and deleted = 0 limit 1")
    Integer checkLogin(String userId,String password);

    @Select("select role from user where phone = #{userId} limit 1")
    Integer getRoleById(String userId);

    @Select("select u.phone,u.nick_name, u.email,u.role,u.state,u.image,u.face_login,u.create_time from user u where u.deleted = 0 and u.phone = #{phone} limit 1")
    UserVo getUserByUserId(String userId);

    @Select("select password from user where phone = #{userId} limit 1")
    String getPasswordByUserId(String userId);

    @Select("select nick_name,image from user where phone = #{userId} and deleted = 0 and state = 1 limit 1")
    UserIndexVo getUserNameAndHImg(String userId);

    @Select("select u.image,u.nick_name,u.email,if(u.school_id = -1,-1,(select name from school where id = u.school_id)) as schoolName,u.school_num,u.face_login from user u where u.phone = #{userId} and u.deleted = 0 and u.state = 1 limit 1")
    UserDetailInfoVo getUserDetailInfo(String userId);

    @Select("select count(phone) from user where phone = #{userId} and email = #{email} and deleted = 0 and state = 1 limit 1")
    Integer checkByUserIdAndEmail(String userId,String email);

    @Select("select school_id from user where phone = #{userId} limit 1")
    Integer getUserSchoolIdByUserId(String userId);

    @Update("update user set state = #{state} where phone = #{userId} and deleted = 0 limit 1")
    Integer changeStateById(String userId,Integer state);

    @Update("update user set school_id = -1 where phone = #{userId} and deleted = 0 limit 1")
    Integer unBundlingSchoolById(String userId);

    @Update("update user set school_num = '-1' where phone = #{userId} and deleted = 0 limit 1")
    Integer unBundlingSchoolNumById(String userId);

    @Update("update user set email = #{email} where phone = #{userId} and deleted = 0 limit 1")
    Integer updateEmail(String userId,String email);


}
