package com.gmart.api.messages.core.responses.entities;

import com.gmart.api.messages.core.responses.enums.LoginStatus;

import lombok.Data;

@Data
public class SignInResponse {
	LoginStatus loginStatus;
	UserInfo authenticatedUser;
	String token;
	CustomError error;
}
