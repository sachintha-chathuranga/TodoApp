package com.sachintha.TodoApp.model;

import java.time.LocalDateTime;

import com.sachintha.TodoApp.dto.TaskPriority;
import com.sachintha.TodoApp.dto.TaskStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@Table(name = "tasks")
@AllArgsConstructor
@NoArgsConstructor
public class Task {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String description;
	private LocalDateTime dueDate;

	private TaskPriority priority;

	private TaskStatus status;

	@ManyToOne()
	@JoinColumn(name = "userId")
	private User user;

	public Task(Long id, String description, LocalDateTime dueDate, TaskPriority priority, TaskStatus status) {
		this.id = id;
		this.description = description;
		this.dueDate = dueDate;
		this.priority = priority;
		this.status = status;
	}
}
