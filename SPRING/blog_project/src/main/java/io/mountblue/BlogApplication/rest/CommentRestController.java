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
        Post post = postService.findPostById(postId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUser = authentication.getName();
        User user = userService.findUserByName(loggedInUser);
        String email = user.getEmail();
        Comment newComment = new Comment();
        newComment.setComment(comment.getComment());
        newComment.setName(loggedInUser);
        newComment.setEmail(email);
        newComment.setPost(post);
        commentService.saveComment(newComment);
        newComment.setPost(post);
        postService.save(post);
        return "success";
    }


    @DeleteMapping("/delete{comment_id}")
    public String deleteComment(@PathVariable("comment_id") Long commentId, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUser = authentication.getName();
        Comment comment = commentService.findCommentById(commentId);
        if (comment == null) {
            return "Comment not found";
        }
        String author = comment.getPost().getAuthor() != null ? comment.getPost().getAuthor().getName() : null;
        if (authentication.getAuthorities().toString().equals("[ROLE_AUTHOR]") && !author.equals(loggedInUser)) {
            return "access-denied";
        }
        commentService.deleteCommentById(commentId);
        return "Deleted";
    }

    @PutMapping("/updateComment{postId}/{commentId}")
    public String updateComment(@PathVariable("postId") Long postId, @PathVariable("commentId") Long commentId, @RequestBody Comment commentRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUser = authentication.getName();
        Comment existingComment = commentService.findCommentById(commentId);
        if (existingComment == null) {
            return "Comment not found";
        }
        if (!existingComment.getPost().getId().equals(postId)) {
            return "Comment does not belong to the specified post";
        }
        String author = existingComment.getPost().getAuthor().getName();
        if (authentication.getAuthorities().toString().equals("[ROLE_AUTHOR]") && !author.equals(loggedInUser)) {
            return "access-denied";
        }
        existingComment.setComment(commentRequest.getComment());
        existingComment.setName(commentRequest.getName());
        existingComment.setEmail(commentRequest.getEmail());
        commentService.saveComment(existingComment);

        return "success";
    }


}
