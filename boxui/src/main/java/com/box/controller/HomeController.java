package com.box.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.box.model.data.repository.UserRepository;
import com.box.model.domain.Lesson;
import com.box.model.domain.User;
import com.box.model.services.AuthenticationService;
import com.box.model.services.RegistrationService;
import com.box.model.type.SkillLevelType;



@Controller
public class HomeController {
	
//	@Autowired
//	private UserRepository repository;
	@Inject
	private AuthenticationService authenticationService;
	
	@Inject
	private RegistrationService registrationService;
	
	@Inject
	private CoachingEngineController coachingEngineController;
	
	
    @ModelAttribute("allSkillLevelType")
    public SkillLevelType[] populateTypes() {
        return new SkillLevelType[] { SkillLevelType.NOVICE, SkillLevelType.INTERMEDIATE, SkillLevelType.EXPERT };
    }

	
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Principal principal) {
        return principal != null ? "home/homeSignedIn" : "home/homeNotSignedIn";
    }
    
	@RequestMapping("/greeting")
    public String greeting(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }
    
	@RequestMapping(value = "/login")
	public ModelAndView login(ModelAndView model) throws IOException {
		User newUser = new User();
		model.addObject("user", newUser);
//		List<Contact> listContact = contactDAO.list();
//		model.addObject("listContact", listContact);
		model.setViewName("login");

		return model;
	}

	@RequestMapping(value = "/register")
	public ModelAndView register (ModelAndView model, @ModelAttribute User user) throws IOException {
		model.setViewName("register");
		return model;
	}
	
	@RequestMapping(value = "/registeration")
	public ModelAndView registeration (ModelAndView model, @ModelAttribute User user) throws IOException {
		registrationService.register(user);
		model.setViewName("login");
		return model;
	}
	
	@RequestMapping(value = "/levels")
	public ModelAndView authenticate(ModelAndView model, @ModelAttribute User user) throws IOException {	
		// testing REST based call
		ModelMap mm = new ModelMap();
		coachingEngineController.getTest(mm);
		System.out.println (mm.get("test") + "<<<<<<------------");
		
		// authenticate user
        boolean isAuthenticated = authenticationService.isAuthenticated(user);
		if (isAuthenticated){
			//List<Contact> listContact = contactDAO.list();
			//model.addObject("listContact", listContact);
			model.addObject("user", user);
			model.setViewName("listLevels");
		}		
		return model;
	}
	
	@RequestMapping(value = "/novice")
	public ModelAndView novice (HttpSession session, ModelAndView model, @RequestParam("userName") String userName, @RequestParam("levelId") String levelId) throws IOException {
		System.out.println (userName + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");		

		Lesson lesson = new Lesson ();
		lesson.setLevelId(levelId);
		
		model.addObject("lesson", lesson);
		session.setAttribute("lessonSessionAttribute",lesson);
		System.out.println (lesson + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");		

		return model;
	}
	
	@RequestMapping(value = "/lessonEntry")
	public ModelAndView lessonEntry (ModelAndView model, @ModelAttribute Lesson lesson) throws IOException {
		model.setViewName("lessonEntryForm");
		return model;
	}

}
