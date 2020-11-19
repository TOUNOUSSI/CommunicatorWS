package com.gmart.api.controllers.core;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
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
@RequestMapping("gmartws-core-account")
@CrossOrigin(origins = { "http://localhost:4200", "https://gmart-front.herokuapp.com" }, maxAge = 5000)
@Slf4j
public class CoreAccountController {

	@Value("${gmart.ws.core.url}")
	private String url;

	@GetMapping("/all")
	@ResponseBody
	public ResponseEntity<?> getAccounts(HttpServletRequest request) {
		RestTemplate rt = null;
		log.info("Starting getAccounts");
		try {
			rt = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.set("token", request.getHeader("token"));
			// example of custom header
			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			return ResponseEntity.status(HttpStatus.ACCEPTED)
					.body(rt.exchange(url, HttpMethod.GET, entity, List.class));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());

		}
	}

}
