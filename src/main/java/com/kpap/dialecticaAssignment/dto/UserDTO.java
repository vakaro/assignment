package com.kpap.dialecticaAssignment.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class UserDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	@NotBlank
	private String fullName;
	@NotBlank
	private String email;
	@NotBlank
	@Size(min = 6, max = 12)
	private transient String password;
	
	public UserDTO() {}
	
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
