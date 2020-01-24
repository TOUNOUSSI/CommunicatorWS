package com.gmart.api.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.gmart.api.messages.core.requests.SignInRequest;
import com.gmart.api.messages.core.requests.SignUpRequest;
import com.gmart.api.messages.core.responses.entities.CustomError;
import com.gmart.api.messages.core.responses.entities.SignInResponse;
import com.gmart.api.messages.core.responses.entities.SignUpResponse;
import com.gmart.api.messages.core.responses.enums.LoginStatus;
import com.gmart.api.messages.core.responses.enums.SignUpStatus;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("gmartws-api")
@CrossOrigin(origins = { "http://localhost:4200", "https://gmart-front.herokuapp.com" }, maxAge = 5000)
@Slf4j
public class GMARTCoreController {

	@Value("${gmart.ws.core.url}")
	private String url;

	@Value("${gmart.ws.core.uri.signin}")
	private String uriSignin;

	@Value("${gmart.ws.core.uri.signup}")
	private String uriSignup;


	@PostMapping("/authenticate")
	@ResponseBody
	public ResponseEntity<?> sigin(@RequestBody @Valid SignInRequest signInRequest) {
		log.info("Starting REST Client : " + url);

		try {
			log.info(signInRequest.toString());

			RestTemplate rt = new RestTemplate();
			rt.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
			rt.getMessageConverters().add(new StringHttpMessageConverter());

			SignInResponse signInResponse = rt.postForObject(url + uriSignin,
					signInRequest,
					SignInResponse.class);
			if (signInResponse != null && signInResponse.getError() == null) {
				log.info("User has been found ");
				log.info(signInResponse.getAuthenticatedUser().toString());
				log.info("End REST Client!!!!");
				return ResponseEntity.status(HttpStatus.ACCEPTED).body(signInResponse);
			} else {
				log.error("Communicator Exception | code : " + signInResponse.getError().getCode() + ", Message "
						+ signInResponse.getError().getMessage());
				throw new Exception("Communicator Exception | code : " + signInResponse.getError().getCode()
						+ ", Message " + signInResponse.getError().getMessage());

			}

		} catch (Exception e) {

			SignInResponse signInResponse = new SignInResponse();
			CustomError error = new CustomError();
			signInResponse.setLoginStatus(LoginStatus.NOT_AUTHENTICATED);

			error.setCode("500");

			error.setMessage(e.getMessage());

			signInResponse.setError(error);

			return ResponseEntity.status(Integer.parseInt(error.getCode())).body(signInResponse);

		}
	}

	@PostMapping("/register")
	@ResponseBody
	public ResponseEntity<?> signup(@RequestBody @Valid SignUpRequest signUpRequest) {
		log.info("Starting REST Client : " + url);

		try {
			log.info(signUpRequest.toString());

			RestTemplate rt = new RestTemplate();
			rt.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
			rt.getMessageConverters().add(new StringHttpMessageConverter());

			SignUpResponse signUpResponse = rt.postForObject(url + uriSignup, signUpRequest, SignUpResponse.class);
			if (signUpResponse != null && signUpResponse.getError() == null) {
				log.info("User has been created ");
				return ResponseEntity.status(HttpStatus.ACCEPTED).body(signUpResponse);
			} else {
				log.error("Communicator Exception | code : " + signUpResponse.getError().getCode() + ", Message "
						+ signUpResponse.getError().getMessage());
				throw new Exception(signUpResponse.getError().getMessage());

			}

		} catch (Exception e) {

			SignUpResponse signUpResponse = new SignUpResponse();
			CustomError error = new CustomError();
			signUpResponse.setSignUpStatus(SignUpStatus.NOT_CREATED);

			error.setCode("500");

			error.setMessage(e.getMessage());

			signUpResponse.setError(error);

			return ResponseEntity.status(Integer.parseInt(error.getCode())).body(signUpResponse);

		}
	}

}
