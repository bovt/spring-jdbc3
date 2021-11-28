package ru.bvt.jdbcnotesenginelite.repository;

import ru.bvt.jdbcnotesenginelite.domain.Category;

import java.util.List;

public interface CategoryRepository {

    List<Category> findAll();

    Category findById(long id);

    boolean existsById(long id);

    boolean existsByName(String name);

    Category findByName(String name);

    Category save(Category category);
}
