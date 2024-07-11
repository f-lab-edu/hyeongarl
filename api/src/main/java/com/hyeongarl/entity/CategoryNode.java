package com.hyeongarl.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CategoryNode {
    private String name;
    private List<CategoryNode> children;
}
