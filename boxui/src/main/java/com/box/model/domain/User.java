package com.box.model.domain;

import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;
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

	/** list of lessons associated with this User 
	 *  Need this association because, the admin via the User flow
	 *  can optionally assign a lesson to a user and when the lesson is
	 *  deleted or renamed, it has to be reflected at the User level.
	 *  
	 *  Note: Tried List<Users>, but had challenges manipulation at the UI/ThymeLeaf level, hence
	 *  had to revert to String list.
	 *  
	 *  List elements are in this format: lessonId;lessonName&
	 *  - a semicolon separates the lesson id and the lesson name
	 *  - an ampersand separates the pairs of elements
	 *  Ex format: [5915ed47a6b94c2fc419e2aa;Access Rights Management&, 5a0b48ab3eba6c4c04ad61e4;test&]
	 */
	private List<String> lessonIdNameList;
	

	/**
	 * Iterate over lessonIdNameList and return list of lessons
	 * 
	 * @param lessonId
	 * @return
	 */
	public String getAssignedLessons(){
		String assignedLessons = null;
		
		// iterate over lessonIdNameList 
		if ((lessonIdNameList != null) && (!lessonIdNameList.isEmpty())) {
				for (String lessonIdName: lessonIdNameList) {
				  StringTokenizer st = new StringTokenizer(lessonIdName,";");
				  while (st.hasMoreTokens()) {
				     String lessonIdToken = st.nextToken();
				     String lessonToken = StringUtils.remove(st.nextToken(), '&');
				    	//lessonList.add(i, lessonToken);
						if (assignedLessons == null) {
						  assignedLessons = lessonToken;	
						} else {
							assignedLessons = lessonToken + ", "  + assignedLessons;
						}
				    	
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
		return lessonIdNameList;
	}

	public void setLessonList(List<String> lessonList) {
		this.lessonIdNameList = lessonList;
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

	public List<String> getLessonIdNameList() {
		return lessonIdNameList;
	}


	public void setLessonIdNameList(List<String> lessonIdNameList) {
		this.lessonIdNameList = lessonIdNameList;
	}
	
	@Override
	public String toString() {
		return "User [id=" + id + ", operatingSystemIdList=" + operatingSystemIdList + ", lessonIdList="
				+ lessonIdList + ", userName=" + userName + ", password=" + password + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", createdDate=" + createdDate + "]";
	}
}
