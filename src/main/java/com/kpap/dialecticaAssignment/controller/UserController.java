package com.kpap.dialecticaAssignment.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kpap.dialecticaAssignment.dto.UserDTO;
import com.kpap.dialecticaAssignment.model.User;
import com.kpap.dialecticaAssignment.repository.UserRepository;

/**
 * @author k.papageorgiou
 *
 */
@RestController
@RequestMapping("api/user")
public class UserController {

	private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	@Autowired
	private UserRepository userRepo;

	/**
	 * @param user
	 * @return
	 */
	@PostMapping("/add")
	public ResponseEntity<UserDTO> add(@RequestBody UserDTO user) {
		try {
			userRepo.save(new User(user.getFullName(), user.getEmail(), encoder.encode(user.getPassword())));
			return new ResponseEntity<>(user, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * @param userDto
	 * @param request
	 * @return
	 */
	@PutMapping("/update")
	public ResponseEntity<UserDTO> update(@RequestBody UserDTO userDto, HttpServletRequest request) {
		try {
			Principal principal = request.getUserPrincipal();
			User loggedinUser = userRepo.findByEmail(principal.getName()); // Get user from login details to avoid
																			// messing up other user data

			return userRepo.findById(loggedinUser.getId()).map(user -> {
				user.setEmail(userDto.getEmail());
				user.setFullName(userDto.getFullName());
				user.setPassword(encoder.encode(userDto.getPassword()));
				userRepo.save(user);
				return new ResponseEntity<>(userDto, HttpStatus.OK);
			}).orElseThrow(() -> new Exception("User not found"));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

}
