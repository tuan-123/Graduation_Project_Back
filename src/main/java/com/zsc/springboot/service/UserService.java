package com.zsc.springboot.service;

import com.zsc.springboot.common.ServerResponse;
import com.zsc.springboot.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zsc.springboot.form.FindPasswordForm;
import com.zsc.springboot.form.LoginForm;
import com.zsc.springboot.form.RegisterForm;
import com.zsc.springboot.vo.UserDetailInfoVo;
import com.zsc.springboot.vo.UserIndexVo;
import com.zsc.springboot.vo.UserVo;
import com.zsc.springboot.vo.admin.AdminUserListVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author HGT
 * @since 2021-02-18
 */
public interface UserService extends IService<User> {
    public ServerResponse register(RegisterForm registerForm);
    public ServerResponse login(LoginForm loginForm);
    public Integer checkLogin(String userId,String password);
    public Integer getRoleById(String userId);
    public UserVo getUserByUserId(String userId);
    public String getPasswordByUserId(String userId);
    public UserIndexVo getUserNameAndHImg(String userId);
    public UserDetailInfoVo getUserDetailInfo(String userId);
    public Integer updateNickName(String userId, String nickName);
    public Integer updateSchoolNum(String userId, String schoolNum);
    public Integer updateSchool(String userId, Integer schoolId);
    public Integer updateImg(String userId,String imgUrl);
    public Integer updatePassword(String userId, String oldPsd, String newPsd);
    public Integer checkByUserIdAndEmail(String userId,String email);
    public Integer findPassword(FindPasswordForm findPasswordForm);
    Integer getUserSchoolIdByUserId(String userId);
    Integer addFaceLogin(String userId,String faceToken);
    Integer removeFaceLogin(String userId);
    String getFaceTokenByUserId(String userId);

    ServerResponse adminLogin(String userId,String pwd);
    AdminUserListVo getUserList(String query, long currentPage, long pageSize);
    Integer changeStateById(String uerId,Integer state);
    Integer deleteUserById(String userId);
    Integer addUser(String userId,String pwd,String email,Integer role);
    Integer unBundlingSchoolById(String userId);
    Integer unBundlingSchoolNumById(String userId);
    Integer updateEmail(String userId,String email);



}
