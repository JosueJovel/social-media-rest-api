package com.cooksys.socialmedia.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cooksys.socialmedia.entities.Hashtag;

import org.springframework.stereotype.Repository;

@Repository
public interface HashtagRepository extends JpaRepository<Hashtag, Long>{

	List<Hashtag> findAllByDeletedFalse();
	
	Hashtag findByLabel(String label);
	
	boolean existsByLabel(String label);

}
