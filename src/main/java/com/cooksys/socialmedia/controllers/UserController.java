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

import com.cooksys.socialmedia.dtos.CredentialsDto;
import com.cooksys.socialmedia.dtos.TweetResponseDto;
import com.cooksys.socialmedia.dtos.UserRequestDto;
import com.cooksys.socialmedia.dtos.UserResponseDto;
import com.cooksys.socialmedia.entities.Tweet;
import com.cooksys.socialmedia.entities.User;
import com.cooksys.socialmedia.services.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    
    private final UserService userService;

    @GetMapping
    public List<UserResponseDto> getAllUsers() {
        return userService.getAllUsers();
    };

    @GetMapping("/@{username}")
    public UserResponseDto getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    @GetMapping("/@{username}/feed")
    public List<TweetResponseDto> getFeed(@PathVariable String username) {
        return userService.getFeed(username);
    }

    @GetMapping("/@{username}/tweets")
    public List<TweetResponseDto> getUserTweets(@PathVariable String username) {
        return userService.getUserTweets(username);
    }

    @GetMapping("/@{username}/mentions")
    public List<TweetResponseDto> getUserMentions(@PathVariable String username) {
        return userService.getUserMentions(username);
    }

    @GetMapping("/@{username}/followers")
    public List<UserResponseDto> getUserFollowers(@PathVariable String username) {
        return userService.getUserFollowers(username);
    } 

    @GetMapping("/@{username}/following")
    public List<UserResponseDto> getUserFollowed(@PathVariable String username) {
        return userService.getUserFollowing(username);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto createUser(@RequestBody UserRequestDto userRequestDto) {
        return userService.createUser(userRequestDto);
    }

    @PatchMapping("/@{username}")
    public UserResponseDto editUser(@RequestBody UserRequestDto userRequestDto, @PathVariable String username) {
        return userService.editUser(userRequestDto, username);
    }

    @PostMapping("/@{username}/follow")
    public void followUser(@RequestBody CredentialsDto credentialsDto, @PathVariable String username) {
        userService.followUser(credentialsDto, username);
    }

    @PostMapping("/@{username}/unfollow")
    public void unfollowUser(@RequestBody CredentialsDto credentialsDto, @PathVariable String username) {
        userService.unfollowUser(credentialsDto, username);
    }
    
    @DeleteMapping("/@{username}")
    public UserResponseDto deleteUser(@RequestBody CredentialsDto userCredentialsDto) {
    	UserResponseDto deletedUser = userService.deleteUser(userCredentialsDto);
    	return deletedUser;
    }
}
