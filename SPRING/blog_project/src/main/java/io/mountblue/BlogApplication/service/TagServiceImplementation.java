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

}
