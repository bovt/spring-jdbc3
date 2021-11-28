package ru.bvt.jdbcnotesenginelite.shell;

import lombok.AllArgsConstructor;
import org.springframework.shell.standard.*;
import ru.bvt.jdbcnotesenginelite.domain.Note;
import ru.bvt.jdbcnotesenginelite.service.AuthorService;
import ru.bvt.jdbcnotesenginelite.service.NoteService;

import java.util.ArrayList;
import java.util.List;

@ShellComponent
@AllArgsConstructor
@ShellCommandGroup("Note")
public class NoteShell {
    private final NoteService noteService;

    @ShellMethod(value = "Create note", key = {"c", "create"})
    public String addNote(
            @ShellOption(defaultValue = "AnyText") String text) {
        long id = noteService.addNote(new Note(text));
        if (id == 0) {
            return String.valueOf("Ошибка создания заметки");
        }
        return String.format("Создана заметка с номером: %s", id);
    }

    @ShellMethod(value = "List notes", key = {"l", "list"})
    public List<String> listNote() {
        List<Note> noteList = noteService.getAllNotes();
        List<String> result = new ArrayList<>();
        if (noteList != null) {
            for (Note note : noteList) {
                result.add("NoteId = " + note.getId() + "   Text = " + note.getText());
                result.add("  Author = " + note.getAuthor().getName() + "   Category = " + note.getCategory().getName());
            }
        }
        return result;
    }

    @ShellMethod(value = "Read note", key = {"r", "read"})
    public String readNote(
            @ShellOption(defaultValue = "1") Long id) {
        if (id == 0) {
            return String.valueOf("Ошибка чтения заметки");
        }
        Note note = noteService.getNote(id);

        if (note == null) {
            return String.valueOf("Ошибка чтения заметки");
        }
        return new String("NoteId = " + note.getId() + "   Text = " + note.getText());
    }

    @ShellMethod(value = "Update note", key = {"u", "update"})
    public String updateNote(
            @ShellOption(defaultValue = "1") Long id,
            @ShellOption(defaultValue = "new text") String newText) {

        if (id == 0) {
            return String.valueOf("Ошибка записи заметки");
        }

        Note note = new Note(newText);
        note.setId(id);

        id = noteService.setNote(note);
        if (id == 0) {
            return String.valueOf("Ошибка записи заметки");
        }
        return new String("Запись заметки c id = " + id + " произведена.");
    }


    @ShellMethod(value = "Delete note", key = {"d", "delete"})
    public String deleteNote(
            @ShellOption Long id) {
        if (id == 0) {
            return String.valueOf("Ошибка удаления заметки");
        }
        id = noteService.deleteNote(id);
        if (id == 0) {
            return String.valueOf("Ошибка удаления заметки");
        }
        return new String("Удаление заметки c id = " + id + " произведено.");
    }

}
