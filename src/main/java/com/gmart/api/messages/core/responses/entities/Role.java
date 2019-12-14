package com.gmart.api.messages.core.responses.entities;

import java.io.Serializable;
import java.util.List;

import com.gmart.api.messages.core.responses.enums.RoleName;

import lombok.Data;

@Data
public class Role implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private Long id;

	private RoleName name;

	private List<Privilege> privileges;

}
