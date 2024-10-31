package com.sachintha.TodoApp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import com.sachintha.TodoApp.dto.TaskDto;
import com.sachintha.TodoApp.dto.TaskPriority;
import com.sachintha.TodoApp.dto.TaskStatus;
import com.sachintha.TodoApp.dto.TaskUpdateDto;
import com.sachintha.TodoApp.exception.CustomException;
import com.sachintha.TodoApp.service.TaskService;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TaskServiceTest {
	@Autowired
	private TaskService taskService;

	@Test
	@Order(1)
	void test1ForCreateTask() {
		assertThrows(NullPointerException.class, () -> {
			taskService.createTask(null);
		});
	}

	@Test
	@Order(2)
	void test2ForCreateTask() {
		TaskDto task = new TaskDto();
		task.setDescription("Testing task");
		task.setDueDate(LocalDateTime.parse("2024-11-02T19:28:27"));
		task.setPriority(TaskPriority.HIGH);
		task.setStatus(TaskStatus.TODO);
		TaskDto createdTask = taskService.createTask(task);
		assertEquals("Testing task", createdTask.getDescription());
	}

	@Test
	@Order(3)
	void test1ForGetTasks() {
		Page<TaskDto> result = taskService.getTasks("0", "1", "dueDate", "asc", "Testing");
		assertEquals(1, result.getTotalElements());
		assertEquals("Testing task", result.getContent().get(0).getDescription());
	}

	@Test
	@Order(4)
	void test1ForUpdateTask() {
		TaskUpdateDto task = new TaskUpdateDto();
		task.setId(Long.valueOf(0));
		assertThrows(CustomException.class, () -> {
			taskService.updateTask(task);
		});
	}

	@Test
	@Order(5)
	void test2ForUpdateTask() {
		Page<TaskDto> tasks = taskService.getTasks("0", "1", "dueDate", "asc", "Testing");
		TaskUpdateDto task = new TaskUpdateDto();
		task.setId(tasks.getContent().get(0).getId());
		task.setStatus(TaskStatus.COMPLETE);
		TaskDto updatedTask = taskService.updateTask(task);
		assertEquals("COMPLETE", updatedTask.getStatus());
	}

	@Test
	@Order(6)
	void testDeleteTask() {
		Page<TaskDto> tasks = taskService.getTasks("0", "1", "dueDate", "asc", "Testing");
		assertTrue(taskService.deleteTask(tasks.getContent().get(0).getId()));
	}

}
