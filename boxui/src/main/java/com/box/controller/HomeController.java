package com.box.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.box.model.domain.Grade;
import com.box.model.domain.GradeComposite;
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

/**
 * 
 * Home Controller
 *
 */
@Controller
public class HomeController {

	private final Logger log = LoggerFactory.getLogger(HomeController.class);

	// @Autowired
	// private UserRepository repository;
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

	@Inject
	private ContentController contentController;
	
	@Inject
	private AdminController adminController;

	@ModelAttribute("allSkillLevelType")
	public SkillLevelType[] populateTypes() {
		return new SkillLevelType[] { SkillLevelType.NOVICE, SkillLevelType.INTERMEDIATE, SkillLevelType.EXPERT };
	}

	// @RequestMapping(value = "/", method = RequestMethod.GET)
	// public String index(Principal principal) {
	// return principal != null ? "home/homeSignedIn" : "home/homeNotSignedIn";
	// //return principal != null ? "tmp/homeSignedIn" : "tmp/homeNotSignedIn";
	// }

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView index(HttpSession session, Principal principal, ModelAndView modelAndView, Model model) {
		if (principal == null) {
			modelAndView.setViewName("home/home");
		} else {
			log.debug(principal.getName(), "logged in");
			// model = listSkillLevels(session, principal, model);
			modelAndView = renderLandingPage(session, principal, modelAndView, model);
		}
		return modelAndView;
	}



	@RequestMapping("/greeting")
	public String greeting(@RequestParam(value = "name", required = false, defaultValue = "World") String name,
			Model model) {
		model.addAttribute("name", name);
		return "greeting";
	}

	// @RequestMapping(value = "/login")
	// public ModelAndView login(ModelAndView model) throws IOException {
	// User newUser = new User();
	// model.addObject("user", newUser);
	// model.setViewName("login");
	//
	// return model;
	// }

	@RequestMapping(value = "/register")
	public ModelAndView register(ModelAndView model, @ModelAttribute User user) throws IOException {
		model.setViewName("home/register");
		return model;
	}

	@RequestMapping(value = "/registration")
	// public ModelAndView registeration(ModelAndView model, @ModelAttribute
	// User user) throws IOException {
	public ModelAndView registeration(ModelAndView model, @Valid User user, BindingResult bindingResult)
			throws IOException {
		if (bindingResult.hasErrors()) {
			// return to view in incoming model
			model.setViewName("home/register");
		} else {
			registrationService.register(user);
			model.setViewName("home/login");
		}
		return model;
	}

	// @RequestMapping(value = "/levels")
	// public ModelAndView authenticate(HttpSession session, ModelAndView model,
	// @ModelAttribute User user) throws IOException {
	// // testing REST based call
	// ModelMap mm = new ModelMap();
	// coachingEngineController.getTest(mm);
	// log.debug (mm.get("test") + "<<<<<<------------");
	//
	// // authenticate user
	// boolean isAuthenticated = authenticationService.isAuthenticated(user);
	// if (isAuthenticated){
	// if (user.getUserName().equals("admin")){
	// // add to session so it's available for subsequent screens
	// // adding to session in lieu of trying to pass it via the hfref link
	// // TODO: Ensure on logoff, to invalidate session!
	// session.setAttribute("userSessionAttribute",user);
	//
	// }
	// //List<Contact> listContact = contactDAO.list();
	// //model.addObject("listContact", listContact);
	// model.addObject("user", user);
	// model.setViewName("listLevels");
	// }
	// return model;
	// }

