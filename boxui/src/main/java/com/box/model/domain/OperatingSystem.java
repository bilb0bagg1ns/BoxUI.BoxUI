package com.box.model.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "operatingsystems")
public class OperatingSystem {

	@Id
	public String id;
	
	private String name;
	private String version;	
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
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getShortDescription() {
		return shortDescription;
	}
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}
	@Override
	public String toString() {
		return "OperatingSystem [id=" + id + ", name=" + name + ", version=" + version + ", shortDescription="
				+ shortDescription + "]";
	}
	
}
