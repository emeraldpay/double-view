package doubleview.todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class Pages {

    @Autowired
    private Storage storage;

    @GetMapping("/")
    public ModelAndView index() {
        return new ModelAndView("App", "todos", storage.getTodos().stream().map(Todo::toJSON).toList());
    }
}
