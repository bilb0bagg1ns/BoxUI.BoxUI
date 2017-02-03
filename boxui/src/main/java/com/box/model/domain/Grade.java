package com.box.model.domain;

import java.util.Date;

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
@Document(collection = "grade")
public class Grade {

	@Id
	public String id;

	/** associated user */
	private String userId;
	/** lesson associated with grade */
	private String lessonId;
	/** associated skill level type id (Novice, Intermediate, Expert) */
	private String skillLevelTypeId;
	/** pass or fail */
	private String grade;
	/** date challenge taken/grade assigned */
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date gradeDate;

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

	public String getLessonId() {
		return lessonId;
	}

	public void setLessonId(String lessonId) {
		this.lessonId = lessonId;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getSkillLevelTypeId() {
		return skillLevelTypeId;
	}

	public void setSkillLevelTypeId(String skillLevelTypeId) {
		this.skillLevelTypeId = skillLevelTypeId;
	}

	public Date getGradeDate() {
		return gradeDate;
	}

	public void setGradeDate(Date gradeDate) {
		this.gradeDate = gradeDate;
	}

	@Override
	public String toString() {
		return "Grade [id=" + id + ", userId=" + userId + ", lessonId=" + lessonId + ", skillLevelTypeId="
				+ skillLevelTypeId + ", grade=" + grade + ", gradeDate=" + gradeDate + "]";
	}

}
