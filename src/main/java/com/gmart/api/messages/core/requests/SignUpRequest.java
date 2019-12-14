package com.gmart.api.messages.core.requests;

import com.gmart.api.messages.core.requests.entities.SignUp;

import lombok.Data;

@Data
public class SignUpRequest {

	private SignUp signup;
	private String uri;
}
