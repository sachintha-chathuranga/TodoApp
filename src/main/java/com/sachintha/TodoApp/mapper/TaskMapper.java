package com.sachintha.TodoApp.mapper;

import com.sachintha.TodoApp.dto.TaskDto;
import com.sachintha.TodoApp.model.Task;

public class TaskMapper {
	public static Task mapToTask(TaskDto taskDto) {
		if (taskDto == null) {
			return null;
		}
		Task task = new Task(taskDto.getId(), taskDto.getDescription(), taskDto.getDueDate(), taskDto.getPriority(),
				taskDto.getStatus());
		return task;
	}

	public static TaskDto mapToTaskDto(Task task) {
		if (task == null) {
			return null;
		}
		TaskDto taskDto = new TaskDto(task.getId(), task.getDescription(), task.getDueDate(), task.getPriority(),
				task.getStatus());
		return taskDto;
	}
}
