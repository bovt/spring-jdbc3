package ru.bvt.jdbcnotesenginelite.shell;

import lombok.AllArgsConstructor;
import org.springframework.shell.standard.*;
import ru.bvt.jdbcnotesenginelite.domain.Author;
import ru.bvt.jdbcnotesenginelite.service.AuthorService;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@ShellComponent
@ShellCommandGroup("Author")
public class AuthorShell {
    private final AuthorService authorService;

    @ShellMethod(value = "Add author", key = {"aa", "add-author"})
    public String addAuthor(
            @ShellOption String name,
            @ShellOption(defaultValue = "AnyPassword") String password) {
        long id = authorService.addAuthor(new Author(name, password));
        if (id == 0) {
            return String.valueOf("Ошибка создания автора");
        }
        return String.format("Создан автор с id: %s", id);
    }

    @ShellMethod(value = "Update author", key = {"ua", "update-author"})
    public String updateAuthor(
            @ShellOption Long id,
            @ShellOption String name,
            @ShellOption(defaultValue = "P@s$w0rD") String password) {

        return authorService.setAuthor(new Author(id, name, password))
                ? new String("Запись карточки автора c id = " + id + " произведена.")
                : new String("Ошибка изменения карточки автора c id = " + id);
    }

    @ShellMethod(value = "List authors", key = {"la", "list-authors"})
    public List<String> listAuthors() {
        List<Author> authorList = authorService.getAllAuthors();
        List<String> result = new ArrayList<>();
        if (authorList != null) {
            for (Author author : authorList) {
                result.add("AuthorId = " + author.getId() + "   Name = " + author.getName() + "   Password = " + author.getPassword());
            }
        }
        return result;
    }

    @ShellMethod(value = "Read author", key = {"ra", "read-author"})
    public String readNote(
            @ShellOption(defaultValue = "1") Long id) {
        if (id == 0) {
            return String.valueOf("Ошибка чтения автора");
        }
        Author author = authorService.getAuthor(id);

        if (author == null) {
            return String.valueOf("Ошибка чтения автора");
        }
        return new String("AuthorId = " + author.getId() + "   Name = " + author.getName() + "   Password = " + author.getPassword());
    }


}
