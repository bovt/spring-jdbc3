package ru.bvt.jdbcnotesenginelite.repository;

import ru.bvt.jdbcnotesenginelite.domain.Author;
import ru.bvt.jdbcnotesenginelite.domain.Category;
import ru.bvt.jdbcnotesenginelite.domain.Note;

import java.util.List;
import java.util.Optional;

public interface NoteRepository {

    List<Note> findAll();

    Note findById(long id);

    Note findByText(String text);

    Note save(Note note);

    long deleteById(Long id);

}
