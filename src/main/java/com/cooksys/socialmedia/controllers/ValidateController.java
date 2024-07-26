package com.cooksys.socialmedia.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.socialmedia.services.HashtagService;
import com.cooksys.socialmedia.services.UserService;
import com.cooksys.socialmedia.services.ValidateService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/validate")
@AllArgsConstructor
public class ValidateController {

	private ValidateService validateService;
	private final HashtagService hashtagService;
	private final UserService userService;
	
	@GetMapping("/tag/exists/{label}")
	public boolean tagExists(@PathVariable String label) {
		return hashtagService.findIfTagExists(label);
	}
	
	@GetMapping("/username/exists/@{username}")
	public boolean userExists(@PathVariable String username) {
		return userService.findIfUserExists(username);
	}
	
	@GetMapping("/username/available/@{username}")
	public boolean isUsernameAvailable(@PathVariable String username) {
		return validateService.isUsernameAvailable(username);
	}
}
