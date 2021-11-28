package ru.bvt.jdbcnotesenginelite.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bvt.jdbcnotesenginelite.domain.Category;
import ru.bvt.jdbcnotesenginelite.domain.Note;
import ru.bvt.jdbcnotesenginelite.repository.AuthorRepository;
import ru.bvt.jdbcnotesenginelite.repository.CategoryRepository;
import ru.bvt.jdbcnotesenginelite.repository.NoteRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class NoteServiceSimple implements NoteService {

    private final String defaultCategoryName;
    private final Long defaultAuthorId;
    private final NoteRepository repository;
    private final CategoryRepository categoryRepository;
    private final AuthorRepository authorRepository;

    @Autowired
    public NoteServiceSimple(@Value("${engine.defaults.category}") String defaultCategoryName, @Value("${engine.defaults.author}") Long defaultAuthorId, NoteRepository repository,
                             CategoryRepository categoryRepository, AuthorRepository authorRepository) {
        this.defaultCategoryName = defaultCategoryName;
        this.defaultAuthorId = defaultAuthorId;
        this.repository = repository;
        this.categoryRepository = categoryRepository;
        this.authorRepository = authorRepository;
    }

    @Transactional(readOnly = true)
    public List<Note> getAllNotes() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Note getNote(long id) {
        return repository.findById(id);
    }

    @Transactional(readOnly = true)
    public Note getNote(String text) {
        return repository.findByText(text);
    }

    @Transactional(readOnly = false)
    public long addNote(Note note) {
        note.setCategory(getCategoryFromHashtagedNote(note.getText()));
        note.setAuthor(authorRepository.findById(defaultAuthorId));
        note = repository.save(note);
        if (note == null) return 0;
        return note.getId();
    }

    @Transactional(readOnly = false)
    public long setNote(Note note) {
        Note resultNote = repository.findById(note.getId());
        if (resultNote == null) {
            return 0;
        }
        resultNote.setId(note.getId());
        resultNote.setText(note.getText());
        resultNote.setAuthor(authorRepository.findById(defaultAuthorId));
        resultNote.setCategory(getCategoryFromHashtagedNote(note.getText()));
        resultNote = repository.save(resultNote);
        return resultNote.getId();
    }

    @Transactional(readOnly = false)
    public long deleteNote(long id) {
        return repository.deleteById(id);
    }

    private Category getCategoryFromHashtagedNote(String noteTextWithCategoryHashtags) {
        List<Category> categoryList = new ArrayList<>();
        Category category = null;
        String currentString, substr = noteTextWithCategoryHashtags;
        if (substr.indexOf('#') != -1) {
            substr = substr.substring(substr.indexOf('#') + 1, substr.length());
            var nums = new int[]{
                    substr.indexOf(' ') > 0 ? substr.indexOf(' ') : substr.length(),
                    substr.indexOf('.') > 0 ? substr.indexOf('.') : substr.length(),
                    substr.indexOf(',') > 0 ? substr.indexOf(',') : substr.length(),
                    substr.indexOf(';') > 0 ? substr.indexOf(';') : substr.length(),
                    substr.length()};
            var min = Arrays.stream(nums).min();
            currentString = substr.substring(0, min.isPresent() ? min.getAsInt() : substr.length());
            if (currentString.length() > 0) {
                if (!categoryRepository.existsByName(currentString)) {
                    category = new Category(currentString);
                    categoryRepository.save(category);
                } else {
                    category = categoryRepository.findByName(currentString);
                }
            }
        } else {
            category = categoryRepository.findByName(defaultCategoryName);
        }
        return category;
    }

}
