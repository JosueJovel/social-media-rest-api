package com.cooksys.socialmedia.repositories;

import com.cooksys.socialmedia.entities.Tweet;
import com.cooksys.socialmedia.entities.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long>{

	// will need to implement delete functions later
	List<Tweet> findAllByHashtags_Id(Long hashtagid); //Derived query: Fetches tweets from the tweet_hashtag table, using a hashtag id.
	
	List<Tweet> findAllByDeletedFalse();
	
}
