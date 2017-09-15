package com.box.model.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "skills")
public class Skill {

	@Id
	public String id;
	
	private String name;  // Novice, Intermediate, Expert
	private String shortDescription;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	@Override
	public String toString() {
		return "Skill [id=" + id + ", name=" + name + ", shortDescription=" + shortDescription + "]";
	}
	
	
	
}
