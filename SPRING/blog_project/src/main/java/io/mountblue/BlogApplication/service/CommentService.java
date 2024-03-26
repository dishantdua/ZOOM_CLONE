package io.mountblue.BlogApplication.service;

import io.mountblue.BlogApplication.entity.Comment;
import org.springframework.ui.Model;

public interface CommentService {
    void saveComment(Comment comment);

    Comment findCommentById(Long id);

    void deleteCommentById(Long id);

    void addComment(String commentText, Long id, Comment comment, Long commentId, Model model, String name, String email);

    void addCommentUsingRest(Comment comment,Long postId);

    String deleteCommentUsingRest(Long commentId);

    String updateCommentUsingRest(Long postId,Long commentId,Comment comment);
}
