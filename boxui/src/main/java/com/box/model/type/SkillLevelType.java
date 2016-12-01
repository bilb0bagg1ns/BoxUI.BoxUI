package com.box.model.type;

public enum SkillLevelType {
	NOVICE("Novice", "novice", "Novice"),
	INTERMEDIATE("Intermediate", "intermediate", "Intermediate"),
	EXPERT("Expert", "expert", "Expert");

	private String code;
	private String link;
	private String text;


	
	private SkillLevelType(String code, String link, String text) {
		this.code = code;
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



}