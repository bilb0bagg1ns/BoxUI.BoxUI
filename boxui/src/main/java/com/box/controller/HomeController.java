package com.box.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.box.model.domain.Grade;
import com.box.model.domain.GradeListWrapper;
import com.box.model.domain.Lesson;
import com.box.model.domain.LessonListWrapper;
import com.box.model.domain.TestBody;
import com.box.model.domain.User;
import com.box.model.services.AuthenticationService;
import com.box.model.services.GradeProcessingService;
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
	private GradeProcessingService gradeProcessingService;

	
	@Inject
	private CoachingEngineController coachingEngineController;
	
	
    @ModelAttribute("allSkillLevelType")
    public SkillLevelType[] populateTypes() {
        return new SkillLevelType[] { SkillLevelType.NOVICE, SkillLevelType.INTERMEDIATE, SkillLevelType.EXPERT };
    }

	
//    @RequestMapping(value = "/", method = RequestMethod.GET)
//    public String index(Principal principal) {
//        return principal != null ? "home/homeSignedIn" : "home/homeNotSignedIn";
//        //return principal != null ? "tmp/homeSignedIn" : "tmp/homeNotSignedIn";
//    }

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public ModelAndView index(HttpSession session, Principal principal, ModelAndView model) {
      if (principal == null) {
    	  model.setViewName("home/home");
      } else {
    	  model =  listSkillLevels (session, principal, model);
      }      
	  return model;
  }
    
    
	@RequestMapping("/greeting")
    public String greeting(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }
    
//	@RequestMapping(value = "/login")
//	public ModelAndView login(ModelAndView model) throws IOException {
//		User newUser = new User();
//		model.addObject("user", newUser);		
//		model.setViewName("login");
//
//		return model;
//	}

	@RequestMapping(value = "/register")
	public ModelAndView register (ModelAndView model, @ModelAttribute User user) throws IOException {
		model.setViewName("home/register");
		return model;
	}
	
	@RequestMapping(value = "/registration")
	public ModelAndView registeration (ModelAndView model, @ModelAttribute User user) throws IOException {
		registrationService.register(user);
		model.setViewName("home/login");
		return model;
	}
	
