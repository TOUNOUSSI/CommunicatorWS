package com.gmart.api.controllers.core;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.gmart.common.messages.core.UserInfoDTO;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("gmartws-core-friend")
@CrossOrigin(origins = { "http://localhost:4200", "https://gmart-front.herokuapp.com" }, maxAge = 5000)
@Slf4j
public class CoreFriendController {

	@Value("${gmart.ws.core.url}")
	private String url;

	@Value("${gmart.ws.core.uri.friend.add-new-friend}")
	private String addNewFriendURI;

	@Value("${gmart.ws.core.uri.friend.myfriends}")
	private String myFriendsURI;

	@Value("${gmart.ws.core.uri.friend.find-friends}")
	private String findFriendsURI;

	@Value("${gmart.ws.core.uri.friend.find-friend}")
	private String findFriendURI;
	
	@Value("${gmart.ws.core.uri.friend.are-we-already-friends}")
	private String areWeAlreadyFriendsURI;

	@GetMapping("/add-new-friend/{pseudoname}")
	@ResponseBody
	public ResponseEntity<Boolean> addNewFriend(@PathVariable("pseudoname") String pseudoname,
			HttpServletRequest request) {
		RestTemplate rt = null;
		Boolean friendHasBeenAdded = false;
		log.debug("Add user to friend list end-point:: Started :: for Pseudoname" + pseudoname);
		try {
			rt = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.set("token", request.getHeader("token"));
			// example of custom header
			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			friendHasBeenAdded = rt.exchange(url + addNewFriendURI + pseudoname, HttpMethod.PUT, entity, Boolean.class)
					.getBody();

		} catch (Exception e) {
			log.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
		}

		return ResponseEntity.status(HttpStatus.OK).body(friendHasBeenAdded);
	}

	@GetMapping("/myfriends")
	@ResponseBody
	public ResponseEntity<List<UserInfoDTO>> getFriendList(HttpServletRequest request) {
		RestTemplate rt = null;
		log.info("Starting getFriendList");
		try {
			rt = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.set("token", request.getHeader("token"));
			// example of custom header
			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			List<UserInfoDTO> friends = rt.exchange(url + myFriendsURI, HttpMethod.GET, entity,
					new ParameterizedTypeReference<List<UserInfoDTO>>() {
					}).getBody();
			if (!CollectionUtils.isEmpty(friends)) {
				return ResponseEntity.status(HttpStatus.OK).body(friends);
			} else {
				return ResponseEntity.status(HttpStatus.OK).body(new ArrayList<>());
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
		}

	}

	@GetMapping("/find-friends/{criteria}")
	@ResponseBody
	public ResponseEntity<List<?>> getAllSearchAccountMatches(@PathVariable String criteria,
			HttpServletRequest request) {
		RestTemplate rt = null;
		log.info("GetAllSearchAccountMatches >> Started");
		log.debug("GetAllSearchAccountMatches :: Started :: criteria :" + criteria);
		try {
			rt = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.set("token", request.getHeader("token"));
			// example of custom header
			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			List<UserInfoDTO> matches = rt.exchange(url + findFriendsURI, HttpMethod.GET, entity,
					new ParameterizedTypeReference<List<UserInfoDTO>>() {
					}).getBody();
			if (!CollectionUtils.isEmpty(matches)) {
				return ResponseEntity.status(HttpStatus.OK).body(matches);
			} else {
				return ResponseEntity.status(HttpStatus.OK).body(new ArrayList<>());
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
		}

	}

	@GetMapping("/are-we-already-friends/{pseudoname}")
	@ResponseBody
	public ResponseEntity<Boolean> areWeAlreadyFriends(@PathVariable("pseudoname") String pseudoname,
			HttpServletRequest request) {
		RestTemplate rt = null;
		Boolean alreadyFriends = false;
		log.info("Are We Already Friends End-point:: Started :: Pseudoname : " + pseudoname);
		try {
			rt = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.set("token", request.getHeader("token"));
			// example of custom header
			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			alreadyFriends = rt
					.exchange(url + areWeAlreadyFriendsURI + pseudoname, HttpMethod.GET, entity, Boolean.class)
					.getBody();

		} catch (Exception e) {
			log.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
		}

		return ResponseEntity.status(HttpStatus.OK).body(alreadyFriends);
	}

	@GetMapping("/find-friend/{pseudoname}")
	@ResponseBody
	public ResponseEntity<?> getFriend(@PathVariable String pseudoname,
			HttpServletRequest request) {
		RestTemplate rt = null;
		log.info("getFriend >> Started");
		log.debug("getFriend :: Started :: pseudoname :" + pseudoname);
		try {
			rt = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.set("token", request.getHeader("token"));
			// example of custom header
			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			UserInfoDTO friendMatch = rt.exchange(url + findFriendURI, HttpMethod.GET, entity,
					UserInfoDTO.class).getBody();
				return ResponseEntity.status(HttpStatus.OK).body(friendMatch);
			
		} catch (Exception e) {
			log.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
		}

	}

}