package com.box.model.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "subjectFocus")
public class SubjectFocus {

	@Id
	public String id;
	private String operatingSystem;
	private String focusArea;

	public SubjectFocus() {
	}

	public SubjectFocus(String id, String operatingSystem, String focusArea) {
		super();
		this.id = id;
		this.operatingSystem = operatingSystem;
		this.focusArea = focusArea;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOperatingSystem() {
		return operatingSystem;
	}

	public void setOperatingSystem(String operatingSystem) {
		this.operatingSystem = operatingSystem;
	}

	public String getFocusArea() {
		return focusArea;
	}

	public void setFocusArea(String focusArea) {
		this.focusArea = focusArea;
	}

	@Override
	public String toString() {
		return "SubjectFocus [id=" + id + ", operatingSystem=" + operatingSystem + ", focusArea=" + focusArea + "]";
	}

}
