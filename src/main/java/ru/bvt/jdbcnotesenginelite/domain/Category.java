package ru.bvt.jdbcnotesenginelite.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Category {
    private long id;
    private String name;

    public Category(String name) {
        this.name = name;
        this.id = 0;
    }

    ;
}
