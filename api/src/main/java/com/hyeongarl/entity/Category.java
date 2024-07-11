package com.hyeongarl.entity;

import com.hyeongarl.util.TreeConverter;
import jakarta.persistence.*;
import lombok.*;

import java.util.Map;

@Entity
@Table(name="category")
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Column(name = "user_id")
    private Long userId;

    @Convert(converter = TreeConverter.class)
    @Column(name = "category_tree", columnDefinition = "TEXT")
    private Map<String, Object> categoryTree;
}
