package ru.bvt.jdbcnotesenginelite;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.bvt.jdbcnotesenginelite.domain.Author;
import ru.bvt.jdbcnotesenginelite.domain.Category;
import ru.bvt.jdbcnotesenginelite.domain.Note;
import ru.bvt.jdbcnotesenginelite.repository.AuthorRepository;
import ru.bvt.jdbcnotesenginelite.repository.CategoryRepository;
import ru.bvt.jdbcnotesenginelite.repository.NoteRepository;
import ru.bvt.jdbcnotesenginelite.service.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("Сервис для работы со заметками ")
@ExtendWith(MockitoExtension.class)
public class JdbcnotesengineliteNoteServiceTest {
    @Mock
    private NoteRepository noteRepository;
    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private CategoryRepository categoryRepository;

    private NoteService noteService;
    private CategoryService categoryService;
    private AuthorService authorService;


    @BeforeEach
    void setUp() {
        noteService = new NoteServiceSimple("unclassified",Long.valueOf(1), noteRepository, categoryRepository, authorRepository);
        categoryService = new CategoryServiceSimple(categoryRepository);
        authorService = new AuthorServiceSimple(authorRepository);
        categoryService.addCategory(new Category("unclassified"));
    }

    @DisplayName("должен искать заметки по тексту заметок")
    @Test
    void getNoteByText() {
        given(noteRepository.findByText(any()))
                .willReturn(new Note("Test note"));

        assertThat(noteService.getNote("Test note"))
                .isNotNull();
    }

    @DisplayName("должен сохранять заметки")
    @Test
    public void addNote() {
        given(authorRepository.findById(1))
                .willReturn(new Author(1, "dfa", "dsf"));
        given(noteRepository.save(any()))
                .willReturn(new Note(1, "Test note 2", null, null));
        Note note = new Note("Test note");
        Long resultId = noteService.addNote(note);
        assertThat(resultId).isNotZero();
    }

}
