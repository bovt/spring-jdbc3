package ru.bvt.jdbcnotesenginelite.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Note {

    @Getter
    private long id;

    @Getter
    private String text;
    private Author author;
    private Category category;

    public Note(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", name='" + text + '\'' +
                '}';
    }

}
