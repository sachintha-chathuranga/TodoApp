package com.sachintha.TodoApp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.sachintha.TodoApp.dto.TaskDto;
import com.sachintha.TodoApp.dto.TaskUpdateDto;
import com.sachintha.TodoApp.exception.CustomException;
import com.sachintha.TodoApp.mapper.TaskMapper;
import com.sachintha.TodoApp.model.Task;
import com.sachintha.TodoApp.model.User;
import com.sachintha.TodoApp.repository.TaskRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {

	private final Logger logger = LoggerFactory.getLogger(TaskService.class);
	private final TaskRepository taskRepository;
	private final JwtService jwtService;

	public TaskDto createTask(TaskDto taskDto) {
		logger.trace("Invoking createTask method");
		try {
			if (taskDto == null) {
				throw new NullPointerException("Task cannot be null");
			}
			User user = jwtService.getUserFromJwt();
			Task newTask = TaskMapper.mapToTask(taskDto);
			newTask.setUser(user);
			taskRepository.save(newTask);
			logger.info("Task Created successfully in createTask method");
			return TaskMapper.mapToTaskDto(newTask);
		} catch (Exception e) {
			logger.trace("Error during createTask: {}", e.getMessage());
			throw e;
		}
	}

	public Page<TaskDto> getTasks(String page, String size, String sortField, String sortDirection, String keyword) {
		logger.trace("Invoking getTask method");
		try {
			User user = jwtService.getUserFromJwt();
			Sort sort = Sort.by(sortField);
			sort = sortDirection.equalsIgnoreCase("desc") ? sort.descending() : sort.ascending();

			Pageable pageable = PageRequest.of(Integer.valueOf(page), Integer.valueOf(size), sort);
			Page<Task> taskPage;
			if (keyword != null && !keyword.isEmpty()) {
				taskPage = taskRepository.searchTasksByUserIdAndKeyword(user.getId(), keyword, pageable);
			} else {
				taskPage = taskRepository.findByUserId(user.getId(), pageable);
			}
			Page<TaskDto> taskDtoPage = taskPage.map(TaskMapper::mapToTaskDto);
			logger.info("Get Tasks Action successfully in getTasks method");
			return taskDtoPage;
		} catch (Exception e) {
			logger.trace("Error during getTask: {}", e.getMessage());
			throw e;
		}
	}

	public TaskDto updateTask(TaskUpdateDto taskDto) {
		logger.trace("Invoking updateTask method");
		try {
			Task task = taskRepository.findById(taskDto.getId())
					.orElseThrow(() -> new CustomException("Task does not exists!", HttpStatus.NOT_FOUND));
			User taskOwner = task.getUser();
			User logginUser = jwtService.getUserFromJwt();
			if (!taskOwner.getId().equals(logginUser.getId())) {
				throw new CustomException("You are not allow to Update Other users task", HttpStatus.FORBIDDEN);
			}
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
			logger.info("Update Task successfully in updateTask method");
			return TaskMapper.mapToTaskDto(task);
		} catch (Exception e) {
			logger.trace("Error during updateTask: {}", e.getMessage());
			throw e;
		}
	}

	public boolean deleteTask(Long id) {
		logger.trace("Invoking deleteTask method");
		try {
			Task task = taskRepository.findById(id)
					.orElseThrow(() -> new CustomException("Task not found to delete", HttpStatus.NOT_FOUND));
			User taskOwner = task.getUser();
			User logginUser = jwtService.getUserFromJwt();
			if (!taskOwner.getId().equals(logginUser.getId())) {
				throw new CustomException("You are not allow to Delete Other users task", HttpStatus.FORBIDDEN);
			}
			taskRepository.deleteById(id);
			logger.info("Task deleted successfully in deleteTask method");
			return true;
		} catch (Exception e) {
			logger.trace("Error during deleteTask: {}", e.getMessage());
			throw e;
		}
	}
}
