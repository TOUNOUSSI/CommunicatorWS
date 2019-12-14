package com.gmart.api.messages.core.requests;

import com.gmart.api.messages.core.requests.entities.SignIn;

import lombok.Data;

@Data
public class SignInRequest {

	private SignIn signIn;
	private String uri;
}
