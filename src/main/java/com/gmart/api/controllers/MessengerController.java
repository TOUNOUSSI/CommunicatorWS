package com.gmart.api.controllers;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("messenger")
@CrossOrigin(origins = { "http://localhost:4200", "https://gmart-front.herokuapp.com" }, maxAge = 5000)
@Slf4j
public class MessengerController {

	@Value("${gmart.ws.core.url}")
	private String url;

	@Value("${gmart.ws.core.uri.messenger.add-friend}")
	private String uriMessengerAddFriend;

	@Value("${gmart.ws.core.uri.messenger.get-friend-list}")
	private String uriMessengerGetFriendList;

	@GetMapping("/myfriends")
	@ResponseBody
	public ResponseEntity<List> getFriendList(HttpServletRequest request) {
		RestTemplate rt = new RestTemplate();
		rt.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		rt.getMessageConverters().add(new StringHttpMessageConverter());
		// create headers
		HttpHeaders headers = new HttpHeaders();

		// set `Content-Type` and `Accept` headers
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		log.info("myFriends jwt :" + request.getHeader("Token") + ", " + request.getHeader("token"));
		// example of custom header
		headers.set("token", request.getHeader("token"));
		HttpEntity rqst = new HttpEntity(headers);

		rt.getForObject(url + uriMessengerGetFriendList, List.class);
		ResponseEntity<List> friends = rt.exchange(url + uriMessengerGetFriendList, HttpMethod.GET, rqst, List.class,
				1);
		return friends;
	}
}
