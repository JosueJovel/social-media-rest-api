package com.cooksys.socialmedia.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.cooksys.socialmedia.dtos.TweetRequestDto;
import com.cooksys.socialmedia.dtos.TweetResponseDto;
import com.cooksys.socialmedia.entities.Tweet;

//removed hashtag mapper
@Mapper(componentModel = "spring", uses = { UserMapper.class})
public interface TweetMapper {
	
	@Mapping(target="author", source = "entity.author") //WAS userResponseDto
	TweetResponseDto entityToDto(Tweet entity);
	
	Tweet requestDtoToEntity(TweetRequestDto tweetRequestDto);
	
	List<TweetResponseDto> entitiesToDtos(List<Tweet> entities);

}
