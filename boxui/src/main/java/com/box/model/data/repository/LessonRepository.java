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

import com.box.model.domain.Lesson;

@Repository
public class LessonRepository {

	private final Logger log = LoggerFactory.getLogger(LessonRepository.class);

	@Autowired
	MongoTemplate mongoTemplate;

	public void save(Lesson lesson) {

		log.debug(lesson + "Lesson being saved <<<<<<<<<<<<<<<<<<<>>>>>>>>>");

		Query query = new Query();
		query.addCriteria(Criteria.where("name").is(lesson.getName()));
		query.fields().include("name");
		Lesson lessonStored = mongoTemplate.findOne(query, Lesson.class);

		if (lessonStored == null) {
			mongoTemplate.save(lesson, "lessons");
		}
	}

	public void update(String lessonId) {

		log.debug(lessonId + "Lesson being updated <<<<<<<<<<<<<<<<<<<>>>>>>>>>");

		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(lessonId));
		query.fields().include("_id");

		Lesson lesson = mongoTemplate.findOne(query, Lesson.class);
		log.debug("lesson - " + lesson);

		Update update = new Update();
		// update.set("age", 100);

		mongoTemplate.updateFirst(query, update, Lesson.class);

	}

	public void upsert(Lesson lesson) {

		log.debug(lesson + "Lesson being upserted <<<<<<<<<<<<<<<<<<<>>>>>>>>>");

		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(lesson.getId()));
		query.fields().include("_id");

		Update update = new Update();
		update.set("id", lesson.getId());
		update.set("skillLevelTypeId", lesson.getSkillLevelTypeId());
		update.set("name", lesson.getName());
		update.set("shortDescription", lesson.getShortDescription());
		update.set("longDescription", lesson.getLongDescription());

		mongoTemplate.upsert(query, update, Lesson.class);

	}

	public void delete(String lessonId) {

		log.debug(lessonId + "Lesson ID being deleted <<<<<<<<<<<<<<<<<<<>>>>>>>>>");
		Lesson lesson = findLessonByLessonId(lessonId);
		mongoTemplate.remove(lesson);
	}

	public List<Lesson> retrieveAllLessons() {

		log.debug("\nLessonRepository::findAllLessons " + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");
		ArrayList<Lesson> retrievedLessonsList = null;
		// find all lessons
		retrievedLessonsList = (ArrayList<Lesson>) mongoTemplate.findAll(Lesson.class);
		log.debug("retrieveAllLessons : " + retrievedLessonsList);
		return retrievedLessonsList;

	}

	public Lesson findByUserNamePassword(Query query) {
		return mongoTemplate.findOne(query, Lesson.class, "lessons");
	}

	public Lesson findLessonByLessonId(String lessonId) {
		Lesson retrievedLesson = null;

		// query to search lessons
		Query searchLessonsQuery = new Query(Criteria.where("_id").is(lessonId));

		// find the saved user again.
		retrievedLesson = (Lesson) mongoTemplate.findById(lessonId, Lesson.class, "lessons"); // (searchLessonsQuery,
																								// Lesson.class,
																								// "lessons");
		log.debug("find - retrievedLesson : " + retrievedLesson);

		return retrievedLesson;
	}

	public List<Lesson> findLessonsBySkillLevelTypeId(String skillLevelTypeId) {
		log.debug("\nLessonRepository::findLessonsBySkillLevelTypeId : " + skillLevelTypeId
				+ "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");

		ArrayList<Lesson> retrievedLessonsList = null;

		// query to search lessons
		Query searchLessonsQuery = new Query(Criteria.where("skillLevelTypeId").is(skillLevelTypeId));

		// find the saved user again.
		retrievedLessonsList = (ArrayList<Lesson>) mongoTemplate.find(searchLessonsQuery, Lesson.class, "lessons");
		log.debug("2. find - retrievedLessonsList : " + retrievedLessonsList);

		return retrievedLessonsList;
	}

}
