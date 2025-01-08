package doubleview.todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class Controller {

    @Autowired
    private Storage storage;

    @GetMapping
    public List<Todo> getTodos() {
        return storage.getTodos();
    }

    @PostMapping
    public void addTodo(@RequestBody Todo todo) {
        storage.addTodo(todo);
    }

    @PutMapping("/{id}")
    public void updateTodo(@PathVariable long id, @RequestBody Todo todo) {
        storage.updateTodo(id, todo);
    }
}