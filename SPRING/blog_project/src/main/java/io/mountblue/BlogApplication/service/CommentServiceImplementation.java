package io.mountblue.BlogApplication.service;

import io.mountblue.BlogApplication.entity.Comment;
import io.mountblue.BlogApplication.entity.Post;
import io.mountblue.BlogApplication.repository.CommentRepository;
import io.mountblue.BlogApplication.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public CommentServiceImplementation(CommentRepository commentRepository,PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository=postRepository;
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
    public void addComment(String commentText,Long id,Comment comment,Long commentId,Model model,String name,String email){
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
}
