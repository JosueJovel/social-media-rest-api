package com.cooksys.socialmedia.controllers;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.socialmedia.dtos.ContextDto;
import com.cooksys.socialmedia.dtos.HashtagDto;
import com.cooksys.socialmedia.dtos.CredentialsDto;
import com.cooksys.socialmedia.dtos.TweetRequestDto;
import com.cooksys.socialmedia.dtos.TweetResponseDto;
import com.cooksys.socialmedia.dtos.UserResponseDto;
import com.cooksys.socialmedia.services.TweetService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tweets")
public class TweetController {
	private final TweetService tweetService;
	
	// @GetMapping
	@GetMapping
	public List<TweetResponseDto> getAllTweets(){
		return tweetService.getAllTweets();
	}
	
	// @GetMapping("/{id}")
	@GetMapping("/{id}")
	public TweetResponseDto getTweetById(@PathVariable Long id) {
		return tweetService.getTweetById(id);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public TweetResponseDto createTweet(@RequestBody TweetRequestDto tweetRequestDto) {
		return tweetService.createTweet(tweetRequestDto);
	}
	
	// @DeleteMapping("/{id}"
	@DeleteMapping("/{id}")
	public TweetResponseDto deleteTweet(@PathVariable Long id) {
		return tweetService.deleteTweet(id);
	}
	
	// POST    tweets/{id}/like
	@PostMapping("/{tweetId}/like")
	@ResponseStatus(HttpStatus.CREATED)
	public void createLike(@PathVariable Long tweetId, @RequestBody CredentialsDto credentialsDto) {
		tweetService.createLike(tweetId, credentialsDto);
	}
	
	@PostMapping("/{tweetId}/reply")
	@ResponseStatus(HttpStatus.CREATED)
	public TweetResponseDto createReplyTweet(@PathVariable Long tweetId, @RequestBody TweetRequestDto tweetRequestDto) {
		return tweetService.createReplyTweet(tweetRequestDto, tweetId);
	}
	
	@PostMapping("/{repostedTweetId}/repost")
	@ResponseStatus(HttpStatus.CREATED)
	public TweetResponseDto createRepostTweet(@PathVariable Long repostedTweetId, @RequestBody CredentialsDto reposterCredentials) {
		return tweetService.createRepostTweet(repostedTweetId, reposterCredentials);
	}
	
    @GetMapping("/{id}/tags")
    public List<HashtagDto> getTags(@PathVariable Long id) {
    	return tweetService.getTags(id);
    }
    
  // GET     tweets/{id}/likes
    @GetMapping("/{id}/likes")
    public List<UserResponseDto> getLikes(@PathVariable Long id) {
    	return tweetService.getLikes(id);
    }
  
  // GET     tweets/{id}/context
    @GetMapping("/{id}/context")
    public ContextDto getContext(@PathVariable Long id){
        return tweetService.getContext(id);
    }
  
  // GET     tweets/{id}/replies
    @GetMapping("/{id}/replies")
    public List<TweetResponseDto> getReplies(@PathVariable Long id){
        return tweetService.getReplies(id);
    }
  
  // GET     tweets/{id}/reposts
    @GetMapping("/{id}/reposts")
    public List<TweetResponseDto> getReposts(@PathVariable Long id){
        return tweetService.getReposts(id);
    }
  
  // GET     tweets/{id}/mentions
    @GetMapping("/{id}/mentions")
    public List<UserResponseDto> getMentions(@PathVariable Long id){
        return tweetService.getMentions(id);
    }

}