	/*-
	 * Renders the correct landing page depending on logged user: Admin -
	 * renders 
	 *   Admin Login Page 
	 *   User - renders Operating System Selection Page
	 * 
	 * @param session
	 * @param principal
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/renderLandingPage")
	public ModelAndView renderLandingPage(HttpSession session, Principal principal, ModelAndView modelAndView, Model model) {
		log.debug("\nHomeController:landingPage - Principal  : " + principal + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");

		// principal holds the user name entered in the login screen.
		// At this point user has been successfully authenticated by Spring
		// Security.
		User user = authenticationService.findByUserName(principal.getName());

		// add user to session so it's available for subsequent screens
		// adding to session in lieu of trying to pass it via the hfref link
		session.setAttribute("userSessionAttribute", user);

		modelAndView.addObject("user", user);

		// route based on admin vs. user
		// TODO: Need to replace hardcoded check with roles
		if (user.getUserName().equals("admin")) {
			modelAndView.setViewName("admin/adminLandingPage");
		} else {
			adminController.buildModelWithOperatingSystemListWrapper(model);
			modelAndView.setViewName("selection/operatingSystem");
		}
		return modelAndView;
	}

	/**
	 * This might have to be deprecated in lieu of landingPage method above
	 * 
	 * @param session
	 * @param principal
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/listSkillLevels")
	public ModelAndView listSkillLevels(HttpSession session, Principal principal, ModelAndView model) {
		log.debug("\nHomeController:listSkillLevels - Principal  : " + principal + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");

		log.debug("\nHomeController:listSkillLevels - Principal  : " + principal + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");

		// principal holds the user name entered in the login screen.
		// At this point user has been successfully authenticated by Spring
		// Security.
		User user = authenticationService.findByUserName(principal.getName());

		// add user to session so it's available for subsequent screens
		// adding to session in lieu of trying to pass it via the hfref link
		session.setAttribute("userSessionAttribute", user);

		model.addObject("user", user);

		// route based on admin vs. user
		// TODO: Need to replace hardcoded check with roles
		if (user.getUserName().equals("admin")) {
			model.setViewName("skilllevels/skillLevels");
		} else {
			model.setViewName("selection/operatingSystem");
		}
		return model;
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
	public ModelAndView submitChallenge(HttpServletRequest request, Model m, ModelAndView model,
			@ModelAttribute LessonListWrapper lessonListWrapper, @RequestParam("lessonId") String lessonId,
			@RequestParam("skillLevelTypeId") String skillLevelTypeId) throws IOException {
		log.debug("Submitting challenge for LessonId: " + lessonId + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");
		log.debug("Submitting challenge for skillLevelTypeId: " + skillLevelTypeId + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");
		User user = (User) request.getSession().getAttribute("userSessionAttribute");
		log.debug("Submitting challenge for User: " + user + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");

		// testing REST based call
		ModelMap mm = new ModelMap();
		coachingEngineController.getTest(mm);
		TestBody testBody = (TestBody) mm.get("test");

		// save the grade
		Grade grade = new Grade();
		if (testBody.getResponseMessage().contentEquals("1")) {
			// challenge successfully completed
			grade.setGrade("Pass");
		} else {
			// challenge needs to be repeated
			grade.setGrade("Repeat");
		}
		grade.setUserId(user.getId());
		grade.setLessonId(lessonId);
		gradeProcessingService.save(grade);

		log.debug("Response from CoachingEngine: " + testBody + "<<<<<<------------");

		// go back to the page where we came from
		if ((SkillLevelType.valueOfId(skillLevelTypeId)).equals(SkillLevelType.NOVICE)) {
			// retrieve lessons
			ArrayList<Lesson> lessonsList = (ArrayList<Lesson>) lessonsProcessingService
					.retrieveLessonsList(skillLevelTypeId);
			m.addAttribute("lessonListTmp", lessonsList);

			// insert list into wrapper
			LessonListWrapper lessonListWrapperTmp = new LessonListWrapper();
			lessonListWrapperTmp.setLessonList(lessonsList);
			m.addAttribute("lessonListWrapper", lessonListWrapperTmp);

			model.setViewName("skilllevels/noviceProve");
		}

		return model;
	}

	@RequestMapping(value = "/retrieveGradebook")
	public ModelAndView retrieveGradebook(HttpServletRequest request, Model m, ModelAndView model,
			@RequestParam("userId") String userId) throws IOException {
		log.debug("Retrieving grade book for User: " + userId + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");
		User user = (User) request.getSession().getAttribute("userSessionAttribute");

		// if logged user is admin, send to view to gradebook to all users
		if (user.getUserName().equals("admin")) {
			model.setViewName("grade/gradebookForAllUsers");

			ArrayList<GradeComposite> gradeCompositeList = (ArrayList<GradeComposite>) gradeProcessingService
					.findAllDetails(userId);
			// insert list into wrapper
			GradeListWrapper gradeListWrapperTmp = new GradeListWrapper();
			gradeListWrapperTmp.setGradeCompositeList(gradeCompositeList);
			m.addAttribute("gradeListWrapper", gradeListWrapperTmp);

		} else {
			model.setViewName("grade/gradebookForUser");
			// retrieve the grade for the user
			ArrayList<GradeComposite> gradeCompositeList = (ArrayList<GradeComposite>) gradeProcessingService
					.findGradeByUserId(userId);

			// insert list into wrapper
			GradeListWrapper gradeListWrapperTmp = new GradeListWrapper();
			gradeListWrapperTmp.setGradeCompositeList(gradeCompositeList);
			m.addAttribute("gradeListWrapper", gradeListWrapperTmp);
		}

		return model;
	}
}
