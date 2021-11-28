package ru.bvt.jdbcnotesenginelite.service;

import ru.bvt.jdbcnotesenginelite.domain.Category;

import java.util.List;

public interface CategoryService {

    List<Category> getAllCategories();

    long addCategory(Category newCategory);

    Category getCategory(long id);

    boolean setCategory(Category category);

}
