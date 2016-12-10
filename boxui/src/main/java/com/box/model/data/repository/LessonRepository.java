package com.box.model.data.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.box.model.domain.Lesson;


@Repository
public class LessonRepository  {

    @Autowired
    MongoTemplate mongoTemplate;
    
    public void save (Lesson lesson){
    	
		System.out.println (lesson + "Lesson being saved <<<<<<<<<<<<<<<<<<<>>>>>>>>>");	
    	mongoTemplate.save(lesson, "lessons");
    }

    public void update (String lessonId){
    	
		System.out.println (lessonId + "Lesson being updated <<<<<<<<<<<<<<<<<<<>>>>>>>>>");	
		
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(lessonId));
		query.fields().include("_id");

		Lesson lesson = mongoTemplate.findOne(query, Lesson.class);
		System.out.println("lesson - " + lesson);

		Update update = new Update();
		//update.set("age", 100);

		mongoTemplate.updateFirst(query, update, Lesson.class);
		
    }
    
    public void upsert (Lesson lesson){
    	
		System.out.println (lesson + "Lesson being upserted <<<<<<<<<<<<<<<<<<<>>>>>>>>>");	
		
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(lesson.getId()));
		query.fields().include("_id");


		Update update = new Update();
		update.set("id", lesson.getId());
		update.set("skillLevelTypeId", lesson.getSkillLevelTypeId());
		update.set ("name", lesson.getName());
		update.set ("shortDescription", lesson.getShortDescription());
		update.set ("longDescription", lesson.getLongDescription());
		
		mongoTemplate.upsert(query, update, Lesson.class);
		
    }
    
    public Lesson findByUserNamePassword(Query query) {
    	return mongoTemplate.findOne(query, Lesson.class, "lessons");
    }
    
	public Lesson findLessonByLessonId(String lessonId) {
		Lesson retrievedLesson = null;
		
    	// query to search lessons
    	Query searchLessonsQuery = new Query(Criteria.where("_id").is(lessonId));

    	// find the saved user again.
    	retrievedLesson = (Lesson)mongoTemplate.findById(lessonId, Lesson.class, "lessons"); //(searchLessonsQuery, Lesson.class, "lessons");
    	System.out.println("find - retrievedLesson : " + retrievedLesson); 
    	
    	return retrievedLesson;
	}
    
	public List<Lesson> findLessonsBySkillLevelTypeId(String skillLevelTypeId) {
		ArrayList<Lesson> retrievedLessonsList = null;
		
    	// query to search lessons
    	Query searchLessonsQuery = new Query(Criteria.where("skillLevelTypeId").is(skillLevelTypeId));

    	// find the saved user again.
    	retrievedLessonsList = (ArrayList<Lesson>)mongoTemplate.find(searchLessonsQuery, Lesson.class, "lessons");
    	System.out.println("2. find - retrievedLessonsList : " + retrievedLessonsList); 
    	
    	return retrievedLessonsList;
	}

}
