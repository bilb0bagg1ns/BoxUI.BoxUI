package com.box.model.domain;

import java.util.List;

/**
 * To capture a User and optionally admin assigned operating systems and lessons.
 *  
 * @author mike.prasad
 *
 */
public class UserComposite {

	private User user;
	private List<OperatingSystem> operatingSystemList;
	private List<Lesson> lessonList;
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public List<OperatingSystem> getOperatingSystemList() {
		return operatingSystemList;
	}
	public void setOperatingSystemList(List<OperatingSystem> operatingSystemList) {
		this.operatingSystemList = operatingSystemList;
	}
	public List<Lesson> getLessonList() {
		return lessonList;
	}
	public void setLessonList(List<Lesson> lessonList) {
		this.lessonList = lessonList;
	}
	@Override
	public String toString() {
		return "UserComposite [user=" + user + ", operatingSystemList=" + operatingSystemList + ", lessonList="
				+ lessonList + "]";
	}
	
	
}
