package com.cooksys.socialmedia.dtos;

import java.sql.Timestamp;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@Data
public class HashtagDto {
	
	@NonNull
	private String label;
	
	@NonNull
	private Timestamp firstUsed;
	
	@NonNull
	private Timestamp lastUsed;

}
