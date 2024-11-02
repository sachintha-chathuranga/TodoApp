package com.sachintha.TodoApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sachintha.TodoApp.dto.TaskDto;
import com.sachintha.TodoApp.dto.TaskUpdateDto;
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
	public ResponseEntity<Page<TaskDto>> getTasks(@RequestParam(defaultValue = "0") String page,
			@RequestParam(defaultValue = "10") String size, @RequestParam(defaultValue = "dueDate") String sortField,
			@RequestParam(defaultValue = "asc") String sortDirection, @RequestParam(defaultValue = "") String keyword) {
		Page<TaskDto> tasks = taskService.getTasks(page, size, sortField, sortDirection, keyword);
		return ResponseEntity.status(HttpStatus.OK).body(tasks);
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<TaskDto> updateTask(@PathVariable("id") Long taskId,
			@Valid @RequestBody TaskUpdateDto taskDto) {
		TaskDto task = taskService.updateTask(taskId, taskDto);
		return ResponseEntity.status(HttpStatus.OK).body(task);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteTask(@PathVariable("id") Long taskId) {
		taskService.deleteTask(taskId);
		return ResponseEntity.status(HttpStatus.OK).body("Task deleted successfully!");
	}
}
