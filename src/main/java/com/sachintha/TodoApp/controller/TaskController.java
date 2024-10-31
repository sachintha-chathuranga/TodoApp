package com.sachintha.TodoApp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sachintha.TodoApp.dto.TaskDto;
import com.sachintha.TodoApp.service.TaskService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/task")
@RequiredArgsConstructor
public class TaskController {
	@Autowired
	private final TaskService taskService;

	@PostMapping("/create")
	public ResponseEntity<String> createTask(@Valid @RequestBody TaskDto task) {
		taskService.createTask(task);
		return ResponseEntity.status(HttpStatus.CREATED).body("Task created successfully!");
	}

	@GetMapping("/getAll")
	public ResponseEntity<List<TaskDto>> getTasks() {
		List<TaskDto> tasks = taskService.getTasks();
		return ResponseEntity.status(HttpStatus.OK).body(tasks);
	}

	@PutMapping("/update")
	public ResponseEntity<TaskDto> updateTask(@RequestBody TaskDto taskDto) {
		TaskDto task = taskService.updateTask(taskDto);
		return ResponseEntity.status(HttpStatus.OK).body(task);
	}

	@DeleteMapping("/delete")
	public ResponseEntity<String> deleteTask(@RequestParam Long taskId) {
		taskService.deleteTask(taskId);
		return ResponseEntity.status(HttpStatus.OK).body("Task deleted successfully!");
	}
}
