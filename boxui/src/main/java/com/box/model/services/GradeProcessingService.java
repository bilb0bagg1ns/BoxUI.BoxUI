package com.box.model.services;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.box.model.data.repository.GradeRepository;
import com.box.model.data.repository.LessonRepository;
import com.box.model.data.repository.UserRepository;
import com.box.model.domain.Grade;
import com.box.model.domain.GradeComposite;
import com.box.model.domain.Lesson;
import com.box.model.domain.User;

@Named
public class GradeProcessingService {

	private final Logger log = LoggerFactory.getLogger(GradeProcessingService.class);

	@Inject
	private GradeRepository repository;

	@Inject
	LessonRepository lessonRepository;

	@Inject
	UserRepository userRepository;

	private GradeComposite gradeComposite;

	public void save(Grade grade) {
		repository.save(grade);
	}

	public void delete(String id) {
		repository.delete(id);
	}

	public Grade findById(String id) {
		Grade grade = repository.findById(id);
		return grade;
	}

	public List<GradeComposite> findGradeByUserId(String userId) {
		List<GradeComposite> gradeCompositeList = new ArrayList<GradeComposite>();
		List<Grade> gradeList = repository.findGradeByUserId(userId);
		for (Grade grade : gradeList) {
			GradeComposite gradeComposite = new GradeComposite();
			gradeComposite.setGrade(grade);
			// get lesson details
			Lesson lesson = lessonRepository.findLessonByLessonId(grade.getLessonId());
			gradeComposite.setLesson(lesson);
			gradeCompositeList.add(gradeComposite);
		}

		return gradeCompositeList;
	}

	public List<GradeComposite> findAllDetails(String userId) {
		List<GradeComposite> gradeCompositeList = new ArrayList<GradeComposite>();
		List<Grade> gradeList = repository.findAllGrades();
		// fetch lesson and user details
		for (Grade grade : gradeList) {
			GradeComposite gradeComposite = new GradeComposite();
			gradeComposite.setGrade(grade);
			// get lesson details
			Lesson lesson = lessonRepository.findLessonByLessonId(grade.getLessonId());
			gradeComposite.setLesson(lesson);
			// get user details
			User user = userRepository.findById(grade.getUserId());
			gradeComposite.setUser(user);
			gradeCompositeList.add(gradeComposite);
		}

		return gradeCompositeList;
	}

	public List<Grade> findAllGrades() {

		List<Grade> gradeList = repository.findAllGrades();
		log.debug(gradeList + "<<<<<----");

		return gradeList;
	}
}
