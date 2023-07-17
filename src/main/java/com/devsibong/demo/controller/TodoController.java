package com.devsibong.demo.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devsibong.demo.dto.ResponseDTO;
import com.devsibong.demo.dto.TodoDTO;
import com.devsibong.demo.model.TodoEntity;
import com.devsibong.demo.service.TodoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/todo")
public class TodoController {
	private final TodoService todoService;
	
	@PostMapping
	public ResponseEntity<?> createTodo(@RequestBody TodoDTO dto) {
		try {
			String tempUserId = "temp-user";
			TodoEntity entity = TodoDTO.toEntity(dto);
			entity.setId(null);
			entity.setUserId(tempUserId);
			List<TodoEntity> entities = todoService.create(entity);
			List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
//			System.out.println("POST : "+response);
			return ResponseEntity.ok().body(response);
		} catch(Exception e) {
			String error = e.getMessage();
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
			
			return ResponseEntity.badRequest().body(response);			
		}
	}
	
	@GetMapping
	public ResponseEntity<?> retrieveTodoList() {
		String tempUserId = "temp-user";
		List<TodoEntity> entities = todoService.retrieve(tempUserId);
		List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
		ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
//		System.out.println("GET : "+response);
		return ResponseEntity.ok().body(response);
	}
	
	@PutMapping
	public ResponseEntity<?> updateTodo(@RequestBody TodoDTO dto) {
		String tempUserId = "temp-user";
		TodoEntity entity = TodoDTO.toEntity(dto);
		entity.setUserId(tempUserId);
		List<TodoEntity> entities = todoService.update(entity);
		List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
		ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
//		System.out.println("PUT : "+response);
		return ResponseEntity.ok().body(response);
	}
	
	@DeleteMapping
	public ResponseEntity<?> deleteTodo(@RequestBody TodoDTO dto) {
		try {
			String tempUserId = "temp-user";
			TodoEntity entity = TodoDTO.toEntity(dto);
			entity.setUserId(tempUserId);
			List<TodoEntity> entities = todoService.delete(entity);
			List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
//			System.out.println("DELETE : "+response);
			return ResponseEntity.ok().body(response); 
		} catch(Exception e) {
			String error = e.getMessage();
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
			
			return ResponseEntity.badRequest().body(response);
		}
		
	}
}
