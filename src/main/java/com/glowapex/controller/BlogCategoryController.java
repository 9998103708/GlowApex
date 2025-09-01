package com.glowapex.controller;

import com.glowapex.entity.BlogCategory;
import com.glowapex.service.BlogCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blog-categories")
@RequiredArgsConstructor
public class BlogCategoryController {

    private final BlogCategoryService blogCategoryService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<BlogCategory> createCategory(@RequestBody BlogCategory category) {
        return ResponseEntity.ok(blogCategoryService.createCategory(category));
    }

    @GetMapping
    public ResponseEntity<List<BlogCategory>> getAllCategories() {
        return ResponseEntity.ok(blogCategoryService.getAllCategories());
    }
}