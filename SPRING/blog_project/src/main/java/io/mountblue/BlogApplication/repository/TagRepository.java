package io.mountblue.BlogApplication.repository;

import io.mountblue.BlogApplication.entity.Post;
import io.mountblue.BlogApplication.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {

    @Query("SELECT DISTINCT p FROM Post p JOIN FETCH p.tags")
    List<Post> findAllWithTags();

    @Query("SELECT Distinct p FROM Post p JOIN p.tags t WHERE t.name = :name")
    List<Post> findByTagName(@Param("name") String name);

}
