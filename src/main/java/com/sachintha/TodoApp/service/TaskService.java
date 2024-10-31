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
import com.sachintha.TodoApp.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {

	private final Logger logger = LoggerFactory.getLogger(TaskService.class);
	private final TaskRepository taskRepository;
	private final UserRepository userRepository;

	public TaskDto createTask(TaskDto taskDto) {
		logger.info("Starting create new task");
		try {
			if (taskDto == null) {
				throw new NullPointerException("task cannot be null");
			}
			User user = userRepository.findById(Long.valueOf("1"))
					.orElseThrow(() -> new CustomException("User does not exists!", HttpStatus.NOT_FOUND));
			Task newTask = TaskMapper.mapToTask(taskDto);
			newTask.setUser(user);
			taskRepository.save(newTask);
			logger.info("Task Created successfully");
			return TaskMapper.mapToTaskDto(newTask);
		} catch (Exception e) {
			logger.error("error while create task", e);
			throw e;
		}
	}

	public Page<TaskDto> getTasks(String page, String size, String sortField, String sortDirection, String keyword) {
		logger.info("Starting get tasks");
		try {
			User user = userRepository.findById(Long.valueOf("1"))
					.orElseThrow(() -> new CustomException("User does not exists!", HttpStatus.NOT_FOUND));
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
			logger.info("Get All task successfully");
			return taskDtoPage;
		} catch (Exception e) {
			logger.error("error while geting task list", e);
			throw e;
		}
	}

	public TaskDto updateTask(TaskUpdateDto taskDto) {
		logger.info("Starting Update task");
		try {
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
		} catch (Exception e) {
			logger.error("error while updating task!", e);
			throw e;
		}
	}

	public boolean deleteTask(Long id) {
		logger.info("Start deleting task for the given id");
		try {
			taskRepository.deleteById(id);
			logger.info("Task deleted successfully");
			return true;
		} catch (Exception e) {
			logger.error("error while deleting task", e);
			throw e;
		}
	}
}
