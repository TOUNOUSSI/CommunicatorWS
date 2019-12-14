package com.gmart.api.messages.core.requests.entities;

import lombok.Data;

@Data
public class SignIn {
	String username;
	String password;
}
