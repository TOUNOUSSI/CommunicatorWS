package com.gmart.api.messages.core.requests.entities;

import lombok.Data;

@Data
public class SignUp {
	private String username;
	private String firstname;
	private String lastname;
	private String phone;
	private String email;
	private String password;
	private String passwordConfirm;
}
