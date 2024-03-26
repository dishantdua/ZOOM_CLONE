package io.mountblue.BlogApplication.repository;

import io.mountblue.BlogApplication.entity.Post;
import io.mountblue.BlogApplication.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    Post findPostById(Long id);

    List<Post> findAllByOrderByCreatedAtDesc();

    List<Post> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCaseOrTagsNameContainingIgnoreCaseOrAuthorNameContainingIgnoreCase(String keyword1, String keyword2, String keyword3, String keyword4);

    @Query("SELECT p FROM Post p WHERE p IN :posts ORDER BY p.createdAt DESC")
    Page<Post> findPostsByOrderByCreatedAtDesc(@Param(value = "posts") List<Post> posts, Pageable pageable);

    @Query("SELECT" + " p FROM Post p WHERE p IN :posts ORDER BY p.createdAt ASC")
    Page<Post> findPostsByOrderByCreatedAtAsc(@Param(value = "posts") List<Post> posts, Pageable pageable);

    List<Post> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT p FROM Post p WHERE p.author = :user ORDER BY p.createdAt DESC")
    List<Post> filterPosts(@Param("user") User user);


}
