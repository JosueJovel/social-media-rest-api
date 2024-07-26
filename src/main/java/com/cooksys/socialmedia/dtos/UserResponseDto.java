package com.cooksys.socialmedia.dtos;

import java.sql.Timestamp;

import com.cooksys.socialmedia.entities.Profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {

    @NonNull
    private String username;

    @NonNull
    private Profile profile;
    
    @NonNull
    private Timestamp joined;

}
