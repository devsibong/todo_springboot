package com.devsibong.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devsibong.demo.dto.ResponseDTO;
import com.devsibong.demo.dto.UserDTO;
import com.devsibong.demo.model.UserEntity;
import com.devsibong.demo.security.TokenProvider;
import com.devsibong.demo.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {
	private final UserService userService;
	private final TokenProvider tokenProvider;
	private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
		try {
			UserEntity user = UserEntity.builder()
					.email(userDTO.getEmail())
					.username(userDTO.getUsername())
					.password(userDTO.getPassword())
					.build();
			UserEntity registeredUser = userService.create(user);
			UserDTO reponseUserDTO = UserDTO.builder()
					.email(registeredUser.getEmail())
					.id(registeredUser.getId())
					.username(registeredUser.getUsername())
					.build();
			return ResponseEntity.ok().body(reponseUserDTO);
		} catch(Exception e) {
			e.printStackTrace();
			ResponseDTO<UserDTO> responseDTO = ResponseDTO.<UserDTO>builder().error(e.getMessage()).build();
			return ResponseEntity.badRequest().body(responseDTO);
		}
	}
	
	@PostMapping("/signin")
	public ResponseEntity<?> authenticate(@RequestBody UserDTO userDTO) {
		UserEntity user = userService.getByCredentials(userDTO.getEmail(), userDTO.getPassword(), passwordEncoder);
		if(user != null) {
			final String token = tokenProvider.create(user);
			final UserDTO responseUserDTO = UserDTO.builder()
					.email(user.getEmail())
					.id(user.getId())
					.token(token)
					.build();
			return ResponseEntity.ok().body(responseUserDTO);
		} else {
			ResponseDTO<UserDTO> responseDTO = ResponseDTO.<UserDTO>builder().error("login fail").build();
			return ResponseEntity.badRequest().body(responseDTO);
		}
	}
}
