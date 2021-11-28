package ru.bvt.jdbcnotesenginelite.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bvt.jdbcnotesenginelite.domain.Author;
import ru.bvt.jdbcnotesenginelite.repository.AuthorRepository;

import java.util.List;

@AllArgsConstructor
@Service
public class AuthorServiceSimple implements AuthorService {

    private final AuthorRepository repository;

    @Transactional(readOnly = true)
    public List<Author> getAllAuthors() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Author getAuthor(long id) {
        return repository.findById(id);
    }

    @Transactional(readOnly = false)
    public long addAuthor(Author author) {
        if (repository.existsById(author.getId())) {
            return 0;
        }
        author = repository.save(author);
        if (author == null) {
            return 0;
        }
        return author.getId();
    }

    @Transactional(readOnly = false)
    public boolean setAuthor(Author author) {
        if (!repository.existsById(author.getId())) {
            return false;
        }
        return (repository.save(author) != null);
    }

}
