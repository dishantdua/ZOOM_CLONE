package io.mountblue.BlogApplication.controller;

import io.mountblue.BlogApplication.service.CommentService;
import io.mountblue.BlogApplication.service.CommentServiceImplementation;
import io.mountblue.BlogApplication.entity.Comment;
import io.mountblue.BlogApplication.entity.Post;
import io.mountblue.BlogApplication.service.PostService;
import io.mountblue.BlogApplication.service.PostServiceImplementation;
import org.hibernate.internal.build.AllowNonPortable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CommentController {
    private CommentService commentService;
    private PostService postService;
    @Autowired
    public CommentController(CommentService commentService, PostService postService ){
        this.commentService = commentService;
        this.postService = postService;
    }

    @PostMapping("/addComment{post_id}")
    public String addComment(@RequestParam("theComment") String commentText, @PathVariable("post_id") Long id,
                             @RequestParam("username") String name, @RequestParam("email") String email,
                             @ModelAttribute("comment") Comment comment, @RequestParam(name = "id", required = false) Long commentId, Model model) {
        commentService.addComment(commentText, id, comment, commentId, model, name, email);
        model.addAttribute("commentId", comment.getId());
        return "redirect:/post" + id;
    }

    @PostMapping("/deleteComment{comment_id}")
    public String deleteComment(@PathVariable("comment_id") Long id, Model model) {
        Comment comment = commentService.findCommentById(id);
        Post post = comment.getPost();
        Long post_id = post.getId();
        commentService.deleteCommentById(id);
        return "redirect:/post" + post_id;
    }

    @GetMapping("/updateComment{postId}/{commentId}")
    public String updateComment(@PathVariable("postId") Long postId, @PathVariable("commentId") Long commentId, Model model) {
        Post post = postService.findPostById(postId);
        List<Comment> comments = post.getComments();
        Comment comment = commentService.findCommentById(commentId);
        if (comment != null) {
            String theComment = comment.getComment();
            model.addAttribute("post", post);
            model.addAttribute("commentId", commentId);
            model.addAttribute("comments", comments);
            model.addAttribute("theComment", theComment);
            model.addAttribute("comment", comment);
            commentService.saveComment(comment);
            return "post-detail";
        } else {
            return "error";
        }
    }
}
