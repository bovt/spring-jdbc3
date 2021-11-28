package ru.bvt.jdbcnotesenginelite.service;

import ru.bvt.jdbcnotesenginelite.domain.Author;

import java.util.List;

public interface AuthorService {

    List<Author> getAllAuthors();

    long addAuthor(Author newAuthor);

    Author getAuthor(long id);

    boolean setAuthor(Author author);

}
