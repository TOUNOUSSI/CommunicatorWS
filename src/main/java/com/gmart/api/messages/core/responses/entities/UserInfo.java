package com.gmart.api.messages.core.responses.entities;

import java.util.Set;

import lombok.Data;

@Data
public class UserInfo {

	private String email;
	private String firstname;
	private Long id;
	private String lastname;
	private String phone;
	private Set<Role> roles;
	private String username;
}
