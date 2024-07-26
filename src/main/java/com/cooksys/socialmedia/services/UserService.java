package com.cooksys.socialmedia.services;

import java.util.List;

import com.cooksys.socialmedia.dtos.CredentialsDto;
import com.cooksys.socialmedia.dtos.TweetResponseDto;
import com.cooksys.socialmedia.dtos.UserRequestDto;
import com.cooksys.socialmedia.dtos.UserResponseDto;
import com.cooksys.socialmedia.entities.Tweet;

public interface UserService {

	List<UserResponseDto> getAllUsers();

	UserResponseDto getUserByUsername(String username);

	UserResponseDto createUser(UserRequestDto userRequestDto);
	
	Boolean findIfUserExists(String username);
	
	UserResponseDto deleteUser(CredentialsDto userRequestDto);

	UserResponseDto editUser(UserRequestDto userRequestDto, String username);

	List<TweetResponseDto> getFeed(String username);

	List<TweetResponseDto> getUserTweets(String username);

	List<TweetResponseDto> getUserMentions(String username);

	List<UserResponseDto> getUserFollowers(String username);

	List<UserResponseDto> getUserFollowing(String username);

	void unfollowUser(CredentialsDto credentialsDto, String username);

	void followUser(CredentialsDto credentialsDto, String username);

//	Boolean isUsernameAvailable(String username);

}
