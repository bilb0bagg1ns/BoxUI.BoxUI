package com.box.model.services;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.box.model.data.repository.LessonRepository;
import com.box.model.data.repository.UserRepository;
import com.box.model.domain.Lesson;
import com.box.model.domain.User;

@Named
public class UsersProcessingService {

	private final Logger log = LoggerFactory.getLogger(UsersProcessingService.class);

	@Inject
	private UserRepository userRepository;

	@Inject
	private LessonRepository lessonRepository;

	public void saveUser(User user) {
		userRepository.save(user);
	}

	public void deleteUser(String userId) {
		userRepository.delete(userId);		
	}

	public void upsertUser(User user) {
		// repository.upsert(user);

		// can't seem to upsert user's skill applicable checkboxes. hence
		// doing this approach
		deleteUser(user.getId());
		saveUser(user);
	}

	public User findById(String userId) {
		User user = userRepository.findById(userId);
		return user;
	}

	public List<User> retrieveAllUsers() {

		List<User> usersList = userRepository.retrieveAllUsers();
		return usersList;
	}

	/**
	 * Add User to lesson by retrieving all lesson Ids associated with the user and 
	 * for each lesson ID, retrieve the lesson and add the user to it.
	 * 
	 * @param user
	 */
	public void addUserToLesson(User user) {
		// fetch all lessons associated with this user
		List<String> lessonIdList = user.fetchLessonIds();
		// for each lessonId, add user to lesson
		for (String lessonId : lessonIdList) {
			// retrieve lesson by lessonId
			Lesson lesson = lessonRepository.findLessonByLessonId(lessonId);
			// add user to lesson
			lesson.addUserToLesson(user);
			lessonRepository.upsert(lesson);
		}
	}
}
