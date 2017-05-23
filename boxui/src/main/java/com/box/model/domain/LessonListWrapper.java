package com.box.model.domain;

import java.util.ArrayList;

/**
 * Box UI user.
 * 
 */

public class LessonListWrapper {

	private ArrayList<Lesson> lessonList;

	private ArrayList<LessonRows> lessonRowsList;

	private LessonRows lessonRows;

	public ArrayList<Lesson> getLessonList() {
		return lessonList;
	}

	public void setLessonList(ArrayList<Lesson> lessonList) {
		this.lessonList = lessonList;
	}

	public ArrayList<LessonRows> getLessonRowsList() {
		return lessonRowsList;
	}

	public void setLessonRowsList(ArrayList<LessonRows> lessonRowsList) {
		this.lessonRowsList = lessonRowsList;
	}

	public LessonRows getLessonRows() {
		return lessonRows;
	}

	public void setLessonRows(LessonRows lessonRows) {
		this.lessonRows = lessonRows;
	}

}
