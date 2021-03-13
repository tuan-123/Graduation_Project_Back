package com.zsc.springboot.mapper;

import com.zsc.springboot.entity.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zsc.springboot.vo.CommentVo;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author HGT
 * @since 2021-02-28
 */
@Repository
public interface CommentMapper extends BaseMapper<Comment> {
    @Select("select user_id,content,create_time from comment where parent_id = #{parentId} and deleted = 0 order by create_time asc")
    List<CommentVo> getCommentsByParentId(Long parentId);
}
