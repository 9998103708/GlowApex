package com.glowapex.service;

import com.glowapex.entity.Blog;
import com.glowapex.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogService {

    private final BlogRepository blogRepository;

    public Blog createBlog(Blog blog) {
        return blogRepository.save(blog);
    }

    public List<Blog> getAllBlogs() {
        return blogRepository.findAll();
    }

    public Blog getBlogBySlug(String slug) {
        return blogRepository.findBySlug(slug);
    }

    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }
}