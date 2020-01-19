package com.gmart.api.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/chat")
@Slf4j
public class GMARTChatAPIController {

	public void sendMessage() {
		log.info("Sending message started ...");

		log.info("Message sent");
	}
}
