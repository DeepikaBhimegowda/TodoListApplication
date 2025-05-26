package com.app.todoapp.taskControllers;

import com.app.todoapp.models.Todo;
import com.app.todoapp.models.User;
import com.app.todoapp.repositories.TodoRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/todos")
public class TodoController {

    @Autowired
    private TodoRepository todoRepository;

    @GetMapping
    public String showTodos(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/";

        List<Todo> todos = todoRepository.findByUserId(user.getId());
        model.addAttribute("todos", todos);
        return "todos";
    }

    @PostMapping("/add")
    public String addTodo(@RequestParam String title,
                          @RequestParam String description,
                          @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dueDate,
                          HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/";

        Todo todo = new Todo();
        todo.setTitle(title);
        todo.setDescription(description);
        todo.setDueDate(dueDate);
        todo.setCompleted(false);
        todo.setUser(user);
        todoRepository.save(todo);

        return "redirect:/todos";
    }

    @PostMapping("/delete/{id}")
    public String deleteTodo(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/";

        Todo todo = todoRepository.findById(id).orElse(null);
        if (todo != null && todo.getUser().getId().equals(user.getId())) {
            todoRepository.deleteById(id);
        }
        return "redirect:/todos";
    }

    @PostMapping("/toggle/{id}")
    public String toggleComplete(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/";

        Todo todo = todoRepository.findById(id).orElse(null);
        if (todo != null && todo.getUser().getId().equals(user.getId())) {
            todo.setCompleted(!todo.isCompleted());
            todoRepository.save(todo);
        }
        return "redirect:/todos";
    }
}
