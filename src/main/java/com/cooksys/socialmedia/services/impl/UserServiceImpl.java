package com.cooksys.socialmedia.services.impl;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cooksys.socialmedia.dtos.CredentialsDto;
import com.cooksys.socialmedia.dtos.TweetResponseDto;
import com.cooksys.socialmedia.dtos.UserRequestDto;
import com.cooksys.socialmedia.dtos.UserResponseDto;
import com.cooksys.socialmedia.entities.Credentials;
import com.cooksys.socialmedia.entities.Tweet;
import com.cooksys.socialmedia.entities.User;
import com.cooksys.socialmedia.exceptions.BadRequestException;
import com.cooksys.socialmedia.exceptions.NotFoundException;
import com.cooksys.socialmedia.mappers.CredentialsMapper;
import com.cooksys.socialmedia.mappers.TweetMapper;
import com.cooksys.socialmedia.mappers.UserMapper;
import com.cooksys.socialmedia.repositories.TweetRepository;
import com.cooksys.socialmedia.repositories.UserRepository;
import com.cooksys.socialmedia.services.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	
	private final UserRepository userRepository;
	private final UserMapper userMapper;
	private final TweetRepository tweetRepository;
	private final TweetMapper tweetMapper;
	private final CredentialsMapper credentialsMapper;
	
	// Validate that the request has all the required fields
	private void validateUserRequest(UserRequestDto userRequestDto) {
		if(userRequestDto == null || userRequestDto.getCredentials() == null || userRequestDto.getProfile() == null) {
			throw new BadRequestException("Missing credentials or profile");
		}
		String username = userRequestDto.getCredentials().getUsername();
		String password = userRequestDto.getCredentials().getPassword();
		String email = userRequestDto.getProfile().getEmail();
		
		if(username == null || password == null || email == null) {
			throw new BadRequestException("Missing either credentials or profile fields.");
		}
	}
	
	// Check that the user exists and is not deleted in our DB
	private void checkUserExists(User user) {
		if(user == null || user.isDeleted()) {
			throw new NotFoundException("No users with match username found.");
		}
	}
	
	//quicksort algorithm for sorting tweets by date
	private void quickSort(List<Tweet> tweets, int begin, int end) {
		if(begin < end) {
			int partitionIndex = partition(tweets, begin, end);
			
			quickSort(tweets, begin, partitionIndex - 1);
			quickSort(tweets, partitionIndex + 1, end);
		}
	}
	
	//quicksorts partition for sorting tweets by date
	private int partition(List<Tweet> tweets, int begin, int end) {
		Timestamp pivot = tweets.get(end).getPosted();
		int i = (begin - 1);
		
		for(int j = begin; j < end; j++) {
			if(tweets.get(j).getPosted().compareTo(pivot) <= 0) {
				i++;
				
				Tweet swapTemp = tweets.get(i);
				tweets.set(i, tweets.get(j));
				tweets.set(j, swapTemp);
			}
		}
		
		Tweet swapTemp = tweets.get(i + 1);
		tweets.set(i + 1, tweets.get(end));
		tweets.set(end, swapTemp);
		
		return i + 1;
	}
	
	@Override
	public List<UserResponseDto> getAllUsers() {
		return userMapper.entitiesToDtos(userRepository.findAllByDeletedFalse());
	}

	@Override
	public UserResponseDto getUserByUsername(String username) {
		User optionalUser = userRepository.findByCredentialsUsername(username);
		if(optionalUser == null || optionalUser.isDeleted()){
			throw new NotFoundException("No User with the username: " + username);
		}
		return userMapper.entityToDto(optionalUser);
	}

	@Override
	public UserResponseDto createUser(UserRequestDto userRequestDto) {
		validateUserRequest(userRequestDto);
		User userToCreate = userMapper.requestDtoToEntity(userRequestDto);
		User userExists = userRepository.findByCredentialsUsername(userToCreate.getCredentials().getUsername());
		if(userExists != null && !userExists.isDeleted()){
			throw new BadRequestException("User already exists.");
		}
		if(userExists != null && userExists.isDeleted()) {
			userExists.setDeleted(false);
			userRepository.saveAndFlush(userExists);
			return userMapper.entityToDto(userExists);
		} else {
			userRepository.saveAndFlush(userToCreate);
			return userMapper.entityToDto(userToCreate);
		}
	}

	@Override
	public Boolean findIfUserExists(String username) {
		User user = userRepository.findByCredentialsUsername(username);
		if(user == null || user.isDeleted()) {
			return false;
		}
		return true;
	}

	@Override
	public UserResponseDto deleteUser(CredentialsDto userRequestDto) {
		User userToDelete = userRepository.findByCredentialsUsernameAndCredentialsPassword(userRequestDto.getUsername(), userRequestDto.getPassword()); //IMPORTANT: mmay need to make CredentialsPassword
		if (userToDelete == null) {
			throw new BadRequestException("No user with matching username and password found.");
		}
		userToDelete.setDeleted(true); //Soft delete a user and any of their tweets
		for (Tweet userTweet : userToDelete.getTweets()) {
			userTweet.setDeleted(true);
		}
		userRepository.saveAndFlush(userToDelete);//This is creating a second user/password? Yes, because it is not PATCHING
		return userMapper.entityToDto(userToDelete); //Return the soft deleted user
	}

	@Override
	public UserResponseDto editUser(UserRequestDto userRequestDto, String username) {
		if(userRequestDto == null || userRequestDto.getCredentials() == null || userRequestDto.getProfile() == null) {
			throw new BadRequestException("Missing credentials or profile");
		}
		String name = userRequestDto.getCredentials().getUsername();
		String password = userRequestDto.getCredentials().getPassword();
		String email = userRequestDto.getProfile().getEmail();
		
		if(name == null || password == null) {
			throw new BadRequestException("Missing either credentials or profile fields.");
		}
		
		User userToUpdate = userRepository.findByCredentialsUsername(username);
		checkUserExists(userToUpdate);
		User newUser = userMapper.requestDtoToEntity(userRequestDto);
		userToUpdate.setCredentials(newUser.getCredentials());
		// Here we check to see if new profile has something
		if(newUser.getProfile().getFirstName() != null) {
			userToUpdate.setProfile(newUser.getProfile());
		}
		return userMapper.entityToDto(userRepository.saveAndFlush(userToUpdate));
	}

	@Override
	public List<TweetResponseDto> getFeed(String username) {
		User userFeed = userRepository.findByCredentialsUsername(username);
		checkUserExists(userFeed);
		
		List<Tweet> feed = userFeed.getTweets();
		
		//get all tweets for user and following users
		for(User u : userFeed.getFollowing()) {
			List<Tweet> uFeedTweets = u.getTweets();
			for(Tweet t : uFeedTweets) {
				if(!feed.contains(t) || !t.isDeleted()) {
					feed.add(t);
				}
			}
		}
		
		//quicksort all tweets
		quickSort(feed, 0, feed.size()-1);
		
		return tweetMapper.entitiesToDtos(feed);
	}

	@Override
	public List<TweetResponseDto> getUserTweets(String username) {
		User userTweets = userRepository.findByCredentialsUsername(username);
		checkUserExists(userTweets);
		
		List<Tweet> tweets = userTweets.getTweets();
		return tweetMapper.entitiesToDtos(tweets);
	}

	@Override
	public List<TweetResponseDto> getUserMentions(String username) {
		User userMentions = userRepository.findByCredentialsUsername(username);
		checkUserExists(userMentions);
		List<Tweet> mentions = userMentions.getMentions();
		quickSort(mentions, 0, mentions.size()-1);
		return tweetMapper.entitiesToDtos(mentions);
	}

	@Override
	public List<UserResponseDto> getUserFollowers(String username) {
		User userFollowers = userRepository.findByCredentialsUsername(username);
		checkUserExists(userFollowers);
		List<User> followers = userFollowers.getFollowers();
		return userMapper.entitiesToDtos(followers);
	}

	@Override
	public List<UserResponseDto> getUserFollowing(String username) {
		User userFollowing = userRepository.findByCredentialsUsername(username);
		checkUserExists(userFollowing);
		List<User> following = userFollowing.getFollowing();
		return userMapper.entitiesToDtos(following);
	}

	@Override
	public void unfollowUser(CredentialsDto credentialsDto, String username) {
		User userToUnfollow = userRepository.findByCredentialsUsername(username);
		checkUserExists(userToUnfollow);
		
		Credentials credentials = credentialsMapper.requestDtoToEntity(credentialsDto);
		User userUnfollowing = userRepository.findByCredentialsUsernameAndCredentialsPassword(credentials.getUsername(), credentials.getPassword());
		checkUserExists(userUnfollowing);
		
		List<User> following = userUnfollowing.getFollowing();
		if(!following.contains(userToUnfollow)) {
			throw new NotFoundException("" + userToUnfollow.getCredentials().getUsername() + " is not following " + username);
		}
		following.remove(userToUnfollow);
		userUnfollowing.setFollowing(following);
		
		List<User> followers = userToUnfollow.getFollowers();
		followers.remove(userUnfollowing);
		userToUnfollow.setFollowers(followers);
		
		userRepository.saveAndFlush(userUnfollowing);
		userRepository.saveAndFlush(userToUnfollow);
		
	}

	@Override
	public void followUser(CredentialsDto credentialsDto, String username) {
		// UserFollowing should be in the credentials
		User userToFollow = userRepository.findByCredentialsUsername(username);
		checkUserExists(userToFollow);
		
		Credentials credentials = credentialsMapper.requestDtoToEntity(credentialsDto);
		User userFollowing = userRepository.findByCredentialsUsernameAndCredentialsPassword(credentials.getUsername(), credentials.getPassword());
		checkUserExists(userFollowing);
		
		List<User> following = userFollowing.getFollowing();
		if(following.contains(userToFollow)) {
			throw new BadRequestException("" + credentials.getUsername() + " is already following " + username);
		}
		following.add(userToFollow);
		userFollowing.setFollowing(following);
		
		List<User> followed = userToFollow.getFollowers();
		if(followed.contains(userFollowing)) {
			throw new BadRequestException("" + credentials.getUsername() + " already follows " + username);
		}
		followed.add(userFollowing);

		userToFollow.setFollowers(followed);
		
		userRepository.saveAndFlush(userFollowing);
		userRepository.saveAndFlush(userToFollow);
	}

    
}
