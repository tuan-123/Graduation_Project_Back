package com.zsc.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsc.springboot.entity.Idle;
import com.zsc.springboot.form.IdleForm;
import com.zsc.springboot.mapper.IdleMapper;
import com.zsc.springboot.mapper.UserMapper;
import com.zsc.springboot.service.CommentService;
import com.zsc.springboot.service.IdleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsc.springboot.service.SchoolService;
import com.zsc.springboot.service.UserService;
import com.zsc.springboot.util.ArrayUtil;
import com.zsc.springboot.util.SnowflakeIdWorker;
import com.zsc.springboot.vo.IdleBriefListVo;
import com.zsc.springboot.vo.IdleBriefVo;
import com.zsc.springboot.vo.IdleDetailVo;
import com.zsc.springboot.vo.admin.AdminAskVo;
import com.zsc.springboot.vo.admin.AdminIdleBriefVo;
import com.zsc.springboot.vo.admin.AdminIdleListVo;
import com.zsc.springboot.vo.admin.AdminIdleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author HGT
 * @since 2021-02-26
 */
@Service
public class IdleServiceImpl extends ServiceImpl<IdleMapper, Idle> implements IdleService {

    @Autowired
    private IdleMapper idleMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private SchoolService schoolService;

    @Transactional
    @Override
    public Integer idleIssue(IdleForm idleForm) {
        Idle idle = new Idle();
        idle.setId(SnowflakeIdWorker.generateId());
        idle.setUserId(idleForm.getUserId());
        idle.setSchoolId(userService.getUserSchoolIdByUserId(idleForm.getUserId()));
        idle.setTitle(idleForm.getTitle());
        idle.setDescr(idleForm.getDescribe());
        idle.setTab(ArrayUtil.arrayToString(idleForm.getTab()));
        idle.setPrice(idleForm.getPrice());
        idle.setNum(idleForm.getNum());
        idle.setPhone(idleForm.getPhone());
        idle.setState(idleForm.getState());
        idle.setPhoto(ArrayUtil.arrayToString(idleForm.getPhoto()));
        return idleMapper.insert(idle);
    }

    @Override
    public IdleDetailVo getIdleById(Long id) {
        Idle idle = idleMapper.getIdleById(id);
        if(idle == null)
            return null;
        IdleDetailVo idleDetailVo = new IdleDetailVo();
        idleDetailVo.setTitle(idle.getTitle());
        idleDetailVo.setDescribe(idle.getDescr());
        idleDetailVo.setPrice(idle.getPrice());
        idleDetailVo.setPhone(idle.getPhone());
        idleDetailVo.setTab(ArrayUtil.stringToObject(idle.getTab()));
        idleDetailVo.setState(idle.getState());
        idleDetailVo.setPhoto(ArrayUtil.stringToObject(idle.getPhoto()));
        idleDetailVo.setCreateTime(idle.getCreateTime());
        idleDetailVo.setComments(commentService.getCommentsByParentId(id));
        return idleDetailVo;
    }

    @Override
    public IdleDetailVo getUpIdleById(Long id) {
        Idle idle = idleMapper.getUpIdleById(id);
        if(idle == null)
            return null;
        IdleDetailVo idleDetailVo = new IdleDetailVo();
        idleDetailVo.setTitle(idle.getTitle());
        idleDetailVo.setDescribe(idle.getDescr());
        idleDetailVo.setPrice(idle.getPrice());
        idleDetailVo.setPhone(idle.getPhone());
        idleDetailVo.setTab(ArrayUtil.stringToObject(idle.getTab()));
        idleDetailVo.setState(idle.getState());
        idleDetailVo.setPhoto(ArrayUtil.stringToObject(idle.getPhoto()));
        idleDetailVo.setCreateTime(idle.getCreateTime());
        idleDetailVo.setComments(commentService.getCommentsByParentId(id));
        return idleDetailVo;
    }

    @Override
    public IdleBriefListVo getIdleBriefList(String userId,String query,long currentPage,long pageSize) {
        Integer schoolId = userService.getUserSchoolIdByUserId(userId);
        QueryWrapper qw = new QueryWrapper();
        qw.eq("school_id",schoolId);
        qw.eq("state",'1');
        Page<Idle> iPage = new Page<>(currentPage,pageSize);
        Page<Idle> idlePage;
        if(!query.isEmpty())
            qw.like("title",query);
        qw.orderByDesc("create_time");
        idlePage = (Page<Idle>) idleMapper.selectPage(iPage, qw);
        List<Idle> idles = idlePage.getRecords();
        List<IdleBriefVo> idleBriefVoList = new ArrayList<>();
        if(idles != null && idles.size() > 0) {
            IdleBriefVo idleBriefVo;
            for(Idle idle : idles){
                idleBriefVo = new IdleBriefVo();
                idleBriefVo.setId(idle.getId());
                idleBriefVo.setUserId(idle.getUserId());
                idleBriefVo.setTitle(idle.getTitle());
                idleBriefVo.setTab(ArrayUtil.stringToObject(idle.getTab()));
                idleBriefVo.setDescribe(idle.getDescr());
                idleBriefVo.setPrice(idle.getPrice());
                idleBriefVo.setNum(idle.getNum());
                idleBriefVo.setState(idle.getState());
                idleBriefVo.setCreateTime(idle.getCreateTime());
                idleBriefVo.setPhoto(ArrayUtil.stringToObject(idle.getPhoto())[0].toString());
                idleBriefVoList.add(idleBriefVo);
            }
        }
        IdleBriefListVo idleBriefListVo = new IdleBriefListVo();
        idleBriefListVo.setCurrentPage(idlePage.getCurrent());
        idleBriefListVo.setTotal(idlePage.getTotal());
        idleBriefListVo.setIdleBriefList(idleBriefVoList);
        return idleBriefListVo;
    }

