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

import com.box.model.domain.OperatingSystem;

@Repository
public class OperatingSystemRepository {

	private final Logger log = LoggerFactory.getLogger(OperatingSystemRepository.class);

	@Autowired
	MongoTemplate mongoTemplate;

	public void save(OperatingSystem operatingSystem) {

		log.debug(operatingSystem + "OperatingSystem being saved <<<<<<<<<<<<<<<<<<<>>>>>>>>>");

		Query query = new Query();
		query.addCriteria(Criteria.where("name").is(operatingSystem.getName()));
		query.fields().include("name");
		OperatingSystem operatingSystemStored = mongoTemplate.findOne(query, OperatingSystem.class);

		if (operatingSystemStored == null) {
			mongoTemplate.save(operatingSystem, "operatingSystems");
		}
	}

	public void update(String operatingSystemId) {

		log.debug(operatingSystemId + "OperatingSystem being updated <<<<<<<<<<<<<<<<<<<>>>>>>>>>");

		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(operatingSystemId));
		query.fields().include("_id");

		OperatingSystem operatingSystem = mongoTemplate.findOne(query, OperatingSystem.class);
		log.debug("operatingSystem - " + operatingSystem);

		Update update = new Update();
		// update.set("age", 100);

		mongoTemplate.updateFirst(query, update, OperatingSystem.class);

	}

	public void upsert(OperatingSystem operatingSystem) {

		log.debug(operatingSystem + "OperatingSystem being upserted <<<<<<<<<<<<<<<<<<<>>>>>>>>>");

		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(operatingSystem.getId()));
		query.fields().include("_id");

		Update update = new Update();
		update.set("id", operatingSystem.getId());
		update.set("name", operatingSystem.getName());
		update.set("version", operatingSystem.getVersion());
		update.set("shortDescription", operatingSystem.getShortDescription());

		mongoTemplate.upsert(query, update, OperatingSystem.class);

	}

	public void delete(String operatingSystemId) {

		log.debug(operatingSystemId + "OperatingSystem ID being deleted <<<<<<<<<<<<<<<<<<<>>>>>>>>>");
		OperatingSystem operatingSystem = findOperatingSystemByOperatingSystemId(operatingSystemId);
		mongoTemplate.remove(operatingSystem);
	}

	public List<OperatingSystem> retrieveAllOperatingSystems() {

		log.debug("\nOperatingSystemRepository::retrieveAllOperatingSystems " + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");
		ArrayList<OperatingSystem> retrievedOperatingSystemsList = null;
		// find all operatingSystems
		retrievedOperatingSystemsList = (ArrayList<OperatingSystem>) mongoTemplate.findAll(OperatingSystem.class);
		log.debug("findAllOperatingSystems : " + retrievedOperatingSystemsList);
		return retrievedOperatingSystemsList;

	}

	public OperatingSystem findByUserNamePassword(Query query) {
		return mongoTemplate.findOne(query, OperatingSystem.class, "operatingSystems");
	}

	public OperatingSystem findOperatingSystemByOperatingSystemId(String operatingSystemId) {
		OperatingSystem retrievedOperatingSystem = null;

		// query to search operatingSystems
		Query searchOperatingSystemsQuery = new Query(Criteria.where("_id").is(operatingSystemId));

		// find the saved user again.
		retrievedOperatingSystem = (OperatingSystem) mongoTemplate.findById(operatingSystemId, OperatingSystem.class, "operatingSystems"); // (searchOperatingSystemsQuery,
																								// OperatingSystem.class,
																								// "operatingSystems");
		log.debug("find - retrievedOperatingSystem : " + retrievedOperatingSystem);

		return retrievedOperatingSystem;
	}
}
