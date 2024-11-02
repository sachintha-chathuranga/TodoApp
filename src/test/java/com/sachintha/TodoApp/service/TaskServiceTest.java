package com.sachintha.TodoApp.service;

import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.sachintha.TodoApp.dto.TaskDto;
import com.sachintha.TodoApp.dto.TaskPriority;
import com.sachintha.TodoApp.dto.TaskStatus;
import com.sachintha.TodoApp.dto.TaskUpdateDto;
import com.sachintha.TodoApp.dto.UserRole;
import com.sachintha.TodoApp.exception.CustomException;
import com.sachintha.TodoApp.mapper.TaskMapper;
import com.sachintha.TodoApp.mapper.UserMapper;
import com.sachintha.TodoApp.model.Task;
import com.sachintha.TodoApp.model.User;
import com.sachintha.TodoApp.repository.TaskRepository;
import com.sachintha.TodoApp.repository.UserRepository;
import com.sachintha.TodoApp.service.JwtService;
import com.sachintha.TodoApp.service.TaskService;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

	@Mock
	private TaskRepository taskRepository;
	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private TaskService taskService;

	@Mock
	private JwtService jwtService;

	private User user;
	private User user2;
	private Task task;
	private Task task2;
	private TaskDto taskDto;
	private TaskUpdateDto taskUpdateDto;
	List<Task> taskList = new ArrayList<>();
	private Page<Task> tasks;

	@BeforeEach
	void init() {
		user = User.builder().id(Long.valueOf(1)).email("test@gmail.com").password("123abc").role(UserRole.USER)
				.build();
		user2 = User.builder().id(Long.valueOf(2)).email("test2@gmail.com").password("123abc").role(UserRole.USER)
				.build();
		UserMapper.mapToUserDto(user);
		task = Task.builder().id(Long.valueOf(1)).description("testing application")
				.dueDate(LocalDateTime.parse("2024-11-02T19:28:27")).priority(TaskPriority.NORMAL)
				.status(TaskStatus.TODO).user(user).build();
		task2 = Task.builder().id(Long.valueOf(2)).description("testing application 2")
				.dueDate(LocalDateTime.parse("2024-11-02T19:28:27")).priority(TaskPriority.LOW).status(TaskStatus.TODO)
				.user(user2).build();
		taskDto = TaskMapper.mapToTaskDto(task);
		taskUpdateDto = TaskUpdateDto.builder().description("updated description").priority(TaskPriority.HIGH)
				.status(TaskStatus.INPROGRESS).build();

		taskList.add(task);
		taskList.add(task2);

		Pageable pageable = PageRequest.of(0, 10);

		tasks = new PageImpl<>(taskList, pageable, taskList.size());
	}

	@Test
	void createTask_ReturnDto() {
		when(jwtService.getUserFromJwt()).thenReturn(user);
		when(taskRepository.save(Mockito.any(Task.class))).thenReturn(task);

		TaskDto createdTask = taskService.createTask(taskDto);
		Assertions.assertNotNull(createdTask);
		Assertions.assertEquals("testing application", createdTask.getDescription());
	}

	@Test
	void getTasks_ReturnPageableTaskDto() {
		when(jwtService.getUserFromJwt()).thenReturn(user);
		when(taskRepository.searchTasksByUserIdAndKeyword(Mockito.anyLong(), Mockito.anyString(),
				Mockito.any(Pageable.class))).thenReturn(tasks);

		Page<TaskDto> result = taskService.getTasks("0", "10", "dueDate", "asc", "Testing");
		Assertions.assertNotNull(result);
		Assertions.assertEquals(2, result.getNumberOfElements());
		Assertions.assertEquals("testing application", result.getContent().get(0).getDescription());
	}

	@Test
	void updateTask_ReturnCustomException() {
		when(jwtService.getUserFromJwt()).thenReturn(user2);
		when(taskRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(task));

		Assertions.assertThrows(CustomException.class, () -> {
			taskService.updateTask(task.getId(), taskUpdateDto);
		});
	}

	@Test
	void updateTask_ReturnDto() {
		when(jwtService.getUserFromJwt()).thenReturn(user);
		when(taskRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(task));
		when(taskRepository.save(Mockito.any(Task.class))).thenReturn(task);

		TaskDto updatedTask = taskService.updateTask(taskDto.getId(), taskUpdateDto);

		Assertions.assertEquals(TaskStatus.INPROGRESS, updatedTask.getStatus());
	}

	@Test
	void testDeleteTask() {
		when(jwtService.getUserFromJwt()).thenReturn(user);
		when(taskRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(task));
		Assertions.assertTrue(taskService.deleteTask(task.getId()));
	}

}
