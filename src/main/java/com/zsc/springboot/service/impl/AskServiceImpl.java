package com.zsc.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsc.springboot.entity.Ask;
import com.zsc.springboot.mapper.AskMapper;
import com.zsc.springboot.service.AskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsc.springboot.service.CommentService;
import com.zsc.springboot.service.SchoolService;
import com.zsc.springboot.service.UserService;
import com.zsc.springboot.util.ArrayUtil;
import com.zsc.springboot.util.SnowflakeIdWorker;
import com.zsc.springboot.vo.AskListVo;
import com.zsc.springboot.vo.AskVo;
import com.zsc.springboot.vo.UserIndexVo;
import com.zsc.springboot.vo.admin.AdminAskBriefVo;
import com.zsc.springboot.vo.admin.AdminAskListVo;
import com.zsc.springboot.vo.admin.AdminAskVo;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @since 2021-03-01
 */
@Service
public class AskServiceImpl extends ServiceImpl<AskMapper, Ask> implements AskService {

    @Autowired
    private AskMapper askMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private SchoolService schoolService;

    @Transactional
    @Override
    public Integer askIssue(String userId, String content, String[] photos) {
        Ask ask = new Ask();
        ask.setId(SnowflakeIdWorker.generateId());
        ask.setUserId(userId);
        ask.setSchoolId(userService.getUserSchoolIdByUserId(userId));
        ask.setContent(content);
        ask.setPhoto(ArrayUtil.arrayToString(photos));
        ask.setHasResolve(0);
        return askMapper.insert(ask);
    }

    @Override
    public AskListVo getAskListByUserId(String userId, String query, long currentPage, long pageSize) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id",userId);
        Page<Ask> iPage = new Page<>(currentPage,pageSize);
        Page<Ask> askPage;
        if(!query.isEmpty())
            queryWrapper.like("content",query);
        queryWrapper.orderByDesc("create_time");
        askPage = (Page<Ask>) askMapper.selectPage(iPage,queryWrapper);
        List<Ask> asks = askPage.getRecords();
        List<AskVo> askVoList = new ArrayList<>();
        if(asks != null && asks.size() > 0) {
            AskVo askVo;
            UserIndexVo userIndexVo = userService.getUserNameAndHImg(userId);
            for(Ask ask : asks){
                askVo = new AskVo();
                askVo.setId(ask.getId());
                askVo.setUserId(userId);
                askVo.setUserImg(userIndexVo.getImage());
                askVo.setUserName(userIndexVo.getNickName());
                askVo.setContent(ask.getContent());
                askVo.setPhotos(ArrayUtil.stringToObject(ask.getPhoto()));
                askVo.setHasResolve(ask.getHasResolve());
                askVo.setCreateTime(ask.getCreateTime());
                askVo.setCommentVoList(commentService.getCommentsByParentId(ask.getId()));
                askVoList.add(askVo);
            }

        }
        AskListVo askListVo = new AskListVo();
        askListVo.setCurrentPage(askPage.getCurrent());
        askListVo.setPages(askPage.getPages());
        askListVo.setTotal(askPage.getTotal());
        askListVo.setAskVoList(askVoList);
        return askListVo;
    }

    @Override
    public AskListVo getAskList(String userId, String query, long currentPage, long pageSize) {
        Integer schoolId = userService.getUserSchoolIdByUserId(userId);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("school_id",schoolId);
        Page<Ask> iPage = new Page<>(currentPage,pageSize);
        Page<Ask> askPage;
        if(!query.isEmpty())
            queryWrapper.like("content",query);
        queryWrapper.orderByDesc("create_time");
        askPage = (Page<Ask>) askMapper.selectPage(iPage,queryWrapper);
        List<Ask> asks = askPage.getRecords();
        List<AskVo> askVoList = new ArrayList<>();
        if(asks != null && asks.size() > 0){
            AskVo askVo;
            UserIndexVo userIndexVo;
            for(Ask ask : asks){
                askVo = new AskVo();
                askVo.setId(ask.getId());
                askVo.setUserId(ask.getUserId());
                askVo.setContent(ask.getContent());
                userIndexVo = userService.getUserNameAndHImg(ask.getUserId());
                askVo.setUserImg(userIndexVo.getImage());
                askVo.setUserName(userIndexVo.getNickName());
                askVo.setPhotos(ArrayUtil.stringToObject(ask.getPhoto()));
                askVo.setHasResolve(ask.getHasResolve());
                askVo.setCreateTime(ask.getCreateTime());
                askVo.setCommentVoList(commentService.getCommentsByParentId(ask.getId()));
                askVoList.add(askVo);
            }
        }

        AskListVo askListVo = new AskListVo();
        askListVo.setCurrentPage(askPage.getCurrent());
        askListVo.setTotal(askPage.getTotal());
        askListVo.setPages(askPage.getPages());
        askListVo.setAskVoList(askVoList);
        return askListVo;
    }

    @Override
    public Integer deleteAskById(Long id) {
        return askMapper.deleteById(id);
    }

    @Override
    public AskListVo loadingMoreAskListByUserId(String userId, String query, long currentPage, long pageSize, Date date) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id",userId);
        Page<Ask> iPage = new Page<>(currentPage,pageSize);
        Page<Ask> askPage;
        if(!query.isEmpty())
            queryWrapper.like("content",query);
        queryWrapper.orderByDesc("create_time");
        queryWrapper.le("create_time",date);
        askPage = (Page<Ask>) askMapper.selectPage(iPage,queryWrapper);
        List<Ask> asks = askPage.getRecords();
        List<AskVo> askVoList = new ArrayList<>();
        if(asks != null && asks.size() > 0) {
            AskVo askVo;
            UserIndexVo userIndexVo = userService.getUserNameAndHImg(userId);
            for(Ask ask : asks){
                askVo = new AskVo();
                askVo.setId(ask.getId());
                askVo.setUserId(userId);
                askVo.setUserImg(userIndexVo.getImage());
                askVo.setUserName(userIndexVo.getNickName());
                askVo.setContent(ask.getContent());
                askVo.setPhotos(ArrayUtil.stringToObject(ask.getPhoto()));
                askVo.setHasResolve(ask.getHasResolve());
                askVo.setCreateTime(ask.getCreateTime());
                askVo.setCommentVoList(commentService.getCommentsByParentId(ask.getId()));
                askVoList.add(askVo);
            }

        }
        AskListVo askListVo = new AskListVo();
        askListVo.setCurrentPage(askPage.getCurrent());
        askListVo.setPages(askPage.getPages());
        askListVo.setTotal(askPage.getTotal());
        askListVo.setAskVoList(askVoList);
        return askListVo;
    }

    @Override
    public AskListVo loadingMoreAskList(String userId, String query, long currentPage, long pageSize, Date date) {
        Integer schoolId = userService.getUserSchoolIdByUserId(userId);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("school_id",schoolId);
        Page<Ask> iPage = new Page<>(currentPage,pageSize);
        Page<Ask> askPage;
        if(!query.isEmpty())
            queryWrapper.like("content",query);
        queryWrapper.orderByDesc("create_time");
        queryWrapper.le("create_time",date);
        askPage = (Page<Ask>) askMapper.selectPage(iPage,queryWrapper);
        List<Ask> asks = askPage.getRecords();
        List<AskVo> askVoList = new ArrayList<>();
        if(asks != null && asks.size() > 0) {
            AskVo askVo;
            UserIndexVo userIndexVo;
            for(Ask ask : asks){
                askVo = new AskVo();
                askVo.setId(ask.getId());
                askVo.setUserId(ask.getUserId());
                askVo.setContent(ask.getContent());
                userIndexVo = userService.getUserNameAndHImg(ask.getUserId());
                askVo.setUserImg(userIndexVo.getImage());
                askVo.setUserName(userIndexVo.getNickName());
                askVo.setPhotos(ArrayUtil.stringToObject(ask.getPhoto()));
                askVo.setHasResolve(ask.getHasResolve());
                askVo.setCreateTime(ask.getCreateTime());
                askVo.setCommentVoList(commentService.getCommentsByParentId(ask.getId()));
                askVoList.add(askVo);
            }
        }
        AskListVo askListVo = new AskListVo();
        askListVo.setCurrentPage(askPage.getCurrent());
        askListVo.setTotal(askPage.getTotal());
        askListVo.setPages(askPage.getPages());
        askListVo.setAskVoList(askVoList);
        return askListVo;
    }

    @Override
    public long getCount() {
        return askMapper.getCount();
    }

    @Override
    public AdminAskListVo adminGetAskList(String query, long currentPage, long pageSize) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if(!query.isEmpty())
            queryWrapper.like("user_id",query);
        Page<Ask> iPage = new Page<>(currentPage,pageSize);
        Page<Ask> askPage;
        askPage = (Page<Ask>) askMapper.selectPage(iPage,queryWrapper);
        List<Ask> asks = askPage.getRecords();
        List<AdminAskBriefVo> adminAskVoList = new ArrayList<>();
        if(asks != null && asks.size() > 0){
            AdminAskBriefVo adminAskVo;
            for(Ask ask : asks){
                adminAskVo = new AdminAskBriefVo();
                adminAskVo.setId(ask.getId());
                adminAskVo.setUserId(ask.getUserId());
                adminAskVo.setContent(ask.getContent());
                adminAskVo.setCreateTime(ask.getCreateTime());
                adminAskVoList.add(adminAskVo);
            }
        }
        AdminAskListVo adminAskListVo = new AdminAskListVo();
        adminAskListVo.setCurrentPage(askPage.getCurrent());
        adminAskListVo.setPages(askPage.getPages());
        adminAskListVo.setTotal(askPage.getTotal());
        adminAskListVo.setAskVoList(adminAskVoList);
        return adminAskListVo;
    }

    @Override
    public AdminAskVo getAskByAskId(Long id) {
        Ask ask = askMapper.selectById(id);
        AdminAskVo adminAskVo = null;
        if(ask != null){
            adminAskVo = new AdminAskVo();
            adminAskVo.setId(ask.getId());
            adminAskVo.setUserId(ask.getUserId());
            adminAskVo.setSchool(schoolService.getSchoolNameBySchoolId(ask.getSchoolId()));
            adminAskVo.setContent(ask.getContent());
            adminAskVo.setPhotos(ArrayUtil.stringToObject(ask.getPhoto()));
            adminAskVo.setCreateTime(ask.getCreateTime());
            adminAskVo.setCommentVoList(commentService.getCommentsByParentId(id));
        }
        return adminAskVo;
    }
}
