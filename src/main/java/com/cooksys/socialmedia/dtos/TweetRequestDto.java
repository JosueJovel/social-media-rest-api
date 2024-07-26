package com.cooksys.socialmedia.dtos;

import com.cooksys.socialmedia.entities.Credentials;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TweetRequestDto {
	
	private String content;
	
	private Credentials credentials;
}
