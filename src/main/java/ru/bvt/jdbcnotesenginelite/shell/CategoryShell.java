package ru.bvt.jdbcnotesenginelite.shell;

import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.bvt.jdbcnotesenginelite.domain.Category;
import ru.bvt.jdbcnotesenginelite.service.CategoryService;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@ShellComponent
@ShellCommandGroup("Category")
public class CategoryShell {
    private final CategoryService categoryService;

    @ShellMethod(value = "Add category", key = {"ac", "add-category"})
    public String addCategory(
            @ShellOption String name) {
        long id = categoryService.addCategory(new Category(name));
        if (id == 0) {
            return String.valueOf("Ошибка создания категории");
        }
        return String.format("Создана категория с id: %s", id);
    }

    @ShellMethod(value = "Update category", key = {"uc", "update-category"})
    public String updateCategory(
            @ShellOption Long id,
            @ShellOption String name) {

        return categoryService.setCategory(new Category(id, name))
                ? new String("Запись карточки категории c id = " + id + " произведена.")
                : new String("Ошибка изменения карточки категории c id = " + id);
    }

    @ShellMethod(value = "List categories", key = {"lc", "list-categories"})
    public List<String> listCategories() {
        List<Category> categoryList = categoryService.getAllCategories();
        List<String> result = new ArrayList<>();
        if (categoryList != null) {
            for (Category category : categoryList) {
                result.add("CategoryId = " + category.getId() + "   Name = " + category.getName());
            }
        }
        return result;
    }

    @ShellMethod(value = "Read category", key = {"rc", "read-category"})
    public String readNote(
            @ShellOption(defaultValue = "1") Long id) {
        if (id == 0) {
            return String.valueOf("Ошибка чтения категории");
        }
        Category category = categoryService.getCategory(id);

        if (category == null) {
            return String.valueOf("Ошибка чтения категории");
        }
        return new String("CategoryId = " + category.getId() + "   Name = " + category.getName());
    }

}
