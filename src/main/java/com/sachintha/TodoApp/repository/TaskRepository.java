package com.sachintha.TodoApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sachintha.TodoApp.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {

}