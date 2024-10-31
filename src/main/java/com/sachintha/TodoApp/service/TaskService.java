package com.sachintha.TodoApp.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.sachintha.TodoApp.dto.TaskDto;
import com.sachintha.TodoApp.exception.CustomException;
import com.sachintha.TodoApp.mapper.TaskMapper;
import com.sachintha.TodoApp.model.Task;
import com.sachintha.TodoApp.model.User;
import com.sachintha.TodoApp.repository.TaskRepository;
import com.sachintha.TodoApp.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {
	private final TaskRepository taskRepository;
	private final UserRepository userRepository;

	public void createTask(TaskDto taskDto) {
		User user = userRepository.findById(Long.valueOf("1"))
				.orElseThrow(() -> new CustomException("User does not exists!", HttpStatus.NOT_FOUND));
		Task newTask = TaskMapper.mapToTask(taskDto);
		newTask.setUser(user);
		taskRepository.save(newTask);
		return;
	}

	public List<TaskDto> getTasks() {
		User user = userRepository.findById(Long.valueOf("1"))
				.orElseThrow(() -> new CustomException("User does not exists!", HttpStatus.NOT_FOUND));
		List<TaskDto> taskList = user.getTasks().stream().map(t -> TaskMapper.mapToTaskDto(t)).toList();
		return taskList;
	}

	public TaskDto updateTask(TaskDto taskDto) {
		Task task = taskRepository.findById(taskDto.getId())
				.orElseThrow(() -> new CustomException("Task does not exists!", HttpStatus.NOT_FOUND));
		if (taskDto.getDescription() != null) {
			task.setDescription(taskDto.getDescription());
		}
		if (taskDto.getDueDate() != null) {
			task.setDueDate(taskDto.getDueDate());
		}
		if (taskDto.getPriority() != null) {
			task.setPriority(taskDto.getPriority());
		}
		if (taskDto.getStatus() != null) {
			task.setStatus(taskDto.getStatus());
		}
		taskRepository.save(task);
		return TaskMapper.mapToTaskDto(task);
	}

	public void deleteTask(Long id) {
		taskRepository.deleteById(id);
		return;
	}
}