//	@RequestMapping(value = "/levels")
//	public ModelAndView authenticate(HttpSession session, ModelAndView model, @ModelAttribute User user) throws IOException {	
//		// testing REST based call
//		ModelMap mm = new ModelMap();
//		coachingEngineController.getTest(mm);
//		System.out.println (mm.get("test") + "<<<<<<------------");
//		
//		// authenticate user
//        boolean isAuthenticated = authenticationService.isAuthenticated(user);
//		if (isAuthenticated){
//			if (user.getUserName().equals("admin")){
//				// add to session so it's available for subsequent screens
//				// adding to session in lieu of trying to pass it via the hfref link
//				// TODO: Ensure on logoff, to invalidate session!
//				session.setAttribute("userSessionAttribute",user);
//				
//			}
//			//List<Contact> listContact = contactDAO.list();
//			//model.addObject("listContact", listContact);
//			model.addObject("user", user);
//			model.setViewName("listLevels");
//		}		
//		return model;
//	}
	
	@RequestMapping(value = "/listSkillLevels")
	public ModelAndView listSkillLevels(HttpSession session, Principal principal, ModelAndView model) {
		System.out.println ("\nHomeController:listSkillLevels - Principal  : "+  principal + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");		
		
		// principal holds the user name entered in the login screen. 
		// At this point user has been successfully authenticated by Spring Security. 
		User user = authenticationService.findByUserName(principal.getName());
		
		// add user to session so it's available for subsequent screens
		// adding to session in lieu of trying to pass it via the hfref link
		session.setAttribute("userSessionAttribute",user);
				
		model.addObject("user", user);
		model.setViewName("skilllevels/skillLevels");
		return model;
	}
	
	@RequestMapping(value = "/novice")
	public ModelAndView novice (HttpSession session, Model m, ModelAndView model, @RequestParam("userName") String userName, @RequestParam("skillLevelTypeId") String skillLevelTypeId) throws IOException {
		System.out.println ("\nHomeController:novice:: "+  userName + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");		

			Lesson lesson = new Lesson ();
			lesson.setSkillLevelTypeId(skillLevelTypeId);
			model.addObject("lesson", lesson);
			
			
			// add to session so it's available for subsequent screens
			// adding to session in lieu of trying to pass it via the hfref link
			// TODO: Ensure on logoff, to invalidate session!
			//session.setAttribute("lessonSessionAttribute",lesson);
			//System.out.println (lesson + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");		
			
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
			m.addAttribute("skillLevelTypeId", skillLevelTypeId); // needed in view
		} else {
			model.setViewName("skilllevels/novice");
		}
		return model;
	}
	
	@RequestMapping(value = "/lessonEntry")
	public ModelAndView lessonEntry (ModelAndView model, @ModelAttribute Lesson lesson,  @RequestParam("skillLevelTypeId") String skillLevelTypeId) throws IOException {
		System.out.println ("skillLevelTypeId: " + skillLevelTypeId + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");		
		System.out.println ("HomeController:lessonEntry: " + lesson + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");		
		
		lesson.setSkillLevelTypeId(skillLevelTypeId);
		model.addObject("lesson", lesson);
		model.setViewName("admin/lesson/lessonEntryForm");
		return model;
	}
	
	@RequestMapping(value = "/lessonEntryUpdate")
	public ModelAndView lessonEntryUpdate (ModelAndView model, @ModelAttribute Lesson lesson) throws IOException {
		model.addObject("lesson", lesson);
		model.setViewName("admin/lesson/lessonEntryUpdateForm");
		return model;
	}
	
	/**
	 * Save lesson created by Admin
	 * 
	 * Called by lessonEntryForm.html
	 * 
	 * @param session
	 * @param model
	 * @param m
	 * @param lesson
	 * @param lessonListWrapper
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveLesson")
	public ModelAndView saveLesson (HttpSession session, ModelAndView model, Model m, @ModelAttribute Lesson lesson, @ModelAttribute LessonListWrapper lessonListWrapper, @ModelAttribute String skillLevelTypeId) throws IOException {
		System.out.println ("\nHomeController:saveLesson: " + lesson + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");		
		
		//Lesson lessonSessionAttribute = (Lesson)session.getAttribute("lessonSessionAttribute");
		//if (lessonSessionAttribute != null){
			//lesson.setSkillLevelTypeId(lessonSessionAttribute.getSkillLevelTypeId()); // assign the skill id
			if (lesson.getId() == null){ // adding a new lesson
		       lessonsProcessingService.saveLesson(lesson);
			} else { // editing a current lesson
		      lessonsProcessingService.upsertLesson(lesson);
		    }
			return routeToLessonList(session, model, m, lesson.getSkillLevelTypeId());
			
//		    // retrieve lessons
////		    ArrayList<Lesson> lessonsList = (ArrayList<Lesson>) lessonsProcessingService.retrieveLessonsList(lessonSessionAttribute.getSkillLevelTypeId());
//		    ArrayList<Lesson> lessonsList = (ArrayList<Lesson>) lessonsProcessingService.retrieveLessonsList(lesson.getSkillLevelTypeId());
//		    m.addAttribute("lessonListTmp", lessonsList);
//
//		    // insert list into wrapper
//		    LessonListWrapper lessonListWrapperTmp = new LessonListWrapper();
//		    lessonListWrapperTmp.setLessonList(lessonsList);
//		    m.addAttribute("lessonListWrapper", lessonListWrapperTmp);
//		    
//		   // model.addObject("lesson", lesson);
//		    m.addAttribute("skillLevelTypeId", lesson.getSkillLevelTypeId());
//		    User userSessionAttribute = (User)session.getAttribute("userSessionAttribute");
//		    if (userSessionAttribute != null){ // admin is logged in
//	  		    model.setViewName("admin/noviceAdminEntry");		    	
//		    } else {
//  		       model.setViewName("skillevels/novice");
//		    }
//		    //model.setViewName("novice?userName=admin&skillLevelTypeId=1");
//
//		//} else {
//		//	throw new Exception ("Internal Error");
//		//}
//		return model;
	}
	
	/**
	 * http://stackoverflow.com/questions/31401669/thymeleaf-multiple-submit-button-in-one-form
	 * http://stackoverflow.com/questions/804581/spring-mvc-controller-redirect-to-previous-page
	 * 
	 * @return
	 */
	@RequestMapping(value="/saveLesson", method=RequestMethod.POST, params="action=cancel")
	public ModelAndView cancelLesson(HttpServletRequest request, HttpSession session, ModelAndView model, Model m, @ModelAttribute Lesson lesson) {
		System.out.println ("\nHomeController:cancelLesson: Lesson : "+ lesson +"<<<<<<<<<<<<<<<<<<<>>>>>>>>>");		
		
//	    String referer = request.getHeader("Referer");
//	    return "redirect:"+ referer;
	    
	    return routeToLessonList(session, model, m, lesson.getSkillLevelTypeId());
	}
	
	/**
	 * Lesson updated by Admin being updated in db. 
	 * 
	 * Called by lessonEntryUpdateForm.html
	 * 
	 * @param session
	 * @param model
	 * @param m
	 * @param lesson
	 * @param lessonListWrapper
	 * @param lessonsListTmp
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveUpdateLesson")
	public ModelAndView saveUpdateLesson (HttpSession session, ModelAndView model, Model m, @ModelAttribute Lesson lesson, @ModelAttribute LessonListWrapper lessonListWrapper,  @ModelAttribute String skillLevelTypeId) throws IOException {
		System.out.println ("HomeController:saveUpdateLesson: " + lesson + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");		
		
		//Lesson lessonSessionAttribute = (Lesson)session.getAttribute("lessonSessionAttribute");
		//if (lessonSessionAttribute != null){
			//lesson.setSkillLevelTypeId(lessonSessionAttribute.getSkillLevelTypeId()); // assign the skill id
			if (lesson.getId() != null){ // editing a current lesson
		      lessonsProcessingService.upsertLesson(lesson);
		    }

			return routeToLessonList(session, model, m, lesson.getSkillLevelTypeId());
//		    // retrieve lessons
////		    ArrayList<Lesson> lessonsList = (ArrayList<Lesson>) lessonsProcessingService.retrieveLessonsList(lessonSessionAttribute.getSkillLevelTypeId());
//		    ArrayList<Lesson> lessonsList = (ArrayList<Lesson>) lessonsProcessingService.retrieveLessonsList(lesson.getSkillLevelTypeId());
//		    m.addAttribute("lessonListTmp", lessonsList);
//
//		    // insert list into wrapper
//		    LessonListWrapper lessonListWrapperTmp = new LessonListWrapper();
//		    lessonListWrapperTmp.setLessonList(lessonsList);
//		    m.addAttribute("lessonListWrapper", lessonListWrapperTmp);
//		    
//		   // model.addObject("lesson", lesson);
//		    m.addAttribute("skillLevelTypeId", lesson.getSkillLevelTypeId());
//		    
//		    User userSessionAttribute = (User)session.getAttribute("userSessionAttribute");
//		    if (userSessionAttribute != null){ // admin is logged in
//	  		    model.setViewName("admin/noviceAdminEntry");		    	
//		    } else {
//  		       model.setViewName("skilllevels/novice");
//		    }
////		    model.setViewName("novice?userName=admin&skillLevelTypeId=1");
//
////		} else {
////			throw new Exception ("Internal Error");
////		}
//		return model;
	}

	private ModelAndView routeToLessonList(HttpSession session, ModelAndView modelAndView, Model model, String skillLevelTypeId){
		System.out.println ("HomeController:routeToLessonList: " + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");		
		
	    ArrayList<Lesson> lessonsList = (ArrayList<Lesson>) lessonsProcessingService.retrieveLessonsList(skillLevelTypeId);
	    //model.addAttribute("lessonListTmp", lessonsList);

	    // insert list into wrapper
	    LessonListWrapper lessonListWrapperTmp = new LessonListWrapper();
	    lessonListWrapperTmp.setLessonList(lessonsList);
	    model.addAttribute("lessonListWrapper", lessonListWrapperTmp);

	    model.addAttribute("skillLevelTypeId", skillLevelTypeId);

	    User userSessionAttribute = (User)session.getAttribute("userSessionAttribute");
	    if (userSessionAttribute != null){ // admin is logged in
  		    modelAndView.setViewName("admin/noviceAdminEntry");		    	
	    } else {
		       modelAndView.setViewName("skilllevels/novice");
	    }
		return modelAndView;
	}
	
	/**
	 * Admin editing a lesson
	 * 
	 * @param model
	 * @param lessonId
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/editLesson")
	public ModelAndView editLesson (ModelAndView model, @RequestParam("lessonId") String lessonId) throws IOException {
		System.out.println ("Editing LessonId: " + lessonId + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");		
		
		Lesson lesson = lessonsProcessingService.findLessonByLessonId(lessonId);
		return lessonEntryUpdate(model, lesson);
	}
	
	/**
	 * Admin deleting a lesson
	 * 
	 * @param model
	 * @param lessonId
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/deleteLesson")
	public ModelAndView deleteLesson (HttpSession session, ModelAndView modelAndView, Model model, @RequestParam("lessonId") String lessonId, @RequestParam("skillLevelTypeId") String skillLevelTypeId) throws IOException {
		System.out.println ("Deleting LessonId: " + lessonId + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");		
		
		lessonsProcessingService.deleteLesson(lessonId);
		return routeToLessonList(session, modelAndView, model, skillLevelTypeId);
	}
	
	/**
	 * Submit challenge to coaching engine
	 * 
	 * @param m
	 * @param model
	 * @param lessonListWrapper
	 * @param lessonId
	 * @param skillLevelTypeId
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/submitChallenge")
	public ModelAndView submitChallenge (HttpServletRequest request, Model m, ModelAndView model, @ModelAttribute LessonListWrapper lessonListWrapper, @RequestParam("lessonId") String lessonId, @RequestParam("skillLevelTypeId") String skillLevelTypeId) throws IOException {
		System.out.println ("Submitting challenge for LessonId: " + lessonId + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");		
		System.out.println ("Submitting challenge for skillLevelTypeId: " + skillLevelTypeId + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");		
		User user = (User)request.getSession().getAttribute("userSessionAttribute");
		System.out.println ("Submitting challenge for User: " + user + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");		

		// testing REST based call
		ModelMap mm = new ModelMap();
		coachingEngineController.getTest(mm);
		TestBody testBody = (TestBody)mm.get("test");
		
		// save the grade
		Grade grade = new Grade();
		if(testBody.getResponseMessage().contentEquals("1")){
			// challenge successfully completed
			grade.setGrade("Pass");		
		} else {
			// challenge needs to be repeated
			grade.setGrade("Repeat");				
		}		
		grade.setUserId(user.getId());
		grade.setLessonId(lessonId);
	    gradeProcessingService.save(grade);


		System.out.println ("Response from CoachingEngine: " + testBody +"<<<<<<------------");
		
		// go back to the page where we came from
		if ((SkillLevelType.valueOfId(skillLevelTypeId)).equals(SkillLevelType.NOVICE)){
		    // retrieve lessons
		    ArrayList<Lesson> lessonsList = (ArrayList<Lesson>) lessonsProcessingService.retrieveLessonsList(skillLevelTypeId);
		    m.addAttribute("lessonListTmp", lessonsList);

		    // insert list into wrapper
		    LessonListWrapper lessonListWrapperTmp = new LessonListWrapper();
		    lessonListWrapperTmp.setLessonList(lessonsList);
		    m.addAttribute("lessonListWrapper", lessonListWrapperTmp);
			
		  model.setViewName("skilllevels/novice");	
		}
		
		return model;
	}
	
	@RequestMapping(value = "/retrieveGradebook")
	public ModelAndView retrieveGradebook (HttpServletRequest request, Model m, ModelAndView model, @RequestParam("userId") String userId) throws IOException {
		System.out.println ("Submitting challenge for User: " + userId + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");		
		User user = (User)request.getSession().getAttribute("userSessionAttribute");
		    
    
		ArrayList<Grade> gradeList = null;
		// if logged user is admin, send them to the 	    
	   if (user.getUserName().equals("admin")){
	     model.setViewName("grade/gradebookForAllUsers");
	   } else {   
			model.setViewName("grade/gradebookForUser");
			// retrieve the grade for the user
		    gradeList = (ArrayList<Grade>)gradeProcessingService.findLessonsByUserId(userId);
		    
		    // insert list into wrapper
		    GradeListWrapper gradeListWrapperTmp = new GradeListWrapper();
		    gradeListWrapperTmp.setGradeList(gradeList);
		    m.addAttribute("gradeListWrapper", gradeListWrapperTmp);	    
	   }
       System.out.println (gradeList);

	   return model;
	}
}
