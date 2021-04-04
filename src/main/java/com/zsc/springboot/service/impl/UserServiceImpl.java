package com.zsc.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsc.springboot.common.ServerResponse;
import com.zsc.springboot.entity.User;
import com.zsc.springboot.form.FindPasswordForm;
import com.zsc.springboot.form.LoginForm;
import com.zsc.springboot.form.RegisterForm;
import com.zsc.springboot.mapper.UserMapper;
import com.zsc.springboot.service.SchoolService;
import com.zsc.springboot.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsc.springboot.shiro.JWTToken;
import com.zsc.springboot.util.AESUtil;
import com.zsc.springboot.util.EmailCodeUtil;
import com.zsc.springboot.util.EncryptPwd;
import com.zsc.springboot.util.JWTUtil;
import com.zsc.springboot.vo.UserDetailInfoVo;
import com.zsc.springboot.vo.UserIndexVo;
import com.zsc.springboot.vo.UserVo;
import com.zsc.springboot.vo.admin.AdminUserListVo;
import com.zsc.springboot.vo.admin.AdminUserVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
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
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private EmailCodeUtil emailCodeUtil;
    @Value("${zsc.userDefaultImg}")
    private String defaultImg;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SchoolService schoolService;

    @Transactional
    @Override
    public ServerResponse register(RegisterForm registerForm) {
        User user = userMapper.selectById(registerForm.getPhone());
        if(user != null)
            return ServerResponse.fail("用户已存在");
        boolean check = emailCodeUtil.checkEmailCode(registerForm.getPhone(),registerForm.getCode());
        if(check){
            user = new User();
            user.setPhone(registerForm.getPhone());
            user.setNickName(registerForm.getPhone());
            user.setEmail(registerForm.getEmail());
            user.setPassword(EncryptPwd.encrypt(registerForm.getPassword(),registerForm.getPhone()));
            user.setState(1);
            user.setImage(defaultImg);
            user.setFaceLogin(0);
            userMapper.insert(user);
            return ServerResponse.addSuccess();
        }
        return ServerResponse.fail("注册失败");
    }

    @Override
    public ServerResponse login(LoginForm loginForm) {
        String password = EncryptPwd.encrypt(loginForm.getPassword(),loginForm.getPhone());
        /*
        UserVo userVo = userMapper.login(loginForm.getPhone(),password);
        if(userVo == null)
            return ServerResponse.fail("用户名或密码不正确");
        if(userVo.getState() == 0)
            return ServerResponse.fail("账户已锁定");
         */
        // 生成签名
        String token = JWTUtil.sign(loginForm.getPhone(),password);
        //构造JWT token
        JWTToken jwtToken = new JWTToken(token);
        //shiro安全框架：获取subject对象
        Subject subject = SecurityUtils.getSubject();
        //设置token
        try{
            subject.login(jwtToken);
        }catch (UnknownAccountException e){
            throw new UnknownAccountException("用户名或密码错误");
        }

        UserVo userVo = getUserByUserId(loginForm.getPhone());
        if(userVo.getState() == 0)
            return ServerResponse.fail("账户已锁定");
        return ServerResponse.loginSuccess(userVo,token);
    }

    @Override
    public Integer checkLogin(String userId,String password){
        return userMapper.checkLogin(userId,password);
    }

    @Override
    public Integer getRoleById(String userId){
        return userMapper.getRoleById(userId);
    }

    @Override
    public UserVo getUserByUserId(String userId) {
        return userMapper.getUserByUserId(userId);
    }

    @Override
    public String getPasswordByUserId(String userId) {
        return userMapper.getPasswordByUserId(userId);
    }

    @Override
    public UserIndexVo getUserNameAndHImg(String userId) {
        return userMapper.getUserNameAndHImg(userId);
    }

    @Override
    public UserDetailInfoVo getUserDetailInfo(String userId) {
        return userMapper.getUserDetailInfo(userId);
    }

    @Transactional
    @Override
    public Integer updateNickName(String userId, String nickName) {
        User user = userMapper.selectById(userId);
        if(user == null){
            return 0;
        }
        user.setNickName(nickName);
        return userMapper.updateById(user);
    }

    @Transactional
    @Override
    public Integer updateSchoolNum(String userId, String schoolNum) {
        User user = userMapper.selectById(userId);
        if(user == null){
            return 0;
        }
        user.setSchoolNum(schoolNum);
        return userMapper.updateById(user);
    }

    @Transactional
    @Override
    public Integer updateSchool(String userId, Integer schoolId) {
        User user = userMapper.selectById(userId);
        if(user == null){
            return 0;
        }
        user.setSchoolId(schoolId);
        return userMapper.updateById(user);
    }

    @Transactional
    @Override
    public Integer updateImg(String userId, String imgUrl) {
        User user = userMapper.selectById(userId);
        if(user == null){
            return 0;
        }
        user.setImage(imgUrl);
        return userMapper.updateById(user);
    }

    @Transactional
    @Override
    public Integer updatePassword(String userId, String oldPsd, String newPsd) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("phone",userId);
        queryWrapper.eq("password",EncryptPwd.encrypt(oldPsd,userId));
        User user = userMapper.selectOne(queryWrapper);
        if(user == null){
            return -1;
        }
        user.setPassword(EncryptPwd.encrypt(newPsd,userId));
        return userMapper.updateById(user);
    }

    @Override
    public Integer checkByUserIdAndEmail(String userId, String email) {
        return userMapper.checkByUserIdAndEmail(userId,email);
    }

    @Transactional
    @Override
    public Integer findPassword(FindPasswordForm findPasswordForm) {
        User user = userMapper.selectById(findPasswordForm.getUserId());
        if(user == null){
            return -1;
        }
        boolean checkCode = emailCodeUtil.checkEmailCode(findPasswordForm.getUserId(),findPasswordForm.getCode());
        if(checkCode){
            user.setPassword(EncryptPwd.encrypt(findPasswordForm.getPassword(),findPasswordForm.getUserId()));
            return userMapper.updateById(user);
        }else{
            return -2;
        }
    }

    @Override
    public Integer getUserSchoolIdByUserId(String userId) {
        return userMapper.getUserSchoolIdByUserId(userId);
    }

    @Transactional
    @Override
    public Integer addFaceLogin(String userId,String faceToken) {
        try {
            return userMapper.addFaceLogin(userId, AESUtil.encrypt(faceToken,userId));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Transactional
    @Override
    public Integer removeFaceLogin(String userId) {
        return userMapper.removeFaceLogin(userId);
    }

    @Override
    public String getFaceTokenByUserId(String userId) {
        return userMapper.getFaceTokeByUserId(userId);
    }

    @Override
    public ServerResponse adminLogin(String userId, String pwd) {
        String password = EncryptPwd.encrypt(pwd,userId);
        // 生成签名
        String token = JWTUtil.sign(userId,password);
        //构造JWT token
        JWTToken jwtToken = new JWTToken(token);
        //shiro安全框架：获取subject对象
        Subject subject = SecurityUtils.getSubject();
        //设置token
        try{
            subject.login(jwtToken);
        }catch (UnknownAccountException e){
            throw new UnknownAccountException("用户名或密码错误");
        }

        UserVo userVo = getUserByUserId(userId);
        if(userVo.getState() == 0)
            return ServerResponse.fail("账户已锁定");
        else if(userVo.getRole() != 2)
            return ServerResponse.fail("您不是管理员哟!");
        return ServerResponse.loginSuccess(userVo,token);
    }



    @Override
    public AdminUserListVo getUserList(String query, long currentPage, long pageSize) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.orderByDesc("create_time");
        if(!query.isEmpty())
            queryWrapper.like("phone",query);
        queryWrapper.eq("role",0);
        Page<User> IPage = new Page<>(currentPage,pageSize);
        Page<User> userPage = (Page<User>) userMapper.selectPage(IPage,queryWrapper);
        List<User> users = userPage.getRecords();
        List<AdminUserVo> adminUserVos = new ArrayList<>();
        if(users != null && users.size() > 0){
            AdminUserVo adminUserVo;
            for (User user : users){
                adminUserVo = new AdminUserVo();
                adminUserVo.setUserId(user.getPhone());
                adminUserVo.setNickName(user.getNickName());
                adminUserVo.setEmail(user.getEmail());
                adminUserVo.setImage(user.getImage());
                adminUserVo.setSchool(schoolService.getSchoolNameBySchoolId(user.getSchoolId()));
                adminUserVo.setSchoolNum(user.getSchoolNum());
                adminUserVo.setFaceLogin(user.getFaceLogin());
                adminUserVo.setState(user.getState());
                adminUserVo.setCreateTime(user.getCreateTime());
                adminUserVos.add(adminUserVo);
            }
        }
        AdminUserListVo adminUserListVo = new AdminUserListVo();
        adminUserListVo.setCurrentPage(userPage.getCurrent());
        adminUserListVo.setTotal(userPage.getTotal());
        adminUserListVo.setUserVoList(adminUserVos);
        return adminUserListVo;
    }

    @Transactional
    @Override
    public Integer changeStateById(String uerId, Integer state) {
        return userMapper.changeStateById(uerId,state);
    }

    @Override
    public Integer deleteUserById(String userId) {
        return userMapper.deleteById(userId);
    }

    @Override
    public Integer addUser(String userId, String pwd, String email, Integer role) {
        User user = new User();
        user.setPhone(userId);
        user.setPassword(EncryptPwd.encrypt(pwd,userId));
        user.setNickName(userId);
        user.setEmail(email);
        user.setRole(role);
        user.setImage(defaultImg);
        return userMapper.insert(user);
    }

    @Transactional
    @Override
    public Integer unBundlingSchoolById(String userId) {
        return userMapper.unBundlingSchoolById(userId);
    }

    @Transactional
    @Override
    public Integer unBundlingSchoolNumById(String userId) {
        return userMapper.unBundlingSchoolNumById(userId);
    }

    @Transactional
    @Override
    public Integer updateEmail(String userId, String email) {
        return userMapper.updateEmail(userId,email);
    }


}
