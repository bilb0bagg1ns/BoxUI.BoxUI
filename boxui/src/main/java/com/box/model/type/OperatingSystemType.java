package com.box.model.type;

public enum OperatingSystemType {
	LINUX("Linux", "1", "linux", "Linux"), WINDOWS("Windows", "2", "windows", "Windows");

	private String code;
	private String id;
	private String link;
	private String text;

	private OperatingSystemType(String code, String id, String link, String text) {
		this.code = code;
		this.id = id;
		this.link = link;
		this.text = text;
	}

	public static OperatingSystemType valueOfCode(String code) {
		for (OperatingSystemType type : OperatingSystemType.values()) {
			if (type.getCode().equals(code)) {
				return type;
			}
		}
		throw new IllegalArgumentException("Invalid code: " + code);
	}

	public static OperatingSystemType valueOfId(String id) {
		for (OperatingSystemType type : OperatingSystemType.values()) {
			if (type.getId().equals(id)) {
				return type;
			}
		}
		throw new IllegalArgumentException("Invalid id: " + id);
	}

	public String getCode() {
		return code;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setCode(String code) {
		this.code = code;
	}

}