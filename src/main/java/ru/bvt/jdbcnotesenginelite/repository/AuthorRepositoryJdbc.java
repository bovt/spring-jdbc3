package ru.bvt.jdbcnotesenginelite.repository;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.bvt.jdbcnotesenginelite.domain.Author;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

@Repository
@SuppressWarnings({"SqlNoDataSourceInspection", "ConstantConditions", "SqlDialectInspection"})
public class AuthorRepositoryJdbc implements AuthorRepository {

    private final JdbcOperations jdbc;
    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    public AuthorRepositoryJdbc(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.jdbc = namedParameterJdbcOperations.getJdbcOperations();
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
    }

    private static class AuthorMapper implements RowMapper<Author> {
        @Override
        public Author mapRow(ResultSet resultSet, int i) throws SQLException {
            long id = resultSet.getLong("id");
            String name = resultSet.getString("name");
            String password = resultSet.getString("password");
            return new Author(id, name, password);
        }
    }

    public List<Author> findAll() {
        return jdbc.query("select * from author", new AuthorRepositoryJdbc.AuthorMapper());
    }

    ;

    public boolean existsById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        return (namedParameterJdbcOperations.queryForObject("select count(*) from author where id = :id", params, Integer.class) > 0);
    }

    ;

    public boolean existsByName(String name) {
        Map<String, Object> params = Collections.singletonMap("name", name);
        return (namedParameterJdbcOperations.queryForObject("select count(*) from author where name = :name", params, Integer.class) > 0);
    }

    ;

    public Author findById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        Author result = null;
        if (id == 0) {
            return null;
        }
        try {
            result = namedParameterJdbcOperations.queryForObject(
                    "select * from author where id = :id", params, new AuthorRepositoryJdbc.AuthorMapper());
        } catch (IncorrectResultSizeDataAccessException ex) {
            return null;
        }
        return result;
    }

    ;

    public Author save(Author author) {
        KeyHolder holder = new GeneratedKeyHolder();
        Author oldAuthor = findById(author.getId());
        if (author == null) return null;
        if (oldAuthor == null) {
            if (existsByName(author.getName())) {
                return null;
            }
            ;
            jdbc.update(connection -> {
                PreparedStatement ps = connection
                        .prepareStatement("insert into author (name, password) values (?, ?)", Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, author.getName());
                ps.setString(2, author.getPassword());
                return ps;
            }, holder);
            author.setId(holder.getKey().longValue());
        } else {
            if (existsByName(author.getName()) && !author.getName().equals(oldAuthor.getName())) {
                return null;
            }
            ;
            jdbc.update("update author set name = ?, password = ? where id = ?", author.getName(), author.getPassword(), author.getId());
        }
        return author;
    }

    ;
};
