package com.glowapex.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String shortDescription;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String coverImage;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    private String slug;

    private LocalDate date;

    private String metaTitle;

    private String metaDescription;

    private String metaKeywords;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private BlogCategory category;
}