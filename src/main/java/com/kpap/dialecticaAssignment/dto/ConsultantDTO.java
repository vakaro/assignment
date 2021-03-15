package com.kpap.dialecticaAssignment.dto;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

public class ConsultantDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long id;
	@NotBlank
	private String fullName;
	@NotBlank
	private String email;
	private String description;
	
	public ConsultantDTO() {}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
