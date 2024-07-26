package com.cooksys.socialmedia.entities;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

//@SQLRestriction("deleted = false")
@Entity
@Data
@NoArgsConstructor
@Table(name="user_details")
public class User {

    @Id
    @GeneratedValue
    private Long id;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Timestamp joined;
    
    @Embedded
    private Profile profile;
    
    @Embedded
    private Credentials credentials;
    
    private boolean deleted = false;

    @OneToMany(mappedBy="author")
    private List<Tweet> tweets;

    @ManyToMany
    @JoinTable(
        name = "user_mentions",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "tweet_id")
    )
    private List<Tweet> mentions;

    @ManyToMany
    @JoinTable(
        name = "user_likes",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "tweet_id")
    )
    private List<Tweet> likes;

    @ManyToMany
    @JoinTable(
        name = "followers_following",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "follower_id")
    )
    private List<User> followers;

    @ManyToMany(mappedBy = "followers")
    private List<User> following;
    
}
