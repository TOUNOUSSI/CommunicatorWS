package com.gmart.api.controllers.core;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.gmart.common.enums.core.LoginStatus;
import com.gmart.common.enums.core.SignUpStatus;
import com.gmart.common.messages.core.requests.SignInRequest;
import com.gmart.common.messages.core.requests.SignUpRequest;
import com.gmart.common.messages.core.responses.CustomError;
import com.gmart.common.messages.core.responses.SignInResponse;
import com.gmart.common.messages.core.responses.SignUpResponse;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("gmartws-core-auth")
@CrossOrigin(origins = { "http://localhost:4200", "https://gmart-front.herokuapp.com" }, maxAge = 5000)
@Slf4j
public class CoreAuthController {

	@Value("${gmart.ws.core.url}")
	private String url;

	@Value("${gmart.ws.core.uri.signin}")
	private String siginInURI;

	@Value("${gmart.ws.core.uri.signup}")
	private String signupURI;
	
	@Value("${gmart.ws.core.uri.signout}")
	private String signoutURI;

	@PostMapping("/authenticate")
	@ResponseBody
	public ResponseEntity<?> sigin(@RequestBody @Valid SignInRequest signInRequest) {
		log.info("Starting REST Client : " + url);

		try {
			log.info(signInRequest.toString());

			RestTemplate rt = new RestTemplate();
			HttpEntity<SignInRequest> entity = new HttpEntity<SignInRequest>(signInRequest)	;
			SignInResponse signInResponse = rt.exchange(url + siginInURI, HttpMethod.POST, entity, SignInResponse.class).getBody();
			if (signInResponse != null && signInResponse.getError() == null) {
				log.info("User has been found ");
				log.info(signInResponse.getAuthenticatedUser().toString());
				log.info("End REST Client!!!!");
				return ResponseEntity.status(HttpStatus.OK).body(signInResponse);
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
			HttpEntity<SignUpRequest> entity = new HttpEntity<>(signUpRequest)	;
			SignUpResponse signUpResponse = rt.exchange(url + signupURI, HttpMethod.POST, entity, SignUpResponse.class).getBody();

			if (signUpResponse != null && signUpResponse.getError() == null) {
				log.info("User has been created ");
				return ResponseEntity.status(HttpStatus.OK).body(signUpResponse);
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
	
	
	@GetMapping("/logout")
	public ResponseEntity<Boolean> signout(HttpServletRequest request) {
		RestTemplate rt = null;
		log.info("Starting getFriendList");
		try {
			rt = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.set("token", request.getHeader("token"));
			// example of custom header
			HttpEntity<?> entity = new HttpEntity<Object>(headers);
		
				return ResponseEntity.status(HttpStatus.OK).body(rt.exchange(url + signoutURI, HttpMethod.GET, entity,
						Boolean.class).getBody());
			
		} catch (Exception e) {
			log.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
		}

	}
}
