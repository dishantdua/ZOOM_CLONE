package io.mountblue.BlogApplication.controller;

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

@Controller
public class PostController {
    private PostService postService;
    private TagService tagService;
    private UserService userService;
    @Autowired
    public PostController(PostService postService, TagService tagService, UserService userService) {
        this.postService = postService;
        this.tagService = tagService;
        this.userService = userService;
    }

    @GetMapping("/newpost")
    public String newPost(Model model) {
        model.addAttribute("post", new Post());
        return "new-post";
    }

    @PostMapping("/createOrUpdate")
    public String createOrUpdatePost(@ModelAttribute("post") Post post, @RequestParam String action, @RequestParam("tagList") String tagsString) {
        postService.createOrUpdate(post, action, tagsString);
        return "redirect:/post" + post.getId();
    }

    @GetMapping("/post{post_id}")
    public String showPostDetail(@PathVariable("post_id") Long postId, Model model) {
        Post post = postService.findPostById(postId);
        model.addAttribute("post", post);
        return "post-detail";
    }

    @PostMapping("/updatepost{post_id}")
    public String showUpdatePost(@PathVariable("post_id") Long id, Model model) {
        Post post = postService.findPostById(id);
        List<Tag> tags = post.getTags();
        String tagListBuilder = tagService.tagString(tags);
        model.addAttribute("post", post);
        model.addAttribute("tagsString", tagListBuilder);
        return "new-post";
    }

    @PostMapping("/deletePost{post_id}")
    public String deletePost(@PathVariable("post_id") Long id, Model model) {
        postService.deleteById(id);
        return "redirect:/";
    }

    @GetMapping("/")
    public String showPage(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
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
        model.addAttribute("hasNextPage", hasNextPage);
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
        return "all-posts";
    }
}


