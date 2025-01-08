package doubleview.todo;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class Storage {
    private final List<Todo> todos = new ArrayList<>();

    public List<Todo> getTodos() {
        return todos;
    }

    public void addTodo(Todo todo) {
        todos.add(todo);
    }

    public void updateTodo(long id, Todo updatedTodo) {
        Optional<Todo> todoOptional = todos.stream().filter(todo -> todo.getId() == id).findFirst();
        todoOptional.ifPresent(todo -> {
            todo.setTitle(updatedTodo.getTitle());
            todo.setCompleted(updatedTodo.isCompleted());
        });
    }
}