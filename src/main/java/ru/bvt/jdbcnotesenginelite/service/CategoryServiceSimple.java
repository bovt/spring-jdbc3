package ru.bvt.jdbcnotesenginelite.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bvt.jdbcnotesenginelite.domain.Category;
import ru.bvt.jdbcnotesenginelite.repository.CategoryRepository;

import java.util.List;

@AllArgsConstructor
@Service
public class CategoryServiceSimple implements CategoryService {

    private final CategoryRepository repository;

    @Transactional(readOnly = true)
    public List<Category> getAllCategories() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Category getCategory(long id) {
        return repository.findById(id);
    }

    @Transactional(readOnly = false)
    public long addCategory(Category category) {
        if (repository.existsById(category.getId())) {
            return 0;
        }
        category = repository.save(category);
        if (category == null) {
            return 0;
        }
        return category.getId();
    }

    @Transactional(readOnly = false)
    public boolean setCategory(Category category) {
        if (!repository.existsById(category.getId())) {
            return false;
        }
        return (repository.save(category) != null);
    }

}
