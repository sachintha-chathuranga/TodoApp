package com.sachintha.TodoApp.repository;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import com.sachintha.TodoApp.dto.TaskPriority;
import com.sachintha.TodoApp.dto.TaskStatus;
import com.sachintha.TodoApp.dto.UserRole;
import com.sachintha.TodoApp.model.Task;
import com.sachintha.TodoApp.model.User;
import com.sachintha.TodoApp.repository.TaskRepository;
import com.sachintha.TodoApp.repository.UserRepository;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@TestMethodOrder(OrderAnnotation.class)
@ActiveProfiles("test")
public class TaskRepositoryTest {

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private UserRepository userRepository;
	private User user;
	private Task task1;
	private Task task2;
	private Task task3;
	private Pageable pageable;

	@BeforeEach
	void init() {
		user = User.builder().id(Long.valueOf(1)).email("test@gmail.com").password("123abc").role(UserRole.USER)
				.build();
		task1 = Task.builder().id(Long.valueOf(1)).description("testing1 application")
				.dueDate(LocalDateTime.parse("2024-11-02T19:28:27")).priority(TaskPriority.NORMAL).user(user)
				.status(TaskStatus.TODO).build();

		task2 = Task.builder().id(Long.valueOf(2)).description("testing2 application")
				.dueDate(LocalDateTime.parse("2024-11-02T19:28:27")).priority(TaskPriority.HIGH).status(TaskStatus.TODO)
				.user(user).build();
		task3 = Task.builder().id(Long.valueOf(3)).description("TASK FOR TEST")
				.dueDate(LocalDateTime.parse("2024-11-02T19:28:27")).priority(TaskPriority.LOW).user(user)
				.status(TaskStatus.COMPLETE).build();
		pageable = PageRequest.of(0, 10);
		userRepository.save(user);
		taskRepository.save(task1);
		taskRepository.save(task2);
		taskRepository.save(task3);
	}

	@Test
	@Order(1)
	public void findTaskByUserId_ReturnMoreThenOneTasks() {

		Page<Task> tasks = taskRepository.findByUserId(user.getId(), pageable);
		Assertions.assertNotNull(tasks);
		Assertions.assertEquals(3, tasks.getNumberOfElements());
	}

	@Test
	@Order(2)
	public void searchTasksByUserIdAndKeyword_ReturnMoreThenOneTasks() {

		Page<Task> tasks = taskRepository.searchTasksByUserIdAndKeyword(user.getId(), "testing", pageable);
		Assertions.assertNotNull(tasks);
		Assertions.assertEquals(2, tasks.getNumberOfElements());
	}
}
