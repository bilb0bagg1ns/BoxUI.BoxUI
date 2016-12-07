package com.box.model.type;

public enum SkillLevelType {
	NOVICE("Novice", "1", "novice", "Novice"),
	INTERMEDIATE("Intermediate", "2", "intermediate", "Intermediate"),
	EXPERT("Expert", "3", "expert", "Expert");

	private String code;
	private String id;
	private String link;
	private String text;


	
	private SkillLevelType(String code, String id, String link, String text) {
		this.code = code;
		this.id = id;
		this.link = link;
		this.text = text;
	}

	public static SkillLevelType valueOfCode(String code) {
		for (SkillLevelType type : SkillLevelType.values()) {
			if (type.getCode().equals(code)) {
				return type;
			}
		}
		throw new IllegalArgumentException("Invalid code: " + code);
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