package com.glowapex.service;

import com.glowapex.entity.BlogCategory;
import com.glowapex.repository.BlogCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogCategoryService {

    private final BlogCategoryRepository blogCategoryRepository;

    public BlogCategory createCategory(BlogCategory category) {
        return blogCategoryRepository.save(category);
    }

    public List<BlogCategory> getAllCategories() {
        return blogCategoryRepository.findAll();
    }
}