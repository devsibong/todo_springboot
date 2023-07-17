package com.devsibong.demo.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.devsibong.demo.model.TodoEntity;

public interface TodoRepository extends JpaRepository<TodoEntity, String>{
	
	@Query("select t from TodoEntity t where t.userId = ?1")
	List<TodoEntity> findByUserId(String userId);
}
