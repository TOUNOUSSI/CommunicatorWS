package com.gmart.api.messages.core.requests;

import lombok.Data;

@Data
public class SignInRequest {

	String username;
	String password;
}
