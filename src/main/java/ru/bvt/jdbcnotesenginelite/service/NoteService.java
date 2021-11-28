package ru.bvt.jdbcnotesenginelite.service;

import ru.bvt.jdbcnotesenginelite.domain.Note;

import java.util.List;

public interface NoteService {

    List<Note> getAllNotes();

    long addNote(Note newNote);

    Note getNote(long id);

    Note getNote(String text);

    long setNote(Note note);

    long deleteNote(long id);

}
