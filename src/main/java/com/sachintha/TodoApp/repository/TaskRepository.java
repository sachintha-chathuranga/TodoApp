package com.sachintha.TodoApp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sachintha.TodoApp.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
	Page<Task> findByUserId(Long userId, Pageable pageable);

	@Query("SELECT t FROM Task t WHERE t.user.id = :userId AND "
			+ "(LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%')) )")
	Page<Task> searchTasksByUserIdAndKeyword(@Param("userId") Long userId, @Param("keyword") String keyword,
			Pageable pageable);
}
