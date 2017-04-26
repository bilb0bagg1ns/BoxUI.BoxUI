package com.box.model.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Box UI user.
 * 
 * @author mike.prasad
 *
 */
@Document(collection = "lessons")
public class Lesson {

	@Id
	public String id;

	/**
	 * Id of skill level associated with this lesson - Novice, Intermediate,
	 * Expert
	 */
	private String skillLevelTypeId;
	private ArrayList<Integer> skillsLevelsApplicableTo;
	private String name;
	private String shortDescription;
	private String longDescription;
	// Checkbox logic comes from :
	// http://stackoverflow.com/questions/17692941/values-for-thfield-attributes-in-checkbox
	private List<String> checkedSkillsLevelsApplicableTo;

	public Lesson() {
	}

	// public Lesson(String id, String name, String shortDescription, String
	// longDescription) {
	// super();
	// this.id = id;
	// this.name = name;
	// this.shortDescription = shortDescription;
	// this.longDescription = longDescription;
	// }

	public Lesson(String id, String skillLevelTypeId, String name, String shortDescription, String longDescription) {
		super();
		this.id = id;
		this.skillLevelTypeId = skillLevelTypeId;
		this.name = name;
		this.shortDescription = shortDescription;
		this.longDescription = longDescription;
	}

	public Lesson(String id, String skillLevelTypeId, ArrayList<Integer> skillsLevelsApplicableTo, String name,
			String shortDescription, String longDescription) {
		super();
		this.id = id;
		this.skillLevelTypeId = skillLevelTypeId;
		this.skillsLevelsApplicableTo = skillsLevelsApplicableTo;
		this.name = name;
		this.shortDescription = shortDescription;
		this.longDescription = longDescription;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSkillLevelTypeId() {
		return skillLevelTypeId;
	}

	public void setSkillLevelTypeId(String skillLevelTypeId) {
		this.skillLevelTypeId = skillLevelTypeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getLongDescription() {
		return longDescription;
	}

	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}

	public ArrayList<Integer> getSkillsLevelsApplicableTo() {
		return skillsLevelsApplicableTo;
	}

	public void setSkillsLevelsApplicableTo(ArrayList<Integer> skillsLevelsApplicableTo) {
		this.skillsLevelsApplicableTo = skillsLevelsApplicableTo;
	}

	public List<String> getCheckedSkillsLevelsApplicableTo() {
		return checkedSkillsLevelsApplicableTo;
	}

	public void setCheckedSkillsLevelsApplicableTo(List<String> checkedSkillsLevelsApplicableTo) {
		this.checkedSkillsLevelsApplicableTo = checkedSkillsLevelsApplicableTo;
	}

	@Override
	public String toString() {
		return "Lesson [id=" + id + ", skillLevelTypeId=" + skillLevelTypeId + ", name=" + name + ", shortDescription="
				+ shortDescription + ", longDescription=" + longDescription + "]";
	}

}
