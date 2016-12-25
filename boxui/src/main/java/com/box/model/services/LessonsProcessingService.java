package com.box.model.services;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import com.box.model.data.repository.LessonRepository;
import com.box.model.domain.Lesson;

@Named
public class LessonsProcessingService {
	
	@Inject
	private LessonRepository repository;


	public void saveLesson (Lesson lesson){
		repository.save(lesson);
	}

	public void deleteLesson (String lessonId){
		repository.delete(lessonId);
	}
	
	public void upsertLesson (Lesson lesson){
		repository.upsert(lesson);
	}
	
	
	public Lesson findLessonByLessonId(String lessonId){
		Lesson lesson = repository.findLessonByLessonId(lessonId);
		return lesson;
	}
	
	public List<Lesson> retrieveLessonsList (String skillLevelTypeId){
		
		List<Lesson>  lessonsList = repository.findLessonsBySkillLevelTypeId(skillLevelTypeId);
		System.out.println(skillLevelTypeId + "<<<<<----");
		
		return lessonsList;
	}
}
