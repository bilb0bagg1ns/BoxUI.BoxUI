package com.box.model.data.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.box.model.domain.Grade;
import com.box.model.domain.Lesson;
import com.mongodb.BasicDBObject;


@Repository
public class GradeRepository  {

    @Autowired
    MongoTemplate mongoTemplate;
    
    public void save (Grade grade){
    	
		System.out.println (grade + "Grade being saved <<<<<<<<<<<<<<<<<<<>>>>>>>>>");
    	mongoTemplate.save(grade, "grades");
    }

    public void update (String gradeId){
    	
		System.out.println (gradeId + "Lesson being updated <<<<<<<<<<<<<<<<<<<>>>>>>>>>");	
		
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(gradeId));
		query.fields().include("_id");

		Grade grade = mongoTemplate.findOne(query, Grade.class);
		System.out.println("grade - " + grade);

		Update update = new Update();
		//update.set("age", 100);

		mongoTemplate.updateFirst(query, update, Grade.class);
		
    }
    
    public void delete (String gradeId){
    	
		System.out.println (gradeId + "Grade Id being deleted <<<<<<<<<<<<<<<<<<<>>>>>>>>>");	
		Grade grade = findById(gradeId);
    	mongoTemplate.remove(grade);
    }
    
    
	public Grade findById(String gradeId) {
		Grade retrievedGrade = null;
		
    	// find the saved user again.
    	retrievedGrade = (Grade)mongoTemplate.findById(gradeId, Grade.class, "grades"); //(searchLessonsQuery, Lesson.class, "lessons");
    	System.out.println("find - retrievedGrade : " + retrievedGrade); 
    	   	
    	return retrievedGrade;
	}
	
	public List<Grade> findLessonsByUserId(String userId) {
		System.out.println ("\nLessonRepository::findLessonsBySkillLevelTypeId : " +  userId  + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");	

		ArrayList<Grade> retrievedGradeList = null;
		
    	// query to search lessons
    	Query searchGradeQuery = new Query(Criteria.where("userId").is(userId));

    	// find the saved user again.
    	retrievedGradeList = (ArrayList<Grade>)mongoTemplate.find(searchGradeQuery, Grade.class, "grades");
    	System.out.println("2. find - retrievedGradeList : " + retrievedGradeList); 
    	
    	return retrievedGradeList;
	}

	public List<Grade> findAll() {
		List<Grade> retrievedGradeList = null;
		
    	// find the saved user again.
		retrievedGradeList = (List<Grade>)mongoTemplate.findAll(Grade.class, "grades"); 
    	System.out.println("find - retrievedGradeList : " + retrievedGradeList); 
    	   	
    	return retrievedGradeList;
	}
	
}
