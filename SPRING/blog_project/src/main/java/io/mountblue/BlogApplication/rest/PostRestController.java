package io.mountblue.BlogApplication.rest;

import io.mountblue.BlogApplication.service.*;
import io.mountblue.BlogApplication.entity.Post;
import io.mountblue.BlogApplication.entity.Tag;
import io.mountblue.BlogApplication.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/blog")
public class PostRestController {
    private PostService postService;
    private TagService tagService;
    private UserService userService;

    @Autowired
    public PostRestController(PostService postService, TagService tagService, UserService userService) {
        this.postService = postService;
        this.tagService = tagService;
        this.userService = userService;
    }

    @PostMapping("/newpost")
    public String newPost(@RequestBody Post post) {
        postService.save(post);
        return "saved";
    }

    @GetMapping("/post{post_id}")
    public Post showPostDetail(@PathVariable("post_id") Long postId) {
        Post post = postService.findPostById(postId);
        return post;
    }

    @PostMapping("/createOrUpdate")
    public String createOrUpdatePost(@ModelAttribute("post") Post post, @RequestParam String action, @RequestParam("tagList") String tagsString) {
        postService.createOrUpdate(post, action, tagsString);
        return "Post created or updated successfully";
    }

    @PutMapping("/updatepost{post_id}")
    public String updatePost(@PathVariable("post_id") Long postId, @RequestBody Post updatedPost) {
        String status=postService.updatePostUsingRest(postId,updatedPost);
        return status;
    }

    @DeleteMapping("/deletePost{post_id}")
    public String deletePost(@PathVariable("post_id") Long postId) {
        String status=postService.deletePostUsingRest(postId);
        return status;
    }

    @GetMapping("/")
    public Page<Post> showPage(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                               @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize, Model model,
                               @RequestParam(value = "keyword", required = false) String keyword, @RequestParam(value = "sort", defaultValue = "Newest") String sort,
                               @RequestParam(value = "selectedTags", required = false) List<String> selectedTags,
                               @RequestParam(value = "selectedAuthors", required = false) List<String> author,
                               @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                               @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        Pageable p = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").descending());
        Page<Post> posts = postService.change(keyword, sort, model, selectedTags, startDate, endDate, author, p);
        int totalPages = (int) Math.ceil((double) posts.getTotalElements() / pageSize);
        boolean hasNextPage = pageNumber < totalPages - 1;
        List<Tag> tags = tagService.findAll();
        List<User> users = userService.findAll();
        model.addAttribute("posts", posts);
        model.addAttribute("tags", tags);
        model.addAttribute("users", users);
        model.addAttribute("keyword", keyword);
        model.addAttribute("sort", sort);
        model.addAttribute("selectedAuthors", author);
        model.addAttribute("selectedTags", selectedTags);
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("hasNextPage", hasNextPage);
        return posts;
    }
}


