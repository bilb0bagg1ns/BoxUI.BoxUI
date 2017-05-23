package com.box.model.domain;

/**
 * 
 * Primarily build for rendering lessons in a row in the view.
 *
 */
public class LessonRows {

	private Lesson lessonOne;
	private Lesson lessonTwo;
	private Lesson lessonThree;
	private Lesson lessonFour;

	public LessonRows() {
	}

	public Lesson getLessonOne() {
		return lessonOne;
	}

	public void setLessonOne(Lesson lessonOne) {
		this.lessonOne = lessonOne;
	}

	public Lesson getLessonTwo() {
		return lessonTwo;
	}

	public void setLessonTwo(Lesson lessonTwo) {
		this.lessonTwo = lessonTwo;
	}

	public Lesson getLessonThree() {
		return lessonThree;
	}

	public void setLessonThree(Lesson lessonThree) {
		this.lessonThree = lessonThree;
	}

	public Lesson getLessonFour() {
		return lessonFour;
	}

	public void setLessonFour(Lesson lessonFour) {
		this.lessonFour = lessonFour;
	}

	@Override
	public String toString() {
		return "LessonRows [lessonOne=" + lessonOne + ", lessonTwo=" + lessonTwo + ", lessonThree=" + lessonThree
				+ ", lessonFour=" + lessonFour + "]";
	}

}
