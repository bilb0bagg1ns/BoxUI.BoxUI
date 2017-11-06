package com.box.model.services;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.box.model.data.repository.UserRepository;
import com.box.model.domain.User;

@Named
public class UsersProcessingService {

	private final Logger log = LoggerFactory.getLogger(UsersProcessingService.class);

	@Inject
	private UserRepository repository;

	public void saveUser(User user) {
		repository.save(user);
	}

	public void deleteUser(String userId) {
		repository.delete(userId);
	}

	public void upsertLesson(User user) {
		// repository.upsert(user);

		// can't seem to upsert user's skill applicable checkboxes. hence
		// doing this approach
		deleteUser(user.getId());
		saveUser(user);
	}

	public User findById(String userId) {
		User user = repository.findById(userId);
		return user;
	}

	public List<User> retrieveAllUsers() {

		List<User> lessonsList = repository.retrieveAllUsers();
		return lessonsList;
	}

	
}
