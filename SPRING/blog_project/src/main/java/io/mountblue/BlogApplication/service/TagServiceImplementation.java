package io.mountblue.BlogApplication.service;

import io.mountblue.BlogApplication.entity.Post;
import io.mountblue.BlogApplication.entity.Tag;
import io.mountblue.BlogApplication.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TagServiceImplementation implements TagService {
    public TagServiceImplementation(){
    }

    private TagRepository tagRepository;
    @Autowired
    public TagServiceImplementation(TagRepository tagRepository){
        this.tagRepository=tagRepository;
    }

    @Override
    public List<Post> findByTagName(String name) {
        return tagRepository.findByTagName(name);
    }

    @Override
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    @Override
    public List<Post> getAllPosts() {
        return tagRepository.findAllWithTags();
    }

    @Override
    public List<Tag> findAll() {
        return tagRepository.findAll();
    }

}
