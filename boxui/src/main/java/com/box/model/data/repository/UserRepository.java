package com.box.model.data.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.box.model.domain.User;

@Repository
public class UserRepository {

	private final Logger log = LoggerFactory.getLogger(UserRepository.class);

	@Autowired
	MongoTemplate mongoTemplate;

	public void save(User user) {

		Query query = new Query();
		query.addCriteria(Criteria.where("userName").is(user.getUserName()));
		query.fields().include("userName");
		User userStored = mongoTemplate.findOne(query, User.class);

		if (userStored == null) {
			user.setCreatedDate(new Date());
			mongoTemplate.save(user, "users");
		}
	}

	public void delete(String userId) {

		log.debug(userId + "Lesson ID being deleted <<<<<<<<<<<<<<<<<<<>>>>>>>>>");
		User user = findById(userId);
		mongoTemplate.remove(user);
	}
	
	public User findById(String userId) {
		User retrievedUser = null;

		// find the saved user again.
		retrievedUser = (User) mongoTemplate.findById(userId, User.class, "users");
		log.debug("find - retrievedUser : " + retrievedUser);

		return retrievedUser;
	}

	public User findByUserNamePassword(Query query) {
		return mongoTemplate.findOne(query, User.class, "users");
	}

	public User findByUserName(String userName) {
		User retrievedUser = null;

		// query to search user
		Query searchUserQuery = new Query(Criteria.where("userName").is(userName));

		// find the saved user again.
		retrievedUser = mongoTemplate.findOne(searchUserQuery, User.class, "users");
		log.debug("2. find - user : " + retrievedUser);

		return retrievedUser;
	}

	public User findByUserNamePassword(String userName, String password) {
		User retrievedUser = null;

		// query to search user
		Query searchUserQuery = new Query(Criteria.where("userName").is(userName).and("password").is(password));

		// find the saved user again.
		retrievedUser = mongoTemplate.findOne(searchUserQuery, User.class, "users");
		log.debug("2. find - user : " + retrievedUser);

		return retrievedUser;
	}
	
	public List<User> retrieveAllUsers() {

		log.debug("\nUserRepository::retrieveAllUsers " + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");
		ArrayList<User> retrievedUsersList = null;
		// find all lessons
		retrievedUsersList = (ArrayList<User>) mongoTemplate.findAll(User.class);
		log.debug("retrieveAllUsers : " + retrievedUsersList);
		return retrievedUsersList;

	}

}
