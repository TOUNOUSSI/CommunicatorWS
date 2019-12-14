package com.gmart.api.controllers;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.gmart.api.messages.core.requests.SignInRequest;
import com.gmart.api.messages.core.responses.entities.CustomError;
import com.gmart.api.messages.core.responses.entities.SignInResponse;
import com.gmart.api.messages.core.responses.enums.LoginStatus;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api")
@Slf4j
public class GMARTCoreController {

	@PostMapping("/gmartws/api/authenticate")
	public ResponseEntity<?> send(@RequestBody @Valid SignInRequest signInRequest) {
		log.info("Starting REST Client!!!!");

		try {
			log.info(signInRequest.toString());

			RestTemplate rt = new RestTemplate();
			rt.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
			rt.getMessageConverters().add(new StringHttpMessageConverter());

			SignInResponse signInResponse = rt.postForObject(signInRequest.getUri(), signInRequest.getSignIn(),
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

}
