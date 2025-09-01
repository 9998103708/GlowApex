package com.glowapex.repository;

import com.glowapex.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Blog, Long> {
    Blog findBySlug(String slug);
}
