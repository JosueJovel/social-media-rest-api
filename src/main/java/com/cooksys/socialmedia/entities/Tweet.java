package com.cooksys.socialmedia.entities;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name="tweet")
@SQLRestriction("deleted = false") //Soft deleted entities are ignored
public class Tweet {
	
    @Id
    @GeneratedValue
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;
    
	@CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Timestamp posted;
        
    @Column
    private String content;
    
    @ManyToMany(mappedBy = "likes")
    private List<User> users_likes;
    
    @ManyToMany(mappedBy = "mentions")
    private List<User> users_mentions;
    
    @ManyToOne
    @JoinColumn (name = "inReplyTo")
    private Tweet inReplyTo;
    
    @OneToMany(mappedBy = "inReplyTo")
    private List<Tweet> replies;
    
    @ManyToOne
    @JoinColumn (name = "repostOf")
    private Tweet repostOf;
    
    @OneToMany(mappedBy = "repostOf")
    private List<Tweet> reposts;
    
    @ManyToMany
    @JoinTable (
    		name = "tweet_hashtags",
    		joinColumns = @JoinColumn(name = "tweet_id"),
    		inverseJoinColumns = @JoinColumn(name = "hashtag_id")
    	)
    private List<Hashtag> hashtags;

    private boolean deleted = false;

}
