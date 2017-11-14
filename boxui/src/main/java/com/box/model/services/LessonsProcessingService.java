package com.box.model.services;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.box.model.data.repository.LessonRepository;
import com.box.model.domain.Lesson;
import com.box.model.domain.User;

@Named
public class LessonsProcessingService {

	private final Logger log = LoggerFactory.getLogger(LessonsProcessingService.class);

	@Inject
	private LessonRepository lessonRepository;

	@Inject
    private UsersProcessingService usersProcessingService;
	
	public void saveLesson(Lesson lesson) {
		lessonRepository.save(lesson);
	}

	/**
	 * As part of admin flow, when the admin deletes a lesson, the following steps apply:
	 * 	1. Need to identify all users who have this lesson assigned to them and delete the associated lesson
	 *  2. Delete the lesson
	 * @param lessonId
	 */
	public void deleteLesson(String lessonId) {
		
		/* need to delete this lesson from the associated user of this lesson */
		
		// retrieve lesson
		Lesson lesson = findLessonByLessonId (lessonId);
		// get all userids associated with this lessonId
		List<String> userIdList = lesson.getUserIdList();
		
		// for each userId, retrieve the User and remove associated lesson
		for (String userId : userIdList) {
			// retrieve user for userId
			User user = usersProcessingService.findById(userId);
			if (user != null) {
				// remove associated lesson from user
				usersProcessingService.removeLessonFromUsersLessonList(user, lessonId);
				// save the updated user who now has an updated lesson list
				usersProcessingService.upsertUser(user);
			}
		}
		
		// delete lesson
		lessonRepository.delete(lessonId);
	}
	
	
	public void deleteJustLesson(String lessonId) {
		
		// delete lesson
		lessonRepository.delete(lessonId);
	}
	
	/**
	 * Check if userId already exists in the lesson's associated userid list
	 *   return true if so
	 * @param userId
	 * @param lesson
	 * @return
	 */
	public boolean isUserIdAssociatedWithLesson (String userId, Lesson lesson) {
		boolean isAssociated = false;

		// get all userids associated with this lessonId
		List<String> userIdList = lesson.getUserIdList();
		if (userIdList.contains(userId)) {
			isAssociated = true;
		}
		return isAssociated;
	}

	public void upsertLesson(Lesson lesson) {
		// repository.upsert(lesson);

		// can't seem to upsert lesson's skill applicable checkboxes. hence
		// doing this approach
		deleteLesson(lesson.getId());
		saveLesson(lesson);
	}

	public void upsertJustLesson(Lesson lesson) {
		// repository.upsert(lesson);

		// can't seem to upsert lesson's skill applicable checkboxes. hence
		// doing this approach
		deleteJustLesson(lesson.getId());
		saveLesson(lesson);
	}
	
	public Lesson findLessonByLessonId(String lessonId) {
		Lesson lesson = lessonRepository.findLessonByLessonId(lessonId);
		return lesson;
	}

	public List<Lesson> retrieveAllLessons() {

		List<Lesson> lessonsList = lessonRepository.retrieveAllLessons();
		return lessonsList;
	}

	public List<Lesson> retrieveLessonsList(String skillLevelTypeId) {

		List<Lesson> lessonsList = lessonRepository.findLessonsBySkillLevelTypeId(skillLevelTypeId);
		log.debug(skillLevelTypeId + "<<<<<----");

		return lessonsList;
	}

	/**
	 * Iterate over lessons from the removedLessonIdList and remove passed in userId for each.
	 * 
	 * @param removedLessonIdList
	 * @param userId
	 * @return
	 */
	public List<String> removeAssociatedUserFromLessonAndUpdate(List<String> removedLessonIdList, String userId) {
		
		for (String lessonIdRemoved : removedLessonIdList) {
			// retrieve the lesson for the lesson id
			Lesson lesson = findLessonByLessonId(lessonIdRemoved);
			
			// remove the userId from the lesson
			List<String> userIdList = lesson.getUserIdList();
			userIdList.remove(userId);
			
			// update the lesson now
			upsertJustLesson(lesson);
		}
		return removedLessonIdList;
	}


}
