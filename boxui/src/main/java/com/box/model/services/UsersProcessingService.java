package com.box.model.services;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.box.model.data.repository.UserRepository;
import com.box.model.domain.Lesson;
import com.box.model.domain.User;

@Named
public class UsersProcessingService {

	private final Logger log = LoggerFactory.getLogger(UsersProcessingService.class);

	@Inject
	private UserRepository userRepository;

	@Inject
	private LessonsProcessingService lessonsProcessingService;

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
	 * Associating user to lesson. This is called when a new user is created and is associated with the lesson(s)
	 * 
	 * Add User to lesson by retrieving all lesson Ids associated with the user and 
	 * for each lesson ID, retrieve the lesson and add the user to it.
	 * 
	 * @param user
	 */
	public void associateUserToLesson(User user) {
		
	  // if user id not already associated with lesson then add it, else ignore	
		// fetch all lessons associated with this user
		List<String> lessonIdList = fetchLessonIdsInUserObject(user);
		// for each lessonId, add user to lesson
		for (String lessonId : lessonIdList) {
			// retrieve lesson by lessonId
			Lesson lesson = lessonsProcessingService.findLessonByLessonId(lessonId);
			// if user id not already in lesson association, then add it
			if (!lessonsProcessingService.isUserIdAssociatedWithLesson(user.getId(), lesson)) {
				// add user to lesson
				lesson.addUserToLesson(user);
				lessonsProcessingService.upsertLesson(lesson);
  		    } 
		}
	}

	
	/**
	 * Associating user to lesson. This is called when an user with removes or adds a lesson 
	 * 
	 * @param user
	 */
	
	public void addOrRemoveUserToLesson(User user) {
		
//		// fetch all (UI edited) lessons associated with this user 
//		List<String> lessonIdList = fetchLessonIds(user);
//
//		 // empty list means that the lesson was removed for the user
//		if ((lessonIdList != null) && (lessonIdList.isEmpty())) {
//		 
//		  // now find lessonIds that were removed for user
//		  List<String> removedLessonIdList = findLessonsRemovedForUser(user);
//		  
//		  // now, remove the user from each of the lessonIds
//		  List<String> updatedLessonIdList = lessonsProcessingService.removeAssociatedUserFromLessonAndUpdate(removedLessonIdList, user.getId());
//		
//		} else {
//		  // which means a lesson was added for the user
//			addUserToLesson (user);
//		}
		  
		  if (isLessonAdded(user)) { // if a lesson was added during User edit
			  // find lessonIds that were added for the user
			  // List<String> addedLessonIdList =  findLessonsAddedForUser(user);
			  // associate the user to the lesson
   			  associateUserToLesson (user);
		  } else {
			  // find lessonIds that were removed for the user
			  List<String> removedLessonIdList = findLessonsRemovedForUser(user);
			  // now, remove the user from each of the lessonIds
			  List<String> updatedLessonIdList = lessonsProcessingService.removeAssociatedUserFromLessonAndUpdate(removedLessonIdList, user.getId());
			  
			  
		  }
	}
	
	
	/**
	 * Check to see during editing of User by admin, additional lessons were added in which case the number
	 * of lessons in the updatedUser's list would be greater then what is in the repository
	 * 
	 * @param updatedUser
	 * @return
	 */
	private boolean isLessonAdded(User updatedUser) {
		boolean isAdd = false;
		
		// list of updated lessonId list from updatedUser
		// Ex: [123, 456, 789]
		List<String> updatedLessonIdList = fetchLessonIdsFromLessonIdNameList(updatedUser.getLessonIdNameList());
		
		// list of lessonIdName list in repository
		// Ex: [123, 456, 789, 111, 222]
		List<String> repoLessonIdList = fetchLessonIdsFromRepository(updatedUser);
		
		// if updated list is greater then what is in the repository, then lessons were added during User edit
		if (updatedLessonIdList.size() > repoLessonIdList.size()) {
			isAdd = true;
		}
		return isAdd;
	}
	
	/*-
	 * Compares the lessons in the passed in user against the lessons stored in the repository for this user and 
	 * returns the list of lessons removed if any.
	 * 
	 * In other words, it substracts from the Lessons in repository from the Updated user and returns list of lessons IDs that 
	 * were removed. 
	 * 
	 * Seq-- In Repository ------- In Updated User ---- New List of lesson ids Removed during User edit
	 * 1  -- [123 456]      --     [456]          ---   [123] (Removed)
	 * 2  -- [123 456]      --      []            ---   [123 456] (Removed)
	 * 3  -- [123 456]      --     [123 456]      ---   [] (No change - containsAll is true)
	 * 
	 * 
	 * @param updatedUser
	 * @return 
	 */
	private List<String> findLessonsRemovedForUser(User updatedUser) {
		
		// list of updated lessonId list
		// Ex: [123, 456, 789]
		List<String> updatedLessonIdList = fetchLessonIdsFromLessonIdNameList(updatedUser.getLessonIdNameList());

		// list of lessonIdName list in repository
		// Ex: [123, 456, 789, 111, 222]
		List<String> repoLessonIdList = fetchLessonIdsFromRepository(updatedUser);

		// find what lessons have been removed by comparing what is in the repository to what was passed in
		// Ex: [111, 222]		
		repoLessonIdList.removeAll(updatedLessonIdList);
		
		// contains the difference
		// Ex: [111, 222]				
		return repoLessonIdList;
	}

