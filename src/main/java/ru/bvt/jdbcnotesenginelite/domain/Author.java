package ru.bvt.jdbcnotesenginelite.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Author {
    private long id;
    private String name;
    private String password;

    public Author(String name, String password) {
        this.id = 0;
        this.name = name;
        this.password = password;
    }
}
