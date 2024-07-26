package com.cooksys.socialmedia.dtos;

import java.util.List;

import com.cooksys.socialmedia.entities.Tweet;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@Data
public class ContextDto {

	@NonNull
	private TweetResponseDto target;
	
	@NonNull
	private List<TweetResponseDto> before;
	
	@NonNull
	private List<TweetResponseDto> after;
}
