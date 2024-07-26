package com.cooksys.socialmedia.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.cooksys.socialmedia.dtos.HashtagDto;
import com.cooksys.socialmedia.entities.Hashtag;

@Mapper(componentModel = "spring")
public interface HashtagMapper {

	Hashtag dtoToEntity(HashtagDto hashTagDto);
	
	HashtagDto entityToDto(Hashtag hashtag);
	
	List<HashtagDto> entitiesToDtos(List<Hashtag> hashtags);
}
