package com.cooksys.socialmedia.dtos;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TweetResponseDto {
	
	@NonNull
	private Long id;
	
	@NonNull
	private UserResponseDto author;
	
	@NonNull
	private Timestamp posted;
	
	private String content;
	
	private TweetResponseDto inReplyTo;
	
	private TweetResponseDto repostOf;
}
