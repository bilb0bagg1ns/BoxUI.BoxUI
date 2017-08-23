package com.box.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import com.box.model.domain.TestBody;

/**
 * Coaching Engine Controller 
 */
@Controller
@RequestMapping("/coachingengine")
public class CoachingEngineController {

	private final Logger log = LoggerFactory.getLogger(CoachingEngineController.class);

	@RequestMapping(value = "/{name}", method = RequestMethod.GET)
	public String getTestName(@PathVariable String name, ModelMap model) {

		model.addAttribute("testName", name);
		return "list";

	}

	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public String getTest(ModelMap model) {
		RestTemplate restTemplate = new RestTemplate();

		TestBody testBody = restTemplate.getForObject("http://localhost:8090/engine/test", TestBody.class);
		log.debug(testBody.toString());
		model.addAttribute("test", testBody);
		return "list";

		// Sample REST call
		// RestTemplate restTemplate = new RestTemplate();
		// Quote quote = restTemplate.getForObject(
		// "http://gturnquist-quoters.cfapps.io/api/random", Quote.class);
		// //log.debug (quote.toString());
		// model.addAttribute("test", quote);
		// return "list";

	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String getDefaultname(ModelMap model) {

		model.addAttribute("name", "this is default name");
		return "list";

	}
}
