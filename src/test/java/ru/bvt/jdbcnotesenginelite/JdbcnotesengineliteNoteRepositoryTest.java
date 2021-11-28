package ru.bvt.jdbcnotesenginelite;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import ru.bvt.jdbcnotesenginelite.repository.AuthorRepositoryJdbc;
import ru.bvt.jdbcnotesenginelite.repository.CategoryRepositoryJdbc;
import ru.bvt.jdbcnotesenginelite.repository.NoteRepositoryJdbc;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jdbc для работы со заметками ")
@JdbcTest
@Import({NoteRepositoryJdbc.class, AuthorRepositoryJdbc.class, CategoryRepositoryJdbc.class})
class JdbcnotesengineliteNoteRepositoryTest {

	private static final int EXPECTED_NUMBER_OF_NOTES = 2;

	@Autowired
	private NoteRepositoryJdbc noteRepository;

	@DisplayName("должен загружать список всех заметок с полной информацией о них")
	@Test
	void shouldReturnCorrectStudentsListWithAllInfo() {
		val notes = noteRepository.findAll();
		assertThat(notes).isNotNull().hasSize(EXPECTED_NUMBER_OF_NOTES)
				.allMatch(s -> !s.getText().equals(""))
				.allMatch(s -> s.getAuthor() != null)
				.allMatch(s -> s.getCategory() != null);
		notes.forEach(System.out::println);
	}



}
