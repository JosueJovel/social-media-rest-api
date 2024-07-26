package com.cooksys.socialmedia.repositories;

import com.cooksys.socialmedia.entities.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	List<User> findAllByDeletedFalse();

	User findByCredentialsUsername(String username);
	
	boolean existsByCredentialsUsername(String username);

	User findByCredentialsUsernameAndCredentialsPassword(String username, String password);
}
