package io.mountblue.BlogApplication.service;

import io.mountblue.BlogApplication.entity.Post;
import io.mountblue.BlogApplication.entity.Tag;

import java.util.List;

public interface TagService {
    List<Post> findByTagName(String name);

    List<Tag> getAllTags();

    List<Post> getAllPosts();

    List<Tag> findAll();

    List<Tag> tagList(String tag);

    List<Tag> removeDuplicateTags(List<Tag> newTags);

    String tagString(List<Tag> tags);

}
