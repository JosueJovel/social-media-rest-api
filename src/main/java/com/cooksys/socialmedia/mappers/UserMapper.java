package com.cooksys.socialmedia.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.cooksys.socialmedia.dtos.UserRequestDto;
import com.cooksys.socialmedia.dtos.UserResponseDto;
import com.cooksys.socialmedia.entities.User;

//removed tweet mapper
@Mapper(componentModel = "spring", uses = {ProfileMapper.class, CredentialsMapper.class })
public interface UserMapper {

	@Mapping(target="username", source="entity.credentials.username")
    UserResponseDto entityToDto(User entity);
    
    User requestDtoToEntity(UserRequestDto userRequestDto);

    List<UserResponseDto> entitiesToDtos(List<User> entities);
}
