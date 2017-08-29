package com.box.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
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
import com.box.model.domain.LessonRows;
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
	public ModelAndView index(HttpSession session, Principal principal, ModelAndView model) {
		if (principal == null) {
			model.setViewName("home/home");
		} else {
			log.debug(principal.getName(), "logged in");
			// model = listSkillLevels(session, principal, model);
			model = renderLandingPage(session, principal, model);
		}
		return model;
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
	public ModelAndView renderLandingPage(HttpSession session, Principal principal, ModelAndView model) {
		log.debug("\nHomeController:landingPage - Principal  : " + principal + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");

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
			model.setViewName("admin/adminLandingPage");
		} else {
			model.setViewName("selection/operatingSystem");
		}
		return model;
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
	 * Called in User flow a. after "Learn" is chosen OR b. after "Prove" is
	 * chosen
	 * 
	 * 7/26/17 - userName is no longer needed, as this flow is only used by the
	 * User -> ContentController.focusAreaProve -> skillLevels.html
	 * 
	 * @param session
	 * @param m
	 * @param model
	 * @param userName
	 * @param skillLevelTypeId
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/novice")
	public ModelAndView novice(HttpSession session, Model m, ModelAndView model, HttpServletRequest request,
			@RequestParam("userName") String userName, @RequestParam("skillLevelTypeId") String skillLevelTypeId,
			@RequestParam("learnOrProve") String learnOrProve) throws IOException {
		log.debug("\nHomeController:novice:: " + userName + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");

		Lesson lesson = new Lesson();
		lesson.setSkillLevelTypeId(skillLevelTypeId);
		model.addObject("lesson", lesson);

		// add to session so it's available for subsequent screens
		// adding to session in lieu of trying to pass it via the hfref link
		// TODO: Ensure on logoff, to invalidate session!
		// session.setAttribute("lessonSessionAttribute",lesson);
		// log.debug (lesson + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");

		// if user is admin, send them to the flow to add/edit lessons
		// if (userName.equals("admin")) {
		// model.setViewName("admin/noviceAdminEntry");
		// m.addAttribute("skillLevelTypeId", skillLevelTypeId); // needed in
		// // view
		// } else {

		// retrieve lessons
		ArrayList<Lesson> lessonsList = (ArrayList<Lesson>) lessonsProcessingService
				.retrieveLessonsList(skillLevelTypeId);
		m.addAttribute("lessonListTmp", lessonsList);

		if ((!StringUtils.isEmpty(learnOrProve)) && learnOrProve.equals("Prove")) {

			// insert list into wrapper
			LessonListWrapper lessonListWrapperTmp = new LessonListWrapper();
			lessonListWrapperTmp.setLessonList(lessonsList);
			m.addAttribute("lessonListWrapper", lessonListWrapperTmp);

			model.setViewName("skilllevels/noviceProve");
		} else if ((!StringUtils.isEmpty(learnOrProve)) && learnOrProve.equals("Learn")) {

			// retrieve lessons
			// ArrayList<Lesson> lessonsList = (ArrayList<Lesson>)
			// lessonsProcessingService.retrieveAllLessons();
			// m.addAttribute("lessonListTmp", lessonsList);

			// insert the lessons into LessonRows for rendering on UI in rows
			LessonListWrapper lessonListWrapperTmp = new LessonListWrapper();
			ArrayList<LessonRows> lessonRowsList = new ArrayList<LessonRows>();
			// create a lessons rows list with each element holding 4 lessons
			lessonRowsList = ContentController.createLessonRowsList(lessonsList, lessonRowsList);
			// insert list into wrapper
			lessonListWrapperTmp.setLessonRowsList(lessonRowsList);
			m.addAttribute("lessonListWrapper", lessonListWrapperTmp);

			model.setViewName("selection/learningTopics");
		}
		// }
		return model;
	}

	@RequestMapping(value = "/novice", method = RequestMethod.POST, params = "action=cancel")
	public ModelAndView cancelNovice(HttpServletRequest request, HttpSession session, ModelAndView modelAndView,
			Model model, @ModelAttribute Lesson lesson) throws IOException {
		log.debug("\nHomeController:cancelNovice <<<<<<<<<<<<<<<<<<<>>>>>>>>>");

		modelAndView.setViewName("skilllevels/skillLevels");
		User user = (User) session.getAttribute("userSessionAttribute");
		modelAndView.addObject("user", user);

		return modelAndView;

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
