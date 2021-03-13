package com.zsc.springboot.controller;


import com.zsc.springboot.common.ServerResponse;
import com.zsc.springboot.form.CommentForm;
import com.zsc.springboot.service.CommentService;
import com.zsc.springboot.service.impl.CommentServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author HGT
 * @since 2021-02-28
 */
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;


    @ApiOperation(value = "发布评论",response = ServerResponse.class,httpMethod = "POST")
    @RequiresAuthentication
    @PostMapping("/commentIssue")
    public ServerResponse commentIssue(@Validated @RequestBody CommentForm commentForm){
        Integer addResult = commentService.addComment(commentForm.getParentId(),commentForm.getUserId(),commentForm.getContent());
        if(addResult != 1){
            return ServerResponse.fail("发布失败");
        }
        return ServerResponse.success(commentService.getCommentsByParentId(commentForm.getParentId()));
    }
}

