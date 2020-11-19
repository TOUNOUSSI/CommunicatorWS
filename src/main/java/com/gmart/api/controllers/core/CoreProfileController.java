/*
 *  This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/3.0/deed.en_US">Creative Commons Attribution 3.0 Unported License</a>.
 *  Copyright © GMART, unpublished work. This computer program
 *  includes confidential, proprietary information and is a trade secret of GMART Inc.
 *  All use, disclosure, or reproduction is prohibited unless authorized
 *  in writing by TOUNOUSSI Youssef. All Rights Reserved.
 */
package com.gmart.api.controllers.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gmart.api.services.FileService;
import com.gmart.common.messages.core.ProfileDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author <a href="mailto:youssef.tounoussi@gmail.com">TOUNOUSSI Youssef</a>
 * @create 16 nov. 2020
 **/

@RestController
@RequestMapping("gmartws-core-profile")
@CrossOrigin(origins = { "http://localhost:4200", "https://gmart-front.herokuapp.com" }, maxAge = 5000)
@Slf4j
public class CoreProfileController {

	@Value("${gmart.ws.core.url}")
	private String url;

	@Value("${gmart.ws.core.uri.profile.find-profile}")
	private String findProfileURI;

	@Value("${gmart.ws.core.uri.profile.find-my-profile}")
	private String getMyProfileURI;

	@Value("${gmart.ws.core.uri.profile.update-profile-cover}")
	private String updateMyProfileCoverURI;

	@Value("${gmart.ws.core.uri.profile.update-profile-picture}")
	private String updateMyProfilePictureURI;

	@Autowired
	FileService fileService;
	
	@GetMapping("/find-profile/{pseudoname}")
	@ResponseBody
	public ResponseEntity<ProfileDTO> findProfile(@PathVariable("pseudoname") String pseudoname,
			HttpServletRequest request) {
		ProfileDTO profile = new ProfileDTO();
		RestTemplate rt = null;
		log.info("Find Profile End-point :: Started");
		try {
			rt = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.set("Token", request.getHeader("token"));
			// example of custom header
			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			profile = rt.exchange(url + findProfileURI + pseudoname, HttpMethod.GET, entity, ProfileDTO.class)
					.getBody();

		} catch (Exception e) {
			log.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(profile);
		}

		return ResponseEntity.status(HttpStatus.OK).body(profile);
	}

	@GetMapping("/find-my-profile")
	@ResponseBody
	public ResponseEntity<ProfileDTO> getMyProfile(HttpServletRequest request) {
		ProfileDTO myProfile = new ProfileDTO();
		RestTemplate rt = null;
		log.info("Get My Profile End-point :: Started");
		try {
			rt = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.set("Token", request.getHeader("token"));
			// example of custom header
			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			myProfile = rt.exchange(url + getMyProfileURI, HttpMethod.GET, entity, ProfileDTO.class).getBody();

		} catch (Exception e) {
			log.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(myProfile);
		}

		return ResponseEntity.status(HttpStatus.OK).body(myProfile);
	}

	@PostMapping("/update-profile-cover")
	@ResponseBody
	public ResponseEntity<?> updateProfileCover(@RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttributes, HttpServletRequest request) {
		RestTemplate rt = null;
		log.info("Update Profile Cover End-point :: Started");
		try {
			rt = new RestTemplate();
			MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
			body.add("file", new FileSystemResource(fileService.convertMultiPartFileToIOFile(file)));
			HttpHeaders headers = new HttpHeaders();
			headers.set("Token", request.getHeader("token"));
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			// setting both body and headers into HttpEntity
			HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);

			return ResponseEntity.status(HttpStatus.OK)
					.body(rt.exchange(url + updateMyProfileCoverURI, HttpMethod.POST, entity, Object.class).getBody());

		} catch (Exception e) {
			log.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
		}
	}

	@PostMapping("/update-profile-picture")
	@ResponseBody
	public ResponseEntity<?> updateProfilePicture(@RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttributes, HttpServletRequest request) {
		RestTemplate rt = null;
		log.info("Update Profile Picture End-point :: Started");
		try {
			rt = new RestTemplate();
			MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
			body.add("file", new FileSystemResource(fileService.convertMultiPartFileToIOFile(file)));
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);

			headers.set("Token", request.getHeader("token"));
			// setting both body and headers into HttpEntity
			HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);

			return ResponseEntity.status(HttpStatus.OK).body(
					rt.exchange(url + updateMyProfilePictureURI, HttpMethod.POST, entity, Object.class).getBody());

		} catch (Exception e) {
			log.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
		}
	}
	
	
}
