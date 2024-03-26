package io.mountblue.BlogApplication.service;

import io.mountblue.BlogApplication.entity.Post;
import io.mountblue.BlogApplication.entity.Tag;
import io.mountblue.BlogApplication.entity.User;
import io.mountblue.BlogApplication.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class PostServiceImplementation implements PostService {

    public PostServiceImplementation() {}
    private UserRepository userRepository;
    private PostRepository postRepository;
    private TagRepository tagRepository;

    @Autowired
    public PostServiceImplementation(UserRepository userRepository, PostRepository postRepository, TagRepository tagRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    public List<Post> findAll() {
        List<Post> postDBData = postRepository.findAll();
        return postDBData;
    }

    @Override
    public void save(Post post) {
        postRepository.save(post);
    }

    @Override
    public Post findPostById(Long id) {
        return postRepository.findPostById(id);
    }

    @Override
    public void deleteById(Long id) {
        postRepository.deleteById(id);
    }
    @Override
    public boolean deleteByPostId(Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        if (optionalPost.isPresent()) {
            postRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<Post> findAllPostsByTagId(Long id) {
        return findAllPostsByTagId(id);
    }

    @Override
    public Post findPostByTagId(Long tagId) {
        return findPostByTagId(tagId);
    }

    @Override
    public String tagString(List<Tag> tags) {
        StringBuilder tagListBuilder = new StringBuilder();
        for (Tag tag : tags) {
            tagListBuilder.append(tag.getName());
            tagListBuilder.append(",");
        }
        tagListBuilder.deleteCharAt(tagListBuilder.length() - 1);
        String tagList = tagListBuilder.toString();
        return tagList;
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }



    @Override
    public Page<Post> sort(List<Post> posts, String sort, Pageable pageable) {
        Page<Post> sortedPosts;
        if (sort.equals("Newest")) {
            sortedPosts = postRepository.findPostsByOrderByCreatedAtDesc(posts, pageable);
        } else {
            sortedPosts = postRepository.findPostsByOrderByCreatedAtAsc(posts, pageable);
        }
        return sortedPosts;
    }

    @Override
    public Page<Post> change(String keyword, String sort, Model model, List<String> selectedTags, LocalDate startDate, LocalDate endDate, List<String> author, Pageable pageable) {
        List<Post> searchedPosts;
        if (keyword != null && !keyword.isEmpty()) {
            searchedPosts = postRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCaseOrTagsNameContainingIgnoreCaseOrAuthorNameContainingIgnoreCase(keyword, keyword, keyword, keyword);
            Collections.sort(searchedPosts, Comparator.comparing(Post::getCreatedAt).reversed());
        } else {
            searchedPosts = postRepository.findAllByOrderByCreatedAtDesc();
        }

        if (selectedTags != null && !selectedTags.isEmpty()) {
            List<Post> postsWithSelectedTags = new ArrayList<>();
            for (String name : selectedTags) {
                List<Post> filteredPost = tagRepository.findByTagName(name);
                postsWithSelectedTags.addAll(filteredPost);
            }
            Iterator<Post> iterator = searchedPosts.iterator();
            while (iterator.hasNext()) {
                Post post = iterator.next();
                if (!postsWithSelectedTags.contains(post)) {
                    iterator.remove();
                }
            }
        }

        if (startDate != null && endDate != null) {
            List<Post> postsCreatedWithinThisRange = postRepository.findByCreatedAtBetween(startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX));
            Iterator<Post> iterator = searchedPosts.iterator();
            while (iterator.hasNext()) {
                Post post = iterator.next();
                if (!postsCreatedWithinThisRange.contains(post)) {
                    iterator.remove();
                }
            }
        }

        if (author != null && !author.isEmpty()) {
            List<Post> postsFromTheSelectedAuthors = new ArrayList<>();
            for (String author1 : author) {
                User user = userRepository.findByName(author1);
                List<Post> posts = postRepository.filterPosts(user);
                postsFromTheSelectedAuthors.addAll(posts);
            }
            Iterator<Post> iterator = searchedPosts.iterator();
            while (iterator.hasNext()) {
                Post post = iterator.next();
                if (!postsFromTheSelectedAuthors.contains(post)) {
                    iterator.remove();
                }
            }
        }
        return sort(searchedPosts, sort, pageable);
    }

    @Override
    public boolean isAuthor(Long postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = authentication.getName();
        Post post = postRepository.findById(postId).orElse(null);
        return post != null && post.getAuthor() != null && post.getAuthor().getName().equals(loggedInUsername);
    }
    @Override
    public void createOrUpdate(Post post, String action, String tagsString) {
        if ("Publish".equals(action)) {
            post.setIs_published(true);
            post.setPublished_at(LocalDateTime.now());
        } else if ("Save".equals(action)) {
            post.setIs_published(false);
        }
        List<Tag> newTags = tagList(tagsString);
       List<Tag> tags = removeDuplicateTags(newTags);
        post.setTags(tags);
        int currentPostLength = post.getContent().length();
        String excerpt = post.getContent().substring(0, Math.min(currentPostLength, 150));
        post.setExcerpt(excerpt);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();
        User currentLoggedInUser = userRepository.findUserByName(user);
        if (post.getId() != null) {
            Post existingPost = postRepository.findPostById(post.getId());
            post.setIs_published(true);
            if (existingPost != null) {
                existingPost.setTitle(post.getTitle());
                existingPost.setContent(post.getContent());
                existingPost.setIs_published(post.isIs_published());
                existingPost.setUpdatedAt(LocalDateTime.now());
                existingPost.setTags(post.getTags());
                existingPost.setExcerpt(excerpt);
                existingPost.setAuthor(currentLoggedInUser);
                postRepository.save(existingPost);
            }
        } else {
            post.setAuthor(currentLoggedInUser);
            userRepository.save(currentLoggedInUser);
            postRepository.save(post);
        }

    }

    @Override
    public List<Tag> tagList(String tag) {
        String[] tagNames = tag.split(",");
        List<Tag> tags = new ArrayList<>();
        for (String tagName : tagNames) {
            Tag t = new Tag();
            t.setName(tagName.trim());
            tags.add(t);
        }
        return tags;
    }

    @Override
    public List<Tag> removeDuplicateTags(List<Tag> newTags) {
        List<Tag> allTags = tagRepository.findAll();
        List<Tag> tags = new ArrayList<>();
        for (Tag tag : newTags) {
            String tagName = tag.getName();
            boolean checkIfExists = false;
            for (Tag tempTag : allTags) {
                String tempTagName = tempTag.getName();
                if (tempTagName.equals(tagName)) {
                    checkIfExists = true;
                    tags.add(tempTag);
                    break;
                }
            }
            if (checkIfExists == false) {
                tags.add(tag);
            }
        }
        return tags;

    }
}