    @Override
    public IdleBriefListVo getIdleBriefListByUserId(String userId, String query, long currentPage, long pageSize) {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("user_id",userId);
        Page<Idle> iPage = new Page<>(currentPage,pageSize);
        Page<Idle> idlePage;
        if(!query.isEmpty())
            qw.like("title",query);
        qw.orderByDesc("create_time");
        idlePage = (Page<Idle>) idleMapper.selectPage(iPage, qw);
        List<Idle> idles = idlePage.getRecords();
        List<IdleBriefVo> idleBriefVoList = new ArrayList<>();
        if(idles != null && idles.size() > 0) {
            IdleBriefVo idleBriefVo;
            for(Idle idle : idles){
                idleBriefVo = new IdleBriefVo();
                idleBriefVo.setId(idle.getId());
                idleBriefVo.setUserId(userId);
                idleBriefVo.setTitle(idle.getTitle());
                idleBriefVo.setTab(ArrayUtil.stringToObject(idle.getTab()));
                idleBriefVo.setDescribe(idle.getDescr());
                idleBriefVo.setPrice(idle.getPrice());
                idleBriefVo.setNum(idle.getNum());
                idleBriefVo.setState(idle.getState());
                idleBriefVo.setCreateTime(idle.getCreateTime());
                idleBriefVo.setPhoto(ArrayUtil.stringToObject(idle.getPhoto())[0].toString());
                idleBriefVoList.add(idleBriefVo);
            }
        }
        IdleBriefListVo idleBriefListVo = new IdleBriefListVo();
        idleBriefListVo.setCurrentPage(idlePage.getCurrent());
        idleBriefListVo.setTotal(idlePage.getTotal());
        idleBriefListVo.setPages(idlePage.getPages());
        idleBriefListVo.setIdleBriefList(idleBriefVoList);
        return idleBriefListVo;
    }

    @Override
    public IdleBriefListVo loadingMoreIdleBriefListByUserId(String userId, String query, long currentPage, long pageSize, Date date) {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("user_id",userId);
        Page<Idle> iPage = new Page<>(currentPage,pageSize);
        Page<Idle> idlePage;
        if(!query.isEmpty())
            qw.like("title",query);
        qw.orderByDesc("create_time");
        qw.le("create_time",date);
        idlePage = (Page<Idle>) idleMapper.selectPage(iPage, qw);
        List<Idle> idles = idlePage.getRecords();
        List<IdleBriefVo> idleBriefVoList = new ArrayList<>();
        if(idles != null && idles.size() > 0){
            IdleBriefVo idleBriefVo;
            for(Idle idle : idles){
                idleBriefVo = new IdleBriefVo();
                idleBriefVo.setId(idle.getId());
                idleBriefVo.setUserId(userId);
                idleBriefVo.setTitle(idle.getTitle());
                idleBriefVo.setTab(ArrayUtil.stringToObject(idle.getTab()));
                idleBriefVo.setDescribe(idle.getDescr());
                idleBriefVo.setPrice(idle.getPrice());
                idleBriefVo.setNum(idle.getNum());
                idleBriefVo.setState(idle.getState());
                idleBriefVo.setCreateTime(idle.getCreateTime());
                idleBriefVo.setPhoto(ArrayUtil.stringToObject(idle.getPhoto())[0].toString());
                idleBriefVoList.add(idleBriefVo);
            }
        }

        IdleBriefListVo idleBriefListVo = new IdleBriefListVo();
        idleBriefListVo.setCurrentPage(idlePage.getCurrent());
        idleBriefListVo.setTotal(idlePage.getTotal());
        idleBriefListVo.setPages(idlePage.getPages());
        idleBriefListVo.setIdleBriefList(idleBriefVoList);
        return idleBriefListVo;
    }

