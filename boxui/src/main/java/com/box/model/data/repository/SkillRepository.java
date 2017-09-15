package com.box.model.data.repository;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.box.model.domain.Skill;

@Repository
public class SkillRepository {

	private final Logger log = LoggerFactory.getLogger(SkillRepository.class);

	@Autowired
	MongoTemplate mongoTemplate;

	public void save(Skill skill) {

		log.debug(skill + "Skill being saved <<<<<<<<<<<<<<<<<<<>>>>>>>>>");

		Query query = new Query();
		query.addCriteria(Criteria.where("name").is(skill.getName()));
		query.fields().include("name");
		Skill skillsStored = mongoTemplate.findOne(query, Skill.class);

		if (skillsStored == null) {
			mongoTemplate.save(skill, "skills");
		}
	}

	public void update(String skillId) {

		log.debug(skillId + "Skill being updated <<<<<<<<<<<<<<<<<<<>>>>>>>>>");

		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(skillId));
		query.fields().include("_id");

		Skill skill = mongoTemplate.findOne(query, Skill.class);
		log.debug("skill - " + skill);

		Update update = new Update();
		// update.set("age", 100);

		mongoTemplate.updateFirst(query, update, Skill.class);

	}

	public void upsert(Skill skill) {

		log.debug(skill + "Skill being upserted <<<<<<<<<<<<<<<<<<<>>>>>>>>>");

		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(skill.getId()));
		query.fields().include("_id");

		Update update = new Update();
		update.set("id", skill.getId());
		update.set("name", skill.getName());
		update.set("shortDescription", skill.getShortDescription());

		mongoTemplate.upsert(query, update, Skill.class);

	}

	public void delete(String skillId) {

		log.debug(skillId + "Skill ID being deleted <<<<<<<<<<<<<<<<<<<>>>>>>>>>");
		Skill skill = findSkillBySkillId(skillId);
		mongoTemplate.remove(skill);
	}

	public List<Skill> retrieveAllSkills() {

		log.debug("\nSkillRepository::retrieveAllSkillss " + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");
		ArrayList<Skill> retrievedSkillsList = null;
		// find all Skills
		retrievedSkillsList = (ArrayList<Skill>) mongoTemplate.findAll(Skill.class);
		log.debug("retrieveAllSkills : " + retrievedSkillsList);
		return retrievedSkillsList;

	}

	public Skill findByUserNamePassword(Query query) {
		return mongoTemplate.findOne(query, Skill.class, "skills");
	}

	public Skill findSkillBySkillId(String skillId) {
		Skill retrievedSkill = null;

		// find the saved user again.
		retrievedSkill = (Skill) mongoTemplate.findById(skillId, Skill.class, "skills"); // (searchOperatingSystemsQuery,
																								// Skill.class,
																								// "operatingSystems");
		log.debug("find - retrievedSkill : " + retrievedSkill);

		return retrievedSkill;
	}
}
