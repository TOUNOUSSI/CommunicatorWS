package com.gmart.api.messages.core.responses.entities;

import com.gmart.api.messages.core.responses.enums.SignUpStatus;

import lombok.Data;


@Data
public class SignUpResponse {
	SignUpStatus signUpStatus;
	CustomError error;
}