    // 不包含下架物品
    @Override
    public IdleBriefListVo loadingMoreIdleBriefList(String userId, String query, long currentPage, long pageSize, Date date) {
        Integer schoolId = userService.getUserSchoolIdByUserId(userId);
        QueryWrapper qw = new QueryWrapper();
        qw.eq("school_id",schoolId);
        qw.eq("state",'1');
        Page<Idle> iPage = new Page<>(currentPage,pageSize);
        Page<Idle> idlePage;
        if(!query.isEmpty())
            qw.like("title",query);
        qw.orderByDesc("create_time");
        qw.le("create_time",date);
        idlePage = (Page<Idle>) idleMapper.selectPage(iPage, qw);
        List<Idle> idles = idlePage.getRecords();
        List<IdleBriefVo> idleBriefVoList = new ArrayList<>();
        if(idles != null && idles.size() > 0) {
            IdleBriefVo idleBriefVo;
            for(Idle idle : idles){
                idleBriefVo = new IdleBriefVo();
                idleBriefVo.setId(idle.getId());
                idleBriefVo.setUserId(idle.getUserId());
                idleBriefVo.setTitle(idle.getTitle());
                idleBriefVo.setTab(ArrayUtil.stringToObject(idle.getTab()));
                idleBriefVo.setDescribe(idle.getDescr());
                idleBriefVo.setPrice(idle.getPrice());
                idleBriefVo.setNum(idle.getNum());
                idleBriefVo.setState(idle.getState());
                idleBriefVo.setCreateTime(idle.getCreateTime());
                idleBriefVo.setPhoto(ArrayUtil.stringToObject(idle.getPhoto())[0].toString());
                idleBriefVoList.add(idleBriefVo);
            }
        }

        IdleBriefListVo idleBriefListVo = new IdleBriefListVo();
        idleBriefListVo.setCurrentPage(idlePage.getCurrent());
        idleBriefListVo.setTotal(idlePage.getTotal());
        idleBriefListVo.setIdleBriefList(idleBriefVoList);
        return idleBriefListVo;
    }

    @Transactional
    @Override
    public Integer downIdleById(Long id) {
        return idleMapper.downIdleById(id);
    }

    @Transactional
    @Override
    public Integer deleteIdleById(Long id) {
        return idleMapper.deletedIdleById(id);
    }

    @Transactional
    @Override
    public Integer upIdleById(Long id){
        return idleMapper.upIdleById(id);
    }

    @Override
    public long getCount() {
        return idleMapper.getCount();
    }

    @Override
    public AdminIdleVo adminGetIdleById(Long id) {
        Idle idle = idleMapper.selectById(id);
        AdminIdleVo adminIdleVo = null;
        if(idle != null){
            adminIdleVo = new AdminIdleVo();
            adminIdleVo.setId(idle.getId());
            adminIdleVo.setUserId(idle.getUserId());
            adminIdleVo.setSchool(schoolService.getSchoolNameBySchoolId(idle.getSchoolId()));
            adminIdleVo.setTitle(idle.getTitle());
            adminIdleVo.setDescribe(idle.getDescr());
            adminIdleVo.setTab(ArrayUtil.stringToObject(idle.getTab()));
            adminIdleVo.setPrice(idle.getPrice());
            adminIdleVo.setNum(idle.getNum());
            adminIdleVo.setPhone(idle.getPhone());
            adminIdleVo.setState(idle.getState());
            adminIdleVo.setPhoto(ArrayUtil.stringToObject(idle.getPhoto()));
            adminIdleVo.setCreateTime(idle.getCreateTime());
            adminIdleVo.setComments(commentService.getCommentsByParentId(id));
        }
        return adminIdleVo;
    }

    @Override
    public AdminIdleListVo adminGetIdleList(String query, long pageNum, long pageSize) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if(!query.isEmpty())
            queryWrapper.like("user_id",query);
        Page<Idle> iPage = new Page<>(pageNum,pageSize);
        Page<Idle> idlePage;
        idlePage = (Page<Idle>) idleMapper.selectPage(iPage,queryWrapper);
        List<Idle> idles = idlePage.getRecords();
        List<AdminIdleBriefVo> adminIdleBriefVoList = new ArrayList<>();
        if(idles!= null){
            AdminIdleBriefVo adminIdleBriefVo;
            for(Idle idle : idles){
                adminIdleBriefVo = new AdminIdleBriefVo();
                adminIdleBriefVo.setId(idle.getId());
                adminIdleBriefVo.setUserId(idle.getUserId());
                adminIdleBriefVo.setTitle(idle.getTitle());
                adminIdleBriefVo.setPrice(idle.getPrice());
                adminIdleBriefVo.setNum(idle.getNum());
                adminIdleBriefVo.setTab(ArrayUtil.stringToObject(idle.getTab()));
                adminIdleBriefVo.setPhone(idle.getPhone());
                adminIdleBriefVo.setCreateTime(idle.getCreateTime());
                adminIdleBriefVoList.add(adminIdleBriefVo);
            }
        }
        AdminIdleListVo adminIdleListVo = new AdminIdleListVo();
        adminIdleListVo.setCurrentPage(idlePage.getCurrent());
        adminIdleListVo.setPageSize(idlePage.getPages());
        adminIdleListVo.setTotal(idlePage.getTotal());
        adminIdleListVo.setAdminIdleBriefVoList(adminIdleBriefVoList);
        return adminIdleListVo;
    }


}