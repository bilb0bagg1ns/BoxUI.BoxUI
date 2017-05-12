package com.box.model.services;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.box.model.data.repository.LessonRepository;
import com.box.model.domain.Lesson;

@Named
public class LessonsProcessingService {

	private final Logger log = LoggerFactory.getLogger(LessonsProcessingService.class);

	@Inject
	private LessonRepository repository;

	public void saveLesson(Lesson lesson) {
		repository.save(lesson);
	}

	public void deleteLesson(String lessonId) {
		repository.delete(lessonId);
	}

	public void upsertLesson(Lesson lesson) {
		// repository.upsert(lesson);

		// can't seem to upsert lesson's skill applicable checkboxes. hence
		// doing this approach
		deleteLesson(lesson.getId());
		saveLesson(lesson);
	}

	public Lesson findLessonByLessonId(String lessonId) {
		Lesson lesson = repository.findLessonByLessonId(lessonId);
		return lesson;
	}

	public List<Lesson> retrieveAllLessons() {

		List<Lesson> lessonsList = repository.retrieveAllLessons();
		return lessonsList;
	}

	public List<Lesson> retrieveLessonsList(String skillLevelTypeId) {

		List<Lesson> lessonsList = repository.findLessonsBySkillLevelTypeId(skillLevelTypeId);
		log.debug(skillLevelTypeId + "<<<<<----");

		return lessonsList;
	}
}