	/*-
	 * Compares the lessons in the passed in user against the lessons stored in the repository for this user and 
	 * returns the list of lessons added.
	 * 
	 * In other words, it substracts from the Lessons in Updated User from the ones in repository and returns list of lessons IDs that 
	 * were added. 
	 * 
	 * Seq-- In Repository ------- In Updated User ---- New List of lesson ids added during User edit
	 * 1  -- [123 456]      --     [123 456 789]          ---   [789] (Added)
	 * 
	 * 
	 * @param updatedUser
	 * @return 
	 */
	private List<String> findLessonsAddedForUser(User updatedUser) {
		
		// list of updated lessonId list
		// Ex: [123, 456, 789]
		List<String> updatedLessonIdList = fetchLessonIdsFromLessonIdNameList(updatedUser.getLessonIdNameList());

		// list of lessonIdName list in repository
		// Ex: [123, 456]
		List<String> repoLessonIdList = fetchLessonIdsFromRepository(updatedUser);

		// find what lessons have been added by comparing what is in the repository to what was passed in
		// Ex: [789]		
		updatedLessonIdList.removeAll(repoLessonIdList);
		
		// contains the added lesson
		// Ex: [789]				
		return updatedLessonIdList;
	}	
	
	/**
	 * fetch lesson ids held in the repo for the user 
	 * 
	 * @param lessonId
	 * @return
	 */
	private List<String> fetchLessonIdsFromRepository(User user){
		List<String> repoUserlessonIdList = new ArrayList<String>();
		
		// get all lessons associated with the user in the repository
		User repoUser = findById (user.getId());
		List<String> repoUserLessonIdNameList = repoUser.getLessonIdNameList();
		
		// retrieve all lesson ids
		repoUserlessonIdList = fetchLessonIdsFromLessonIdNameList(repoUserLessonIdNameList);
		return repoUserlessonIdList;
	}
	
	/**
	 * fetch lesson ids held in the user object
	 * 
	 * @param lessonId
	 * @return
	 */
	private List<String> fetchLessonIdsInUserObject(User user){
		List<String> userlessonIdList = new ArrayList<String>();
		
		// get all lessons held in the user object 
		List<String> userLessonIdNameList = user.getLessonIdNameList();
		
		// retrieve all lesson ids
		userlessonIdList = fetchLessonIdsFromLessonIdNameList(userLessonIdNameList);
		return userlessonIdList;
	}
	
	/**
	 * Iterate over lessonIdNameList and return list of lessonIds
	 * 
	 * @param lessonId
	 * @return
	 */
	private List<String> fetchLessonIdsFromLessonIdNameList(List<String> lessonIdNameList){
		List<String> lessonIdList = new ArrayList<String>();
		
		// iterate over lessonIdNameList 
		if ((lessonIdNameList != null) && (!lessonIdNameList.isEmpty())) {
			for (int i=0; i < lessonIdNameList.size(); i++) {
				String lessonIdName = lessonIdNameList.get(i);
				StringTokenizer st = new StringTokenizer(lessonIdName,";");
				  while (st.hasMoreTokens()) {
				    String lessonIdToken = st.nextToken();
				    	lessonIdList.add(i, lessonIdToken); // assign lessonId
				    	break;
				    }
				  }				
			}				
		return lessonIdList;
	}
	/**
	 * Iterate over User's lessonIdNameList and search for match with lessonId. If found remove it.
	 * 
	 * @param lessonId
	 * @return
	 */
	public boolean removeLessonFromUsersLessonList (User user, String lessonId) {
		boolean isRemoved = false;
		List<String> lessonIdNameList = user.getLessonIdNameList();		
		// iterate over lessonIdNameList 
		if ((lessonIdNameList != null) && (!lessonIdNameList.isEmpty())) {
			for (int i=0; (i < lessonIdNameList.size() && (!isRemoved)); i++) {
				String lessonIdName = lessonIdNameList.get(i);
				StringTokenizer st = new StringTokenizer(lessonIdName,";");
				  while (st.hasMoreTokens()) {
				    String lessonIdToken = st.nextToken();
				    if (lessonIdToken.equals(lessonId)) { // found lesson id 
						lessonIdNameList.remove(i);
						isRemoved = true;
						break; // we are done
				    }
				  }				
			}
		}
		return isRemoved;
	}
}
