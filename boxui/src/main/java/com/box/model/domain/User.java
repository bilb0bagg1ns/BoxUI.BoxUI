package com.box.model.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

/**
 * Box UI user.
 * 
 * @author mike.prasad
 *
 */
@Document(collection = "users")
public class User {

	@Id
	public String id;


	/** list of admin optionally assigned operating systems */
	private List<String> operatingSystemIdList;
	
	/** list of admin optionally assigned lessons */
	private List<String> lessonIdList;

	private List<String> lessonList = new ArrayList<String>();
	
	public String getAssignedLessons() {
		String assignedLessons = null;
		if ((lessonList != null) && (!lessonList.isEmpty())){
			for (String lesson: lessonList) {
				if (assignedLessons == null) {
				  assignedLessons = lesson;	
				} else {
					assignedLessons = lesson + " "  + assignedLessons;
				}
				
			}
		} 
		return assignedLessons;
	}
	@NotNull
	@Size(min = 2, max = 30)
	private String userName;

	@NotNull
	@Size(min = 2, max = 30)
	private String password;

	@NotNull
	@Size(min = 2, max = 30)
	private String firstName;

	@NotNull
	@Size(min = 2, max = 30)
	private String lastName;

	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date createdDate;

	public User() {
	}

	public User(String userName, String password) {
		super();
		this.userName = userName;
		this.password = password;
	}

	public User(String userName, String password, String firstName, String lastName) {
		super();
		this.userName = userName;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	

	public List<String> getOperatingSystemIdList() {
		return operatingSystemIdList;
	}

	public List<String> getLessonIdList() {
		return lessonIdList;
	}

	public void setOperatingSystemIdList(List<String> operatingSystemIdList) {
		this.operatingSystemIdList = operatingSystemIdList;
	}

	public void setLessonIdList(List<String> lessonIdList) {
		this.lessonIdList = lessonIdList;
	}

		
	public List<String> getLessonList() {
		return lessonList;
	}

	public void setLessonList(List<String> lessonList) {
		this.lessonList = lessonList;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFullName() {
		return lastName + "," + firstName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", operatingSystemIdList=" + operatingSystemIdList + ", lessonIdList="
				+ lessonIdList + ", userName=" + userName + ", password=" + password + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", createdDate=" + createdDate + "]";
	}
}
