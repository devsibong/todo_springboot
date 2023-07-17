package com.devsibong.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.devsibong.demo.model.TodoEntity;
import com.devsibong.demo.persistence.TodoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TodoService {
	private final TodoRepository todoRepository;
	
	private void validate(final TodoEntity entity) {
		if(entity == null) {
			throw new RuntimeException("Entity cannot be null");
		}
		
		if(entity.getUserId() == null) {
			throw new RuntimeException("Unknown user");		
		}
	}
	
	public List<TodoEntity> create(final TodoEntity entity) {
		validate(entity);
		
		todoRepository.save(entity);
		
		return todoRepository.findByUserId(entity.getUserId());
	}
	
	public List<TodoEntity> retrieve(final String userId){
		return todoRepository.findByUserId(userId);
	}
	
	public List<TodoEntity> update(final TodoEntity entity){
		validate(entity);
		final Optional<TodoEntity> original = todoRepository.findById(entity.getId());
		original.ifPresent(todo -> {
			todo.setTitle(entity.getTitle());
			todo.setDone(entity.isDone());
			todoRepository.save(todo);
		});
		
		return retrieve(entity.getUserId());
	}
	
	public List<TodoEntity> delete(final TodoEntity entity) {
		validate(entity);
		try {
			todoRepository.delete(entity);
		} catch(Exception e) {
			System.out.println(entity.getId()+"error");
			throw new RuntimeException("error");
		}
		return retrieve(entity.getUserId());
	}
}
