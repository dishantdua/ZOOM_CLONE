package io.mountblue.BlogApplication.rest;

import io.mountblue.BlogApplication.entity.User;
import io.mountblue.BlogApplication.service.*;
import io.mountblue.BlogApplication.entity.Comment;
import io.mountblue.BlogApplication.entity.Post;
import org.hibernate.internal.build.AllowNonPortable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/blog")
public class CommentRestController {
    private CommentService commentService;
    private PostService postService;
    private UserService userService;

    @Autowired
    public CommentRestController(CommentService commentService, PostService postService, UserService userService) {
        this.commentService = commentService;
        this.postService = postService;
        this.userService = userService;
    }

    @PostMapping("/addComment{post_id}")
    public String addComment(@RequestBody Comment comment, @PathVariable("post_id") Long postId) {
       commentService.addCommentUsingRest(comment,postId);
        return "success";
    }


    @DeleteMapping("/delete{comment_id}")
    public String deleteComment(@PathVariable("comment_id") Long commentId) {
         String status=commentService.deleteCommentUsingRest(commentId);
        return status;
    }

    @PutMapping("/updateComment{postId}/{commentId}")
    public String updateComment(@PathVariable("postId") Long postId, @PathVariable("commentId") Long commentId, @RequestBody Comment commentRequest) {
        String status=commentService.updateCommentUsingRest(postId,commentId,commentRequest);
        return status;
    }


}
