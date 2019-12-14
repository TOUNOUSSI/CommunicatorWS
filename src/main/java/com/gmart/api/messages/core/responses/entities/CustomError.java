package com.gmart.api.messages.core.responses.entities;

import lombok.Data;

@Data
public class CustomError {

	String code;
	String message;
}
