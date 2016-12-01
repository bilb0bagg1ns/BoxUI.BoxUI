package com.box.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import com.box.model.domain.Quote;

@Controller
@RequestMapping("/coachingengine")
public class CoachingEngineController {

	@RequestMapping(value = "/{name}", method = RequestMethod.GET)
	public String getTestName(@PathVariable String name, ModelMap model) {

		model.addAttribute("testName", name);
		return "list";

	}

	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public String getTest(ModelMap model) {

		// TestBody testBody = restTemplate.getForObject(
		// "http://localhost:8090/engine/test", TestBody.class);
		//  System.out.println(testBody.toString());
        //	model.addAttribute("test", quote);
        //	return "list";

		// Sample REST call
		RestTemplate restTemplate = new RestTemplate();
		Quote quote = restTemplate.getForObject(
		"http://gturnquist-quoters.cfapps.io/api/random", Quote.class);
		//System.out.println (quote.toString());
		
		model.addAttribute("test", quote);
		return "list";

	}
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String getDefaultname(ModelMap model) {

		model.addAttribute("name", "this is default name");
		return "list";

	}
}
