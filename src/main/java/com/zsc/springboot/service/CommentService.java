package com.zsc.springboot.service;

import com.zsc.springboot.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zsc.springboot.vo.CommentVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author HGT
 * @since 2021-02-28
 */
public interface CommentService extends IService<Comment> {
    public List<CommentVo> getCommentsByParentId(Long parentId);
    public Integer addComment(Long parentId,String userId,String content);
}
