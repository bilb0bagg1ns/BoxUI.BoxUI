package com.box.model.data.repository;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.box.model.domain.Grade;

@Repository
public class GradeRepository {

	private final Logger log = LoggerFactory.getLogger(GradeRepository.class);

	@Autowired
	MongoTemplate mongoTemplate;

	public void save(Grade grade) {

		log.debug(grade + "Grade being saved <<<<<<<<<<<<<<<<<<<>>>>>>>>>");
		mongoTemplate.save(grade, "grades");
	}

	public void update(String gradeId) {

		log.debug(gradeId + "Lesson being updated <<<<<<<<<<<<<<<<<<<>>>>>>>>>");

		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(gradeId));
		query.fields().include("_id");

		Grade grade = mongoTemplate.findOne(query, Grade.class);
		log.debug("grade - " + grade);

		Update update = new Update();
		// update.set("age", 100);

		mongoTemplate.updateFirst(query, update, Grade.class);

	}

	public void delete(String gradeId) {

		log.debug(gradeId + "Grade Id being deleted <<<<<<<<<<<<<<<<<<<>>>>>>>>>");
		Grade grade = findById(gradeId);
		mongoTemplate.remove(grade);
	}

	public Grade findById(String gradeId) {
		Grade retrievedGrade = null;

		// find the saved user again.
		retrievedGrade = (Grade) mongoTemplate.findById(gradeId, Grade.class, "grades"); // (searchLessonsQuery,
																							// Lesson.class,
																							// "lessons");
		log.debug("find - retrievedGrade : " + retrievedGrade);

		return retrievedGrade;
	}

	public List<Grade> findGradeByUserId(String userId) {
		log.debug("\nLessonRepository::findLessonsBySkillLevelTypeId : " + userId + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");

		ArrayList<Grade> retrievedGradeList = null;

		// query to search lessons
		Query searchGradeQuery = new Query(Criteria.where("userId").is(userId));

		// find the saved user again.
		retrievedGradeList = (ArrayList<Grade>) mongoTemplate.find(searchGradeQuery, Grade.class, "grades");
		log.debug("2. find - retrievedGradeList : " + retrievedGradeList);

		return retrievedGradeList;
	}

	public List<Grade> findAllGrades() {
		List<Grade> retrievedGradeList = null;

		// find the saved user again.
		retrievedGradeList = (List<Grade>) mongoTemplate.findAll(Grade.class, "grades");
		log.debug("find - retrievedGradeList : " + retrievedGradeList);

		return retrievedGradeList;
	}

}
