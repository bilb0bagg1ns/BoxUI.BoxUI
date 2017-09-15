package com.box.model.services;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.box.model.data.repository.SkillRepository;
import com.box.model.domain.Skill;


@Named
public class SkillProcessingService {

	private final Logger log = LoggerFactory.getLogger(SkillProcessingService.class);

	@Inject
	private SkillRepository repository;

	public void saveSkill(Skill skill) {
		repository.save(skill);
	}

	public void deleteSkill(String skillId) {
		repository.delete(skillId);
	}

	public void upsertSkill(Skill skill) {
		// repository.upsert(skill);

		// can't seem to upsert skill's skill applicable checkboxes. hence
		// doing this approach
		deleteSkill(skill.getId());
		saveSkill(skill);
	}

	public Skill findSkillBySkillId(String skillId) {
		Skill skill = repository.findSkillBySkillId(skillId);
		return skill;
	}

	public List<Skill> retrieveAllSkills() {

		List<Skill> skillsList = repository.retrieveAllSkills();
		return skillsList;
	}
	
}