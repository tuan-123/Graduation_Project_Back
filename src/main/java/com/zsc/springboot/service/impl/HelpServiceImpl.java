package com.zsc.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsc.springboot.entity.Help;
import com.zsc.springboot.mapper.HelpMapper;
import com.zsc.springboot.service.CommentService;
import com.zsc.springboot.service.HelpService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsc.springboot.service.UserService;
import com.zsc.springboot.util.ArrayUtil;
import com.zsc.springboot.util.SnowflakeIdWorker;
import com.zsc.springboot.vo.HelpListVo;
import com.zsc.springboot.vo.HelpVo;
import com.zsc.springboot.vo.UserIndexVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author HGT
 * @since 2021-03-02
 */
@Service
public class HelpServiceImpl extends ServiceImpl<HelpMapper, Help> implements HelpService {

    @Autowired
    private HelpMapper helpMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;

    @Transactional
    @Override
    public Integer helpIssue(String userId, String article, String time, String place, String to, BigDecimal fee, String describe, String phone, String[] photo) {
        Help help = new Help();
        help.setId(SnowflakeIdWorker.generateId());
        help.setUserId(userId);
        help.setSchoolId(userService.getUserSchoolIdByUserId(userId));
        help.setHelpArticle(article);
        help.setHelpTime(time);
        help.setHelpPlace(place);
        help.setHelpTo(to);
        help.setHelpFee(fee);
        help.setHelpDescr(describe);
        help.setHelpPhone(phone);
        help.setHelpPhoto(ArrayUtil.arrayToString(photo));
        help.setHelpState(0);
        return helpMapper.insert(help);
    }

    @Override
    public HelpListVo getHelpListByUserId(String userId, String query, long currentPage, long pageSize) {
        Page<Help> iPage = new Page<>(currentPage,pageSize);
        QueryWrapper qw = new QueryWrapper();
        qw.eq("user_id",userId);
        if(!query.isEmpty())
            qw.like("help_article",query);
        qw.orderByDesc("create_time");
        Page<Help> helpPage;
        helpPage = (Page<Help>) helpMapper.selectPage(iPage,qw);
        List<Help> helps = helpPage.getRecords();
        List<HelpVo> helpVos = new ArrayList<>();
        if(helps != null && helps.size() > 0){
            HelpVo helpVo;
            UserIndexVo userIndexVo = userService.getUserNameAndHImg(userId);
            for(Help help : helps){
                helpVo = new HelpVo();
                helpVo.setId(help.getId());
                helpVo.setUserId(userId);
                helpVo.setUserName(userIndexVo.getNickName());
                helpVo.setUserImg(userIndexVo.getImage());
                helpVo.setArticle(help.getHelpArticle());
                helpVo.setTime(help.getHelpTime());
                helpVo.setPlace(help.getHelpPlace());
                helpVo.setTo(help.getHelpTo());
                helpVo.setFee(help.getHelpFee());
                helpVo.setDescr(help.getHelpDescr());
                helpVo.setPhone(help.getHelpPhone());
                helpVo.setPhotos(ArrayUtil.stringToObject(help.getHelpPhoto()));
                helpVo.setState(help.getHelpState());
                helpVo.setAcceptUserId(help.getAcceptUserId());
                helpVo.setCreateTime(help.getCreateTime());
                helpVo.setCommentVoList(commentService.getCommentsByParentId(help.getId()));
                helpVos.add(helpVo);
            }
        }
        HelpListVo helpListVo = new HelpListVo();
        helpListVo.setCurrentPage(helpPage.getCurrent());
        helpListVo.setTotal(helpPage.getTotal());
        helpListVo.setPages(helpPage.getPages());
        helpListVo.setHelpVoList(helpVos);
        return helpListVo;
    }

    @Override
    public HelpListVo getHelpList(String userId, String query, long currentPage, long pageSize) {
        Integer schoolId = userService.getUserSchoolIdByUserId(userId);
        Page<Help> iPage = new Page<>(currentPage,pageSize);
        QueryWrapper qw = new QueryWrapper();
        qw.eq("school_id",schoolId);
        qw.eq("help_state",0); // 这里，默认使用and连接 即qw.and()
        qw.or();
        qw.eq("accept_user_id",userId);
        if(!query.isEmpty())
            qw.like("help_article",query);
        qw.orderByDesc("create_time");
        Page<Help> helpPage;
        helpPage = (Page<Help>) helpMapper.selectPage(iPage,qw);
        List<Help> helps = helpPage.getRecords();
        List<HelpVo> helpVos = new ArrayList<>();
        if(helps != null && helps.size() > 0) {
            HelpVo helpVo;
            UserIndexVo userIndexVo;
            for(Help help : helps){
                helpVo = new HelpVo();
                helpVo.setId(help.getId());
                userIndexVo = userService.getUserNameAndHImg(help.getUserId());
                helpVo.setUserId(help.getUserId());
                helpVo.setUserName(userIndexVo.getNickName());
                helpVo.setUserImg(userIndexVo.getImage());
                helpVo.setArticle(help.getHelpArticle());
                helpVo.setTime(help.getHelpTime());
                helpVo.setPlace(help.getHelpPlace());
                helpVo.setTo(help.getHelpTo());
                helpVo.setFee(help.getHelpFee());
                helpVo.setDescr(help.getHelpDescr());
                helpVo.setPhone(help.getHelpPhone());
                helpVo.setPhotos(ArrayUtil.stringToObject(help.getHelpPhoto()));
                helpVo.setState(help.getHelpState());
                helpVo.setAcceptUserId(help.getAcceptUserId());
                helpVo.setCreateTime(help.getCreateTime());
                helpVo.setCommentVoList(commentService.getCommentsByParentId(help.getId()));
                helpVos.add(helpVo);
            }
        }
        HelpListVo helpListVo = new HelpListVo();
        helpListVo.setCurrentPage(helpPage.getCurrent());
        helpListVo.setTotal(helpPage.getTotal());
        helpListVo.setPages(helpPage.getPages());
        helpListVo.setHelpVoList(helpVos);
        return helpListVo;
    }

    @Override
    public Integer deleteHelpById(Long id) {
        return helpMapper.deleteById(id);
    }

    @Override
    public HelpListVo loadingMoreHelpList(String userId, String query, long currentPage, long pageSize, Date date) {
        Integer schoolId = userService.getUserSchoolIdByUserId(userId);
        Page<Help> iPage = new Page<>(currentPage,pageSize);
        QueryWrapper qw = new QueryWrapper();
        qw.eq("school_id",schoolId);
        qw.or();
        qw.eq("accept_user_id",userId);
        if(!query.isEmpty())
            qw.like("help_article",query);
        qw.orderByDesc("create_time");
        qw.le("create_time",date);
        Page<Help> helpPage;
        helpPage = (Page<Help>) helpMapper.selectPage(iPage,qw);
        List<Help> helps = helpPage.getRecords();
        List<HelpVo> helpVos = new ArrayList<>();
        if(helps != null && helps.size() > 0) {
            HelpVo helpVo;

            UserIndexVo userIndexVo;
            for(Help help : helps){
                helpVo = new HelpVo();
                helpVo.setId(help.getId());
                userIndexVo = userService.getUserNameAndHImg(help.getUserId());
                helpVo.setUserId(help.getUserId());
                helpVo.setUserName(userIndexVo.getNickName());
                helpVo.setUserImg(userIndexVo.getImage());
                helpVo.setArticle(help.getHelpArticle());
                helpVo.setTime(help.getHelpTime());
                helpVo.setPlace(help.getHelpPlace());
                helpVo.setTo(help.getHelpTo());
                helpVo.setFee(help.getHelpFee());
                helpVo.setDescr(help.getHelpDescr());
                helpVo.setPhone(help.getHelpPhone());
                helpVo.setPhotos(ArrayUtil.stringToObject(help.getHelpPhoto()));
                helpVo.setState(help.getHelpState());
                helpVo.setAcceptUserId(help.getAcceptUserId());
                helpVo.setCreateTime(help.getCreateTime());
                helpVo.setCommentVoList(commentService.getCommentsByParentId(help.getId()));
                helpVos.add(helpVo);
            }
        }

        HelpListVo helpListVo = new HelpListVo();
        helpListVo.setCurrentPage(helpPage.getCurrent());
        helpListVo.setTotal(helpPage.getTotal());
        helpListVo.setPages(helpPage.getPages());
        helpListVo.setHelpVoList(helpVos);
        return helpListVo;
    }

    @Override
    public HelpListVo loadingMoreHelpListByUserId(String userId, String query, long currentPage, long pageSize, Date date) {
        Page<Help> iPage = new Page<>(currentPage,pageSize);
        QueryWrapper qw = new QueryWrapper();
        qw.eq("user_id",userId);
        if(!query.isEmpty())
            qw.like("help_article",query);
        qw.orderByDesc("create_time");
        qw.le("create_time",date);
        Page<Help> helpPage;
        helpPage = (Page<Help>) helpMapper.selectPage(iPage,qw);
        List<Help> helps = helpPage.getRecords();
        List<HelpVo> helpVos = new ArrayList<>();
        if(helps != null && helps.size() > 0) {
            HelpVo helpVo;

            UserIndexVo userIndexVo = userService.getUserNameAndHImg(userId);
            for(Help help : helps){
                helpVo = new HelpVo();
                helpVo.setId(help.getId());
                helpVo.setUserId(userId);
                helpVo.setUserName(userIndexVo.getNickName());
                helpVo.setUserImg(userIndexVo.getImage());
                helpVo.setArticle(help.getHelpArticle());
                helpVo.setTime(help.getHelpTime());
                helpVo.setPlace(help.getHelpPlace());
                helpVo.setTo(help.getHelpTo());
                helpVo.setFee(help.getHelpFee());
                helpVo.setDescr(help.getHelpDescr());
                helpVo.setPhone(help.getHelpPhone());
                helpVo.setPhotos(ArrayUtil.stringToObject(help.getHelpPhoto()));
                helpVo.setState(help.getHelpState());
                helpVo.setAcceptUserId(help.getAcceptUserId());
                helpVo.setCreateTime(help.getCreateTime());
                helpVo.setCommentVoList(commentService.getCommentsByParentId(help.getId()));
                helpVos.add(helpVo);
            }
        }

        HelpListVo helpListVo = new HelpListVo();
        helpListVo.setCurrentPage(helpPage.getCurrent());
        helpListVo.setTotal(helpPage.getTotal());
        helpListVo.setPages(helpPage.getPages());
        helpListVo.setHelpVoList(helpVos);
        return helpListVo;
    }

    @Transactional
    @Override
    public Integer acceptHelp(Long id, String userId) {
        Help help = helpMapper.selectById(id);
        help.setHelpState(1);
        help.setAcceptUserId(userId);
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("id",id);
        updateWrapper.eq("help_state",0);
        return helpMapper.update(help,updateWrapper);
    }
}
