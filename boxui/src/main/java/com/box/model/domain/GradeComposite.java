package com.box.model.domain;

public class GradeComposite {
	
	private User user;
	private Grade grade;
	private Lesson lesson;
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Grade getGrade() {
		return grade;
	}
	public void setGrade(Grade grade) {
		this.grade = grade;
	}
	public Lesson getLesson() {
		return lesson;
	}
	public void setLesson(Lesson lesson) {
		this.lesson = lesson;
	}
	@Override
	public String toString() {
		return "GradeComposite [user=" + user + ", grade=" + grade + ", lesson=" + lesson + "]";
	}
		
}
