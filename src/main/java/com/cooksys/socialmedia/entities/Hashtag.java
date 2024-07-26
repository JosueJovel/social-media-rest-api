package com.cooksys.socialmedia.entities;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class Hashtag {

	@Id
	@GeneratedValue
	private Long id;

	@Column(unique = true, nullable = false)
	private String label;

	@CreationTimestamp
	@Column(nullable = false)
	private Timestamp firstUsed;

	@UpdateTimestamp
	@Column(nullable = false)
	private Timestamp lastUsed;
	
	private boolean deleted = false;

	@ManyToMany(mappedBy = "hashtags")
	private List<Tweet> tweets_hashtags;
}
