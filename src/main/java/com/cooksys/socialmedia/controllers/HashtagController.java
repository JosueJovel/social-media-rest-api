package com.cooksys.socialmedia.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.socialmedia.dtos.HashtagDto;
import com.cooksys.socialmedia.dtos.TweetResponseDto;
import com.cooksys.socialmedia.services.HashtagService;
import com.cooksys.socialmedia.services.TweetService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/tags")
@AllArgsConstructor
public class HashtagController {
	
	private final HashtagService hashtagService;
	private final TweetService tweetService;

	@GetMapping
	public List<HashtagDto> getAllHashTags() {
		return hashtagService.getAllHashTags();
	}
	
	@GetMapping("/{label}")
	public List<TweetResponseDto> getTweetsByLabel(@PathVariable String label) {
		List<TweetResponseDto> fetchedTweets = tweetService.getTweetsFromHashtag(label);//Get tagged tweets from tweet service
		return fetchedTweets;
	}
	
}
