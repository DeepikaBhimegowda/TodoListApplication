package com.app.todoapp.models;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Data
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private boolean completed;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;
}
