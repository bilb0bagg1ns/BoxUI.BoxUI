package com.box.model.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Box UI user.
 * 
 * @author mike.prasad
 *
 */
@Document(collection = "grade")
public class Grade {

    @Id
    public String id;

    /** associated user */
    private String userId;
    /** lesson associated with grade */
    private String lessionId;
    /** pass or fail */
	private String grade;
	
	
	public Grade() {
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}


	public String getLessionId() {
		return lessionId;
	}


	public void setLessionId(String lessionId) {
		this.lessionId = lessionId;
	}


	public String getGrade() {
		return grade;
	}


	public void setGrade(String grade) {
		this.grade = grade;
	}


	@Override
	public String toString() {
		return "Grade [id=" + id + ", userId=" + userId + ", lessionId=" + lessionId + ", grade=" + grade + "]";
	}
		
}
