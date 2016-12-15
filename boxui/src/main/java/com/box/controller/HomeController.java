package com.box.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.box.model.domain.Lesson;
import com.box.model.domain.LessonListWrapper;
import com.box.model.domain.User;
import com.box.model.services.AuthenticationService;
import com.box.model.services.LessonsProcessingService;
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
	private LessonsProcessingService lessonsProcessingService;

	
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
	public ModelAndView authenticate(HttpSession session, ModelAndView model, @ModelAttribute User user) throws IOException {	
		// testing REST based call
		ModelMap mm = new ModelMap();
		coachingEngineController.getTest(mm);
		System.out.println (mm.get("test") + "<<<<<<------------");
		
		// authenticate user
        boolean isAuthenticated = authenticationService.isAuthenticated(user);
		if (isAuthenticated){
			if (user.getUserName().equals("admin")){
				// add to session so it's available for subsequent screens
				// adding to session in lieu of trying to pass it via the hfref link
				// TODO: Ensure on logoff, to invalidate session!
				session.setAttribute("userSessionAttribute",user);
				
			}
			//List<Contact> listContact = contactDAO.list();
			//model.addObject("listContact", listContact);
			model.addObject("user", user);
			model.setViewName("listLevels");
		}		
		return model;
	}
	
	@RequestMapping(value = "/novice")
	public ModelAndView novice (HttpSession session, Model m, ModelAndView model, @RequestParam("userName") String userName, @RequestParam("skillLevelTypeId") String skillLevelTypeId) throws IOException {
		System.out.println (userName + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");		

			Lesson lesson = new Lesson ();
			lesson.setSkillLevelTypeId(skillLevelTypeId);
			
			// add to session so it's available for subsequent screens
			// adding to session in lieu of trying to pass it via the hfref link
			// TODO: Ensure on logoff, to invalidate session!
			session.setAttribute("lessonSessionAttribute",lesson);
			System.out.println (lesson + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");		
			
		    // retrieve lessons
		    ArrayList<Lesson> lessonsList = (ArrayList<Lesson>) lessonsProcessingService.retrieveLessonsList(skillLevelTypeId);
		    m.addAttribute("lessonListTmp", lessonsList);

		    // insert list into wrapper
		    LessonListWrapper lessonListWrapperTmp = new LessonListWrapper();
		    lessonListWrapperTmp.setLessonList(lessonsList);
		    m.addAttribute("lessonListWrapper", lessonListWrapperTmp);

			// if user is admin, send them to the flow to add/edit lessons
		if (userName.equals("admin")){
		   model.setViewName("admin/noviceAdminEntry");   
		} else {
			model.setViewName("novice");
		}
		return model;
	}
	
	@RequestMapping(value = "/lessonEntry")
	public ModelAndView lessonEntry (ModelAndView model, @ModelAttribute Lesson lesson) throws IOException {
		model.addObject("lesson", lesson);
		model.setViewName("lessonEntryForm");
		return model;
	}
	
	@RequestMapping(value = "/lessonEntryUpdate")
	public ModelAndView lessonEntryUpdate (ModelAndView model, @ModelAttribute Lesson lesson) throws IOException {
		model.addObject("lesson", lesson);
		model.setViewName("admin/lessonEntryUpdateForm");
		return model;
	}
	
	@RequestMapping(value = "/saveLesson")
	public ModelAndView saveLesson (HttpSession session, ModelAndView model, Model m, @ModelAttribute Lesson lesson, @ModelAttribute LessonListWrapper lessonListWrapper, @ModelAttribute ArrayList<Lesson> lessonsListTmp) throws IOException, Exception {
		Lesson lessonSessionAttribute = (Lesson)session.getAttribute("lessonSessionAttribute");
		if (lessonSessionAttribute != null){
			lesson.setSkillLevelTypeId(lessonSessionAttribute.getSkillLevelTypeId()); // assign the skill id
			if (lesson.getId() == null){ // adding a new lesson
		       lessonsProcessingService.saveLesson(lesson);
			} else { // editing a current lesson
		      lessonsProcessingService.upsertLesson(lesson);
		    }

		    // retrieve lessons
		    ArrayList<Lesson> lessonsList = (ArrayList<Lesson>) lessonsProcessingService.retrieveLessonsList(lessonSessionAttribute.getSkillLevelTypeId());
		    m.addAttribute("lessonListTmp", lessonsList);

		    // insert list into wrapper
		    LessonListWrapper lessonListWrapperTmp = new LessonListWrapper();
		    lessonListWrapperTmp.setLessonList(lessonsList);
		    m.addAttribute("lessonListWrapper", lessonListWrapperTmp);
		    
		    model.addObject("lesson", lesson);
		    User userSessionAttribute = (User)session.getAttribute("userSessionAttribute");
		    if (userSessionAttribute != null){ // admin is logged in
	  		    model.setViewName("admin/noviceAdminEntry");		    	
		    } else {
  		       model.setViewName("novice");
		    }
		    //model.setViewName("novice?userName=admin&skillLevelTypeId=1");

		} else {
			throw new Exception ("Internal Error");
		}
		return model;
	}
	
	@RequestMapping(value = "/saveUpdateLesson")
	public ModelAndView saveUpdateLesson (HttpSession session, ModelAndView model, Model m, @ModelAttribute Lesson lesson, @ModelAttribute LessonListWrapper lessonListWrapper, @ModelAttribute ArrayList<Lesson> lessonsListTmp) throws IOException, Exception {
		Lesson lessonSessionAttribute = (Lesson)session.getAttribute("lessonSessionAttribute");
		if (lessonSessionAttribute != null){
			lesson.setSkillLevelTypeId(lessonSessionAttribute.getSkillLevelTypeId()); // assign the skill id
			if (lesson.getId() != null){ // editing a current lesson
		      lessonsProcessingService.upsertLesson(lesson);
		    }

		    // retrieve lessons
		    ArrayList<Lesson> lessonsList = (ArrayList<Lesson>) lessonsProcessingService.retrieveLessonsList(lessonSessionAttribute.getSkillLevelTypeId());
		    m.addAttribute("lessonListTmp", lessonsList);

		    // insert list into wrapper
		    LessonListWrapper lessonListWrapperTmp = new LessonListWrapper();
		    lessonListWrapperTmp.setLessonList(lessonsList);
		    m.addAttribute("lessonListWrapper", lessonListWrapperTmp);
		    
		    model.addObject("lesson", lesson);
		    User userSessionAttribute = (User)session.getAttribute("userSessionAttribute");
		    if (userSessionAttribute != null){ // admin is logged in
	  		    model.setViewName("admin/noviceAdminEntry");		    	
		    } else {
  		       model.setViewName("novice");
		    }
		    //model.setViewName("novice?userName=admin&skillLevelTypeId=1");

		} else {
			throw new Exception ("Internal Error");
		}
		return model;
	}

	@RequestMapping(value = "/editLesson")
	public ModelAndView editLesson (ModelAndView model, @RequestParam("lessonId") String lessonId) throws IOException {
		System.out.println ("Editing LessonId: " + lessonId + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");		
		
		Lesson lesson = lessonsProcessingService.findLessonByLessonId(lessonId);
		return lessonEntryUpdate(model, lesson);
	}
}
