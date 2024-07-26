package com.cooksys.socialmedia.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cooksys.socialmedia.dtos.HashtagDto;
import com.cooksys.socialmedia.entities.Hashtag;
import com.cooksys.socialmedia.exceptions.BadRequestException;
import com.cooksys.socialmedia.mappers.HashtagMapper;
import com.cooksys.socialmedia.repositories.HashtagRepository;
import com.cooksys.socialmedia.services.HashtagService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HashtagServiceImpl implements HashtagService{
	
	private final HashtagRepository hashtagRepository;//Variables need to be "final" for @RequiredArgsConstructor to autowire dependencies
	private final HashtagMapper hashtagMapper;
	
	@Override
	public List<HashtagDto> getAllHashTags() {
		return hashtagMapper.entitiesToDtos(hashtagRepository.findAll());
	}

	@Override
	public Boolean findIfTagExists(String label) {
//		label = "#" + label; //When using Seeded DB data, uncomment this line to append # to the label (seeded data is incorrect)
		return hashtagRepository.existsByLabel(label); //Find if hashtag exists in db by label
	}

	@Override
	public Hashtag createHashtag(String hashtag) {
		if (hashtagRepository.existsByLabel(hashtag)) {
			throw new BadRequestException("Error creating hashtag: Hashtag already exists.");
		} 
		Hashtag hashtagToBeCreated = new Hashtag();
		hashtagToBeCreated.setLabel(hashtag); //Create a new hashtag and set the label. first and last used will automatically be set
		hashtagRepository.saveAndFlush(hashtagToBeCreated); //Save new hashtag to the DB
		return hashtagToBeCreated;
	}

}
