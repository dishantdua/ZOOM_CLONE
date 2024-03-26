package io.mountblue.BlogApplication.service;

import io.mountblue.BlogApplication.entity.Post;
import io.mountblue.BlogApplication.entity.Tag;
import io.mountblue.BlogApplication.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface PostService {
    List<Post> findAll();

    void save(Post post);

    Post findPostById(Long id);

    void deleteById(Long id);

    List<Post> findAllPostsByTagId(Long id);

    Post findPostByTagId(Long tagId);

    Page<Post> sort(List<Post> posts, String sort, Pageable pageable);

    void createOrUpdate(Post post, String action, String tagsString);

    Page<Post> change(String keyword, String sort, Model model, List<String> selectedTags, LocalDate startDate, LocalDate endDate, List<String> author,Pageable pageable);
    boolean isAuthor(Long postId) ;

    void saveUser(User user);

    String updatePostUsingRest(Long postId,Post updatedPost);

     String deletePostUsingRest(Long postId);

}
