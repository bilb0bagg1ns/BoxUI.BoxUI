package com.box.model.data.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.box.model.domain.Lesson;
import com.box.model.domain.User;


@Repository
public class UserRepository  {

    @Autowired
    MongoTemplate mongoTemplate;
    
    public void save (User user){
    	mongoTemplate.save(user, "users");
    }
    
	public User findById(String userId) {
		User retrievedUser = null;
		
    	// find the saved user again.
    	retrievedUser = (User)mongoTemplate.findById(userId, User.class, "users"); 
    	System.out.println("find - retrievedUser : " + retrievedUser); 
    	
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
    	System.out.println("2. find - user : " + retrievedUser); 
    	
    	return retrievedUser;
	}

	
	public User findByUserNamePassword(String userName, String password) {
		User retrievedUser = null;
		
    	// query to search user
    	Query searchUserQuery = new Query(Criteria.where("userName").is(userName).and("password").is(password));

    	// find the saved user again.
    	retrievedUser = mongoTemplate.findOne(searchUserQuery, User.class, "users");
    	System.out.println("2. find - user : " + retrievedUser); 
    	
    	return retrievedUser;
	}

}
