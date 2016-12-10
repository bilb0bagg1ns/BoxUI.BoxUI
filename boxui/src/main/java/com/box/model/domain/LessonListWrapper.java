package com.box.model.domain;

import java.util.ArrayList;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Box UI user.
 * 
 * @author mike.prasad
 *
 */

public class LessonListWrapper {

	private ArrayList<Lesson> lessonList;

	public ArrayList<Lesson> getLessonList() {
		return lessonList;
	}

	public void setLessonList(ArrayList<Lesson> lessonList) {
		this.lessonList = lessonList;
	}
	 
	 
	 
}
