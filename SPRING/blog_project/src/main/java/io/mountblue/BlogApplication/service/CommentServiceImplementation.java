package io.mountblue.BlogApplication.service;

import io.mountblue.BlogApplication.entity.Comment;
import io.mountblue.BlogApplication.entity.Post;
import io.mountblue.BlogApplication.entity.User;
import io.mountblue.BlogApplication.repository.CommentRepository;
import io.mountblue.BlogApplication.repository.PostRepository;
import io.mountblue.BlogApplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.List;

@Service
public class CommentServiceImplementation implements CommentService {
    public CommentServiceImplementation() {
    }

    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private UserRepository userRepository;

    @Autowired
    public CommentServiceImplementation(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void saveComment(Comment comment) {
        commentRepository.save(comment);
    }

    @Override
    public Comment findCommentById(Long id) {
        return commentRepository.findCommentById(id);
    }

    @Override
    @Transactional
    public void deleteCommentById(Long id) {
        commentRepository.deleteById(id);
    }

    @Override
    public void addComment(String commentText, Long id, Comment comment, Long commentId, Model model, String name, String email) {
        Post post = postRepository.findPostById(id);
        List<Comment> list = post.getComments();
        Comment newComment;
        if (commentId == null) {
            newComment = new Comment();
        } else {
            newComment = commentRepository.findCommentById(commentId);
        }
        newComment.setComment(commentText);
        newComment.setName(name);
        newComment.setEmail(email);
        list.add(newComment);
        post.setComments(list);
        newComment.setPost(post);
        postRepository.save(post);
    }

    @Override
    public String addCommentUsingRest(Comment comment, Long postId) {
        Post post = postRepository.findPostById(postId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUser = authentication.getName();
        User user = userRepository.findUserByName(loggedInUser);
        String email = user.getEmail();
        Comment newComment = new Comment();
        newComment.setComment(comment.getComment());
        newComment.setName(loggedInUser);
        newComment.setEmail(email);
        newComment.setPost(post);
        commentRepository.save(newComment);
        newComment.setPost(post);
        postRepository.save(post);
        return "Comment added";
    }

    @Override
    public String deleteCommentUsingRest(Long commentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUser = authentication.getName();
        Comment comment = commentRepository.findCommentById(commentId);
        if (comment == null) {
            return "Comment not found";
        }
        String author = comment.getPost().getAuthor() != null ? comment.getPost().getAuthor().getName() : null;
        if (authentication.getAuthorities().toString().equals("[ROLE_AUTHOR]") && !author.equals(loggedInUser)) {
            return "access-denied";
        }
        commentRepository.deleteById(commentId);
        return "Deleted";
    }

    @Override
    public String updateCommentUsingRest(Long postId, Long commentId, Comment comment) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUser = authentication.getName();
        Comment existingComment = commentRepository.findCommentById(commentId);
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
        existingComment.setComment(comment.getComment());
        existingComment.setName(comment.getName());
        existingComment.setEmail(comment.getEmail());
        commentRepository.save(existingComment);
        return "updated";
    }
}
