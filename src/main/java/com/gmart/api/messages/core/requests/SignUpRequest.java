package com.gmart.api.messages.core.requests;

import lombok.Data;

@Data
public class SignUpRequest {

	String firstname;
	String lastname;
	String phone;
	String email;
	String password;
	String passwordConfirm;
}
