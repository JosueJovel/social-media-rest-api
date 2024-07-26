package com.cooksys.socialmedia.services;

import java.util.List;

import com.cooksys.socialmedia.dtos.HashtagDto;
import com.cooksys.socialmedia.entities.Hashtag;

public interface HashtagService {

	List<HashtagDto> getAllHashTags();
	
	Boolean findIfTagExists(String label);
	
	Hashtag createHashtag (String hashtag);


}
