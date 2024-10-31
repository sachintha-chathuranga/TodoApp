package com.sachintha.TodoApp.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

	public Page<TaskDto> getTasks(int page, int size) {
		User user = userRepository.findById(Long.valueOf("1"))
				.orElseThrow(() -> new CustomException("User does not exists!", HttpStatus.NOT_FOUND));
		Pageable pageable = PageRequest.of(page, size);
		Page<Task> taskPage = taskRepository.findByUserId(user.getId(), pageable);
		Page<TaskDto> taskDtoPage = taskPage.map(TaskMapper::mapToTaskDto);
		return taskDtoPage;
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
