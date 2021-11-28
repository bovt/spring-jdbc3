package ru.bvt.jdbcnotesenginelite.repository;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.bvt.jdbcnotesenginelite.domain.Author;
import ru.bvt.jdbcnotesenginelite.domain.Category;
import ru.bvt.jdbcnotesenginelite.domain.Note;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

@SuppressWarnings({"SqlNoDataSourceInspection", "ConstantConditions", "SqlDialectInspection"})
@Repository
public class NoteRepositoryJdbc implements NoteRepository {

    private final JdbcOperations jdbc;
    private final NamedParameterJdbcOperations namedParameterJdbcOperations;
    private final CategoryRepository categoryRepository;
    private final AuthorRepository authorRepository;

    public NoteRepositoryJdbc(NamedParameterJdbcOperations namedParameterJdbcOperations,
                              CategoryRepository categoryRepository, AuthorRepository authorRepository, JdbcTemplate jdbcTemplate) {
        this.jdbc = namedParameterJdbcOperations.getJdbcOperations();
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
        this.categoryRepository = categoryRepository;
        this.authorRepository = authorRepository;
    }

    private static class NoteMapper implements RowMapper<Note> {

        @Override
        public Note mapRow(ResultSet resultSet, int i) throws SQLException {
            return new Note(resultSet.getLong("id"), resultSet.getString("text"),
                    new Author(resultSet.getLong("author_id"),
                            resultSet.getString("author_name"), resultSet.getString("author_password")),
                    new Category(resultSet.getLong("category_id"), resultSet.getString("category_name")));
        }
    }

    ;

    public List<Note> findAll() {
        return jdbc.query("select n.id, n.text, a.id author_id, a.name author_name, a.password author_password, c.id category_id, c.name category_name" +
                " from (note n left join category c on " + "n.category_id = c.id) left join author a on n.author_id = a.id", new NoteMapper());
    }

    ;


    public boolean existsById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        return (namedParameterJdbcOperations.queryForObject("select count(*) from note where id = :id", params, Integer.class) > 0);
    }

    ;

    @Override
    public Note findById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        Note result = null;
        if (id == 0) {
            return null;
        }
        try {
            result = namedParameterJdbcOperations.queryForObject(
                    "select n.id, n.text, a.id author_id, a.name author_name, a.password author_password, c.id category_id, c.name category_name" +
                            " from (note n left join category c on " + "n.category_id = c.id) left join author a on n.author_id = a.id where n.id = :id", params, new NoteMapper());
        } catch (
                IncorrectResultSizeDataAccessException ex) {
            return null;
        }
        return result;
    }

    ;

    public Note findByText(String text) {
        Map<String, Object> params = Collections.singletonMap("text", text);
        Note result = null;
        try {
            result = namedParameterJdbcOperations.queryForObject(
                    "select * from note where text = :name", params, new NoteRepositoryJdbc.NoteMapper());
        } catch (IncorrectResultSizeDataAccessException ex) {
            return null;
        }
        return result;
    }

    ;

    public Note save(Note note) {
        KeyHolder holder = new GeneratedKeyHolder();
        if (note == null) return null;

        // Prepare category
        Category category = note.getCategory();
        long categoryId;
        note.setCategory(categoryRepository.save(note.getCategory())); // в случае создания категории будет подтянут её новый id
        categoryId = note.getCategory().getId();

        // Prepare author
        note.setAuthor(authorRepository.save(note.getAuthor())); // в случае создания автора будет подтянут его новый id

        // Save note
        Note oldNote = findById(note.getId());
        if (oldNote == null) {
            jdbc.update(connection -> {
                PreparedStatement ps = connection
                        .prepareStatement("insert into note (text, author_id, category_id) values (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, note.getText());
                ps.setLong(2, note.getAuthor().getId());
                ps.setLong(3, categoryId);
                return ps;
            }, holder);
            note.setId(holder.getKey().longValue());
        } else {
            jdbc.update("update note set text = ?, author_id = ?,  category_id = ?  where id = ?",
                    note.getText(), note.getAuthor().getId(), categoryId, note.getId());
        }
        return note;
    }

    public long deleteById(Long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        namedParameterJdbcOperations.update(
                "delete from note where id = :id", params
        );
        return id;
    }

    ;


}
