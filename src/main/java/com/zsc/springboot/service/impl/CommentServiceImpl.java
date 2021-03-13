package com.zsc.springboot.service.impl;

import com.zsc.springboot.entity.Comment;
import com.zsc.springboot.mapper.CommentMapper;
import com.zsc.springboot.service.CommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsc.springboot.vo.CommentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author HGT
 * @since 2021-02-28
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public List<CommentVo> getCommentsByParentId(Long parentId) {
        return commentMapper.getCommentsByParentId(parentId);
    }

    @Transactional
    @Override
    public Integer addComment(Long parentId, String userId, String content) {
        Comment comment = new Comment();
        comment.setParentId(parentId);
        comment.setUserId(userId);
        comment.setContent(content);
        return commentMapper.insert(comment);
    }
}
