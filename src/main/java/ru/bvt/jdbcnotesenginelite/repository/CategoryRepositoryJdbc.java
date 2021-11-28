package ru.bvt.jdbcnotesenginelite.repository;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.bvt.jdbcnotesenginelite.domain.Author;
import ru.bvt.jdbcnotesenginelite.domain.Category;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

@Repository
@SuppressWarnings({"SqlNoDataSourceInspection", "ConstantConditions", "SqlDialectInspection"})
public class CategoryRepositoryJdbc implements CategoryRepository {

    private final JdbcOperations jdbc;
    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    public CategoryRepositoryJdbc(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.jdbc = namedParameterJdbcOperations.getJdbcOperations();
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
    }

    private static class CategoryMapper implements RowMapper<Category> {
        @Override
        public Category mapRow(ResultSet resultSet, int i) throws SQLException {
            long id = resultSet.getLong("id");
            String name = resultSet.getString("name");
            return new Category(id, name);
        }
    }

    public List<Category> findAll() {
        return jdbc.query("select * from category", new CategoryRepositoryJdbc.CategoryMapper());
    }

    ;

    public boolean existsById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        return (namedParameterJdbcOperations.queryForObject("select count(*) from category where id = :id", params, Integer.class) > 0);
    }

    ;

    public boolean existsByName(String name) {
        Map<String, Object> params = Collections.singletonMap("name", name);
        return (namedParameterJdbcOperations.queryForObject("select count(*) from category where name = :name", params, Integer.class) > 0);
    }

    ;

    public Category findById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        Category result = null;
        if (id == 0) {
            return null;
        }

        try {
            result = namedParameterJdbcOperations.queryForObject(
                    "select * from category where id = :id", params, new CategoryRepositoryJdbc.CategoryMapper());
        } catch (IncorrectResultSizeDataAccessException ex) {
            return null;
        }
        return result;
    }

    ;

    public Category findByName(String name) {
        Map<String, Object> params = Collections.singletonMap("name", name);
        Category result = null;
        try {
            result = namedParameterJdbcOperations.queryForObject(
                    "select * from category where name = :name", params, new CategoryRepositoryJdbc.CategoryMapper());
        } catch (IncorrectResultSizeDataAccessException ex) {
            return null;
        }
        return result;

    }

    ;

    public Category save(Category category) {
        KeyHolder holder = new GeneratedKeyHolder();
        Category oldCategory = findById(category.getId());
        if (category == null) return null;
        if (oldCategory == null) {
            if (existsByName(category.getName())) {
                return null;
            }
            jdbc.update(connection -> {
                PreparedStatement ps = connection
                        .prepareStatement("insert into category (name) values (?)", Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, category.getName());
                return ps;
            }, holder);
            category.setId(holder.getKey().longValue());
        } else {
            jdbc.update("update category set name = ? where id = ?", category.getName(), category.getId());
        }
        return category;
    }

}

