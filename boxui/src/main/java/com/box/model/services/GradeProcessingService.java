package com.box.model.services;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import com.box.model.data.repository.GradeRepository;
import com.box.model.domain.Grade;
import com.box.model.domain.Lesson;

@Named
public class GradeProcessingService {
	
	@Inject
	private GradeRepository repository;


	public void save (Grade grade){
		repository.save(grade);
	}

	public void delete (String id){
		repository.delete(id);
	}
	
	
	public Grade findById(String id){
		Grade grade = repository.findById(id);
		return grade;
	}
	
	public List<Grade> findLessonsByUserId (String userId){
		return repository.findLessonsByUserId(userId);
	}
	
	public List<Grade> findAll (){
		
		List<Grade>  gradeList = repository.findAll();
		System.out.println(gradeList + "<<<<<----");
		
		return gradeList;
	}
}
