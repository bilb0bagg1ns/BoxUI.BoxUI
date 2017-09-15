package com.box.model.services;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.box.model.data.repository.OperatingSystemRepository;
import com.box.model.domain.OperatingSystem;


@Named
public class OperatingSystemProcessingService {

	private final Logger log = LoggerFactory.getLogger(OperatingSystemProcessingService.class);

	@Inject
	private OperatingSystemRepository repository;

	public void saveOperatingSystem(OperatingSystem operatingSystem) {
		repository.save(operatingSystem);
	}

	public void deleteOperatingSystem(String operatingSystemId) {
		repository.delete(operatingSystemId);
	}

	public void upsertOperatingSystem(OperatingSystem operatingSystem) {
		// repository.upsert(operatingSystem);

		// can't seem to upsert operatingSystem's skill applicable checkboxes. hence
		// doing this approach
		deleteOperatingSystem(operatingSystem.getId());
		saveOperatingSystem(operatingSystem);
	}

	public OperatingSystem findOperatingSystemByOperatingSystemId(String operatingSystemId) {
		OperatingSystem operatingSystem = repository.findOperatingSystemByOperatingSystemId(operatingSystemId);
		return operatingSystem;
	}

	public List<OperatingSystem> retrieveAllOperatingSystems() {


		List<OperatingSystem> operatingSystemsList = repository.retrieveAllOperatingSystems();
		return operatingSystemsList;
	}
	
}