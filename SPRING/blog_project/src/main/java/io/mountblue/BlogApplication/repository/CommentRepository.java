package io.mountblue.BlogApplication.repository;

import io.mountblue.BlogApplication.entity.Comment;
import io.mountblue.BlogApplication.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Comment findCommentById(Long id);
    @Modifying
    @Query("DELETE FROM Comment c WHERE c.id = :id")
    void deleteById(Long id);
}
