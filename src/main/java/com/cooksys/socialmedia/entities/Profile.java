package com.cooksys.socialmedia.entities;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Profile {

	private String firstName;
	
	private String lastName;
	
	@NonNull
	private String email;
	
	private String phone;
}
