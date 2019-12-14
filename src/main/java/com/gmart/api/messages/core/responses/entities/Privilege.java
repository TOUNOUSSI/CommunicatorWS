package com.gmart.api.messages.core.responses.entities;

import java.io.Serializable;

import lombok.Data;

@Data
public class Privilege implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;

}
