package com.box.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.box.model.domain.Lesson;
import com.box.model.domain.LessonListWrapper;
import com.box.model.domain.OperatingSystem;
import com.box.model.domain.OperatingSystemListWrapper;
import com.box.model.domain.Skill;
import com.box.model.domain.SkillListWrapper;
import com.box.model.domain.User;
import com.box.model.domain.UserListWrapper;
import com.box.model.services.LessonsProcessingService;
import com.box.model.services.OperatingSystemProcessingService;
import com.box.model.services.SkillProcessingService;
import com.box.model.services.UsersProcessingService;

@Controller
public class AdminController {

	private final Logger log = LoggerFactory.getLogger(AdminController.class);
	
	@Inject
	private UsersProcessingService usersProcessingService;
	
	@Inject
	private LessonsProcessingService lessonsProcessingService;
	
	@Inject
	private OperatingSystemProcessingService operatingSystemProcessingService;
	
	@Inject
	private SkillProcessingService skillProcessingService;
	
	/**
	 * Renders the Add Lessons table for Admin user
	 * 
	 * @param session
	 * @param model
	 * @param modelAndView
	 * @param userName
	 * @param skillLevelTypeId
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/adminEntry")
	public ModelAndView adminEntry(HttpSession session, Model model, ModelAndView modelAndView) throws IOException {
		// public ModelAndView adminEntry(HttpSession session, Model m,
		// ModelAndView model) throws IOException {
		log.debug("\nAdminController:adminEntry:: " + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");

		loadLessonsIntoModel(model);

		modelAndView.setViewName("admin/adminEntry");
		// m.addAttribute("skillLevelTypeId", skillLevelTypeId); // needed in
		// view

		return modelAndView;
	}
	
	/**
	 * Checkbox logic comes from :
	 * http://stackoverflow.com/questions/17692941/values-for-thfield-attributes-in-checkbox
	 * 
	 * @param modelAndView
	 * @param lesson
	 * @param skillLevelTypeId
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/quizEntry")
	public ModelAndView quizEntry(ModelAndView modelAndView, @ModelAttribute Lesson lesson) throws IOException {
		// public ModelAndView lessonEntry(ModelAndView modelAndView,
		// @ModelAttribute Lesson lesson,
		// @RequestParam("skillLevelTypeId") String skillLevelTypeId) throws
		// IOException {
		// log.debug("skillLevelTypeId: " + skillLevelTypeId +
		// "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");
		log.debug("AdminController:quizEntry: " + lesson + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");

		// 4/18/17: might be removing this functionality in lieu of setting it
		// at each lesson entry via checkbox
		// initialize skill level associated with lesson
		// lesson.setSkillLevelTypeId(skillLevelTypeId);

		// initialize checkbox. Used in rendering checkboxes in lesson entry and
		// lesson entry update
		List<String> quizTrueFalseState = new ArrayList<String>();
		quizTrueFalseState.add("true");
		quizTrueFalseState.add("false");
		modelAndView.addObject("quizTrueFalseState", quizTrueFalseState);

		// initialize default value
		List<String> checkedItems = new ArrayList<String>();
		// novice will be checked by default.
		checkedItems.add("novice");
		lesson.setCheckedSkillsLevelsApplicableTo(checkedItems);

		modelAndView.addObject("lesson", lesson);
		modelAndView.setViewName("admin/lesson/quizEntryForm");
		return modelAndView;
	}

	@RequestMapping(value = "/userManagement")
	public ModelAndView userManagement(HttpSession session, ModelAndView modelAndView, Model model) throws IOException {

		log.debug("AdminController:userManagement: " + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");

		User userSessionAttribute = (User) session.getAttribute("userSessionAttribute");
		if (userSessionAttribute != null) { // admin is logged in

//            // retrieve all users
//			ArrayList<User> usersList = null;
//			usersList = (ArrayList<User>) usersProcessingService.retrieveAllUsers();
//
//			// insert list into wrapper
//			UserListWrapper userListWrapperTmp = new UserListWrapper();
//			userListWrapperTmp.setUserList(usersList);
//			model.addAttribute("userListWrapper", userListWrapperTmp);
//			
//			modelAndView.setViewName("admin/user/userEntry");
			modelAndView =  userEntry(modelAndView, model);
		}
		return modelAndView;
	}
	
	@RequestMapping(value = "/contentManagement")
	public ModelAndView contentManagement(HttpSession session, ModelAndView modelAndView, Model model, @ModelAttribute Lesson lesson) throws IOException {

		log.debug("AdminController:contentManagement: " + lesson + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");

		modelAndView.setViewName("admin/contentManagement");
		return modelAndView;

	}
	
	@RequestMapping(value = "/operatingSystemEntry")
	public ModelAndView operatingSystemEntry(HttpSession session, ModelAndView modelAndView, Model model, @ModelAttribute OperatingSystem operatingSystem) throws IOException {

		log.debug("AdminController:operatingSystemEntry: " + operatingSystem + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");

		// get model with operating system info in it
		buildModelWithOperatingSystemListWrapper(model);

		modelAndView.setViewName("admin/operatingSystem/operatingSystemEntry");
		return modelAndView;
	}
	

	/**
	 * Retrieve all operating systems details from repository into a wrapper and add it to the Model.
	 * @param model
	 */
	protected void buildModelWithOperatingSystemListWrapper(Model model) {
		ArrayList<OperatingSystem> operatingSystemList  = (ArrayList<OperatingSystem>) operatingSystemProcessingService.retrieveAllOperatingSystems();

		// insert list into wrapper
		OperatingSystemListWrapper operatingSystemListWrapperTmp = new OperatingSystemListWrapper();
		operatingSystemListWrapperTmp.setOperatingSystemList(operatingSystemList);
		model.addAttribute("operatingSystemListWrapper", operatingSystemListWrapperTmp);
	}
	
	/**
	 * Checkbox logic comes from :
	 * http://stackoverflow.com/questions/17692941/values-for-thfield-attributes-in-checkbox
	 * 
	 * @param modelAndView
	 * @param lesson
	 * @param skillLevelTypeId
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/operatingSystemEntryForm")
	public ModelAndView operatingSystemEntryForm(ModelAndView modelAndView, @ModelAttribute OperatingSystem operatingSystem) throws IOException {
		log.debug("AdminController:operatingSystemEntryForm: " + operatingSystem + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");

		modelAndView.addObject("operatingSystem", operatingSystem);
		modelAndView.setViewName("admin/operatingsystem/operatingSystemEntryForm");
		return modelAndView;
	}
	
	@RequestMapping(value = "/operatingSystemEntryUpdate")
	public ModelAndView operatingSystemEntryUpdate(ModelAndView modelAndView, @ModelAttribute OperatingSystem operatingSystem) throws IOException {

		modelAndView.addObject("operatingSystem", operatingSystem);
		modelAndView.setViewName("admin/operatingSystem/operatingSystemEntryUpdateForm");
		return modelAndView;
	}
	
	/**
	 * Save lesson created by Admin
	 * 
	 * Called by OperatingSystemEntryForm.html
	 * 
	 * @param session
	 * @param modelAndView
	 * @param model
	 * @param lesson
	 * @param lessonListWrapper
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveOperatingSystem")
	public ModelAndView saveOperatingSystem(HttpSession session, ModelAndView modelAndView, Model model, @ModelAttribute OperatingSystem operatingSystem,
			@ModelAttribute LessonListWrapper lessonListWrapper, @ModelAttribute String skillLevelTypeId)
			throws IOException {
		log.debug("\nAdminController:saveOperatingSystem: " + operatingSystem + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");

		// assign the skill id
		if (operatingSystem.getId() == null) { // adding a new operating system
			operatingSystemProcessingService.saveOperatingSystem(operatingSystem);
		} else { // editing a current lesson
			operatingSystemProcessingService.upsertOperatingSystem(operatingSystem);
		}
		return routeToOperatingSystemList(session, modelAndView, model);

	}
	
	
	private ModelAndView routeToOperatingSystemList(HttpSession session, ModelAndView modelAndView, Model model) {
		buildModelWithOperatingSystemListWrapper(model);

		modelAndView.setViewName("admin/operatingSystem/operatingSystemEntry");
		return modelAndView;
	}
	
	/**
	 * Admin editing an Operating System
	 * 
	 * @param model
	 * @param lessonId
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/editOperatingSystem")
	public ModelAndView editOperatingSystem(ModelAndView model, @RequestParam("operatingSystemId") String id) throws IOException {
		log.debug("Editing id: " + id + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");

		OperatingSystem operatingSystem = operatingSystemProcessingService.findOperatingSystemByOperatingSystemId(id);
		return operatingSystemEntryUpdate(model, operatingSystem);
	}

	/**
	 * Called by Admin to delete an operating system
	 * 
	 * @param model
	 * @param lessonId
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/deleteOperatingSystem")
	public ModelAndView deleteOperatingSystem(HttpSession session, ModelAndView modelAndView, Model model,
			@RequestParam("operatingSystemId") String operatingSystemId)
			throws IOException {
		log.debug("Deleting operatingSystemId: " + operatingSystemId + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");

		operatingSystemProcessingService.deleteOperatingSystem(operatingSystemId);
		return routeToOperatingSystemList(session, modelAndView, model);
	}
	
	@RequestMapping(value = "/skillEntry")
	public ModelAndView skillEntry(HttpSession session, ModelAndView modelAndView, Model model, @ModelAttribute Skill skill) throws IOException {

		log.debug("AdminController:skillEntry: " + skill + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");

		ArrayList<Skill> skillList  = (ArrayList<Skill>) skillProcessingService.retrieveAllSkills();

		// insert list into wrapper
		SkillListWrapper skillListWrapperTmp = new SkillListWrapper();
		skillListWrapperTmp.setSkillList(skillList);
		model.addAttribute("skillListWrapper", skillListWrapperTmp);

		modelAndView.setViewName("admin/skill/skillEntry");
		return modelAndView;
	}
	
	@RequestMapping(value = "/skillEntryForm")
	public ModelAndView skillEntryForm(ModelAndView modelAndView, @ModelAttribute Skill skill) throws IOException {
		log.debug("AdminController:skillEntryForm: " + skill + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");

		modelAndView.addObject("skill", skill);
		modelAndView.setViewName("admin/skill/skillEntryForm");
		return modelAndView;
	}
	
	@RequestMapping(value = "/skillEntryUpdate")
	public ModelAndView skillEntryUpdate(ModelAndView modelAndView, @ModelAttribute Skill skill) throws IOException {

		modelAndView.addObject("skill", skill);
		modelAndView.setViewName("admin/skill/skillEntryUpdateForm");
		return modelAndView;
	}	
	
	@RequestMapping(value = "/saveSkill")
	public ModelAndView saveSkill(HttpSession session, ModelAndView modelAndView, Model model, @ModelAttribute Skill skill,
			@ModelAttribute LessonListWrapper lessonListWrapper)
			throws IOException {
		log.debug("\nAdminController:saveSkill: " + skill + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");

		// assign the skill id
		if (skill.getId() == null) { // adding a new operating system
			skillProcessingService.saveSkill(skill);
		} else { // editing a current lesson
			skillProcessingService.upsertSkill(skill);
		}
		return routeToSkillList(session, modelAndView, model);

	}
	
	@RequestMapping(value = "/editSkill")
	public ModelAndView editSkill(ModelAndView model, @RequestParam("skillId") String id) throws IOException {
		log.debug("Editing id: " + id + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");

		Skill skill = skillProcessingService.findSkillBySkillId(id);
		return skillEntryUpdate(model, skill);
	}
	
	@RequestMapping(value = "/deleteSkill")
	public ModelAndView deleteSkill(HttpSession session, ModelAndView modelAndView, Model model,
			@RequestParam("skillId") String skillId)
			throws IOException {
		log.debug("Deleting skillId: " + skillId + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");

		skillProcessingService.deleteSkill(skillId);
		return routeToSkillList(session, modelAndView, model);
	}
	
	private ModelAndView routeToSkillList(HttpSession session, ModelAndView modelAndView, Model model) {
		ArrayList<Skill> skillList  = (ArrayList<Skill>) skillProcessingService.retrieveAllSkills();

		// insert list into wrapper
		SkillListWrapper skillListWrapperTmp = new SkillListWrapper();
		skillListWrapperTmp.setSkillList(skillList);
		model.addAttribute("skillListWrapper", skillListWrapperTmp);

		modelAndView.setViewName("admin/skill/skillEntry");
		return modelAndView;
	}
	
	@RequestMapping(value = "/lessonEntry")
	public ModelAndView lessonEntry(HttpSession session, ModelAndView modelAndView, Model model, @ModelAttribute Lesson lesson) throws IOException {

		log.debug("AdminController:lessonEntry: " + lesson + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");

		return routeToLessonList(session, modelAndView, model, null);

	}
	/**
	 * Checkbox logic comes from :
	 * http://stackoverflow.com/questions/17692941/values-for-thfield-attributes-in-checkbox
	 * 
	 * @param modelAndView
	 * @param lesson
	 * @param skillLevelTypeId
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/lessonEntryForm")
	public ModelAndView lessonEntryForm(ModelAndView modelAndView, @ModelAttribute Lesson lesson) throws IOException {
		// public ModelAndView lessonEntry(ModelAndView modelAndView,
		// @ModelAttribute Lesson lesson,
		// @RequestParam("skillLevelTypeId") String skillLevelTypeId) throws
		// IOException {
		// log.debug("skillLevelTypeId: " + skillLevelTypeId +
		// "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");
		log.debug("AdminController:lessonEntryForm: " + lesson + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");

		// 4/18/17: might be removing this functionality in lieu of setting it
		// at each lesson entry via checkbox
		// initialize skill level associated with lesson
		// lesson.setSkillLevelTypeId(skillLevelTypeId);

		// extract all skills info from repository
		ArrayList<Skill> skillList  = (ArrayList<Skill>) skillProcessingService.retrieveAllSkills();
		// add each skill to list for UI display
		List<String> allSkillLevelApplicableItems = new ArrayList<String>();
		for (Skill skill : skillList ) {
			allSkillLevelApplicableItems.add(skill.getName());			
		}	
		modelAndView.addObject("allSkillLevelApplicableItems", allSkillLevelApplicableItems);
		

		// set all operating systems available
		setToModelAndViewAllOperatingSystemApplicableItems(modelAndView);

		// initialize default value
		List<String> checkedItems = new ArrayList<String>();
		// novice will be checked by default.
		//checkedItems.add("novice");
		checkedItems.add(allSkillLevelApplicableItems.get(0));
		lesson.setCheckedSkillsLevelsApplicableTo(checkedItems);

		modelAndView.addObject("lesson", lesson);
		modelAndView.setViewName("admin/lesson/lessonEntryForm");
		return modelAndView;
	}

	/**
	 * 
	 * @param modelAndView
	 * @param lesson
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/lessonEntryUpdate")
	public ModelAndView lessonEntryUpdate(ModelAndView modelAndView, @ModelAttribute Lesson lesson) throws IOException {
		
		// set all skills available
		setToModelAndViewAllSkillLevelApplicableItems(modelAndView);
		// set all operating systems available
		setToModelAndViewAllOperatingSystemApplicableItems(modelAndView);

		modelAndView.addObject("lesson", lesson);
		modelAndView.setViewName("admin/lesson/lessonEntryUpdateForm");
		return modelAndView;
	}

	private void setToModelAndViewAllSkillLevelApplicableItems(ModelAndView modelAndView) {
		// extract all skills info from repository
		ArrayList<Skill> skillList  = (ArrayList<Skill>) skillProcessingService.retrieveAllSkills();
		// add each skill to list for UI display
		List<String> allSkillLevelApplicableItems = new ArrayList<String>();
		for (Skill skill : skillList ) {
			allSkillLevelApplicableItems.add(skill.getName());			
		}	
		modelAndView.addObject("allSkillLevelApplicableItems", allSkillLevelApplicableItems);
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
	public ModelAndView saveLesson(HttpSession session, ModelAndView model, Model m, @ModelAttribute Lesson lesson,
			@ModelAttribute LessonListWrapper lessonListWrapper, @ModelAttribute String skillLevelTypeId)
			throws IOException {
		log.debug("\nAdminController:saveLesson: " + lesson + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");

		// Lesson lessonSessionAttribute =
		// (Lesson)session.getAttribute("lessonSessionAttribute");
		// if (lessonSessionAttribute != null){
		// lesson.setSkillLevelTypeId(lessonSessionAttribute.getSkillLevelTypeId());
		// // assign the skill id
		if (lesson.getId() == null) { // adding a new lesson
			ArrayList<Integer> skillsApplicableTo = new ArrayList<Integer>();
			skillsApplicableTo.add(1);
			skillsApplicableTo.add(2);
			skillsApplicableTo.add(3);
			lesson.setSkillsLevelsApplicableTo(skillsApplicableTo);

			lessonsProcessingService.saveLesson(lesson);
		} else { // editing a current lesson
			lessonsProcessingService.upsertLesson(lesson);
		}
		return routeToLessonList(session, model, m, lesson.getSkillLevelTypeId());

		// // retrieve lessons
		//// ArrayList<Lesson> lessonsList = (ArrayList<Lesson>)
		// lessonsProcessingService.retrieveLessonsList(lessonSessionAttribute.getSkillLevelTypeId());
		// ArrayList<Lesson> lessonsList = (ArrayList<Lesson>)
		// lessonsProcessingService.retrieveLessonsList(lesson.getSkillLevelTypeId());
		// m.addAttribute("lessonListTmp", lessonsList);
		//
		// // insert list into wrapper
		// LessonListWrapper lessonListWrapperTmp = new LessonListWrapper();
		// lessonListWrapperTmp.setLessonList(lessonsList);
		// m.addAttribute("lessonListWrapper", lessonListWrapperTmp);
		//
		// // model.addObject("lesson", lesson);
		// m.addAttribute("skillLevelTypeId", lesson.getSkillLevelTypeId());
		// User userSessionAttribute =
		// (User)session.getAttribute("userSessionAttribute");
		// if (userSessionAttribute != null){ // admin is logged in
		// model.setViewName("admin/noviceAdminEntry");
		// } else {
		// model.setViewName("skillevels/novice");
		// }
		// //model.setViewName("novice?userName=admin&skillLevelTypeId=1");
		//
		// //} else {
		// // throw new Exception ("Internal Error");
		// //}
		// return model;
	}

	/**
	 * Called by Admin
	 * 
	 * http://stackoverflow.com/questions/31401669/thymeleaf-multiple-submit-button-in-one-form
	 * http://stackoverflow.com/questions/804581/spring-mvc-controller-redirect-to-previous-page
	 * 
	 * @return
	 */
	@RequestMapping(value = "/saveLesson", method = RequestMethod.POST, params = "action=cancel")
	public ModelAndView cancelLesson(HttpServletRequest request, HttpSession session, ModelAndView model, Model m,
			@ModelAttribute Lesson lesson) {
		log.debug("\nAdminController:cancelLesson: Lesson : " + lesson + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");

		// String referer = request.getHeader("Referer");
		// return "redirect:"+ referer;

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
	public ModelAndView saveUpdateLesson(HttpSession session, ModelAndView model, Model m,
			@ModelAttribute Lesson lesson, @ModelAttribute LessonListWrapper lessonListWrapper,
			@ModelAttribute String skillLevelTypeId) throws IOException {
		log.debug("AdminController:saveUpdateLesson: " + lesson + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");

		// Lesson lessonSessionAttribute =
		// (Lesson)session.getAttribute("lessonSessionAttribute");
		// if (lessonSessionAttribute != null){
		// lesson.setSkillLevelTypeId(lessonSessionAttribute.getSkillLevelTypeId());
		// // assign the skill id
		if (lesson.getId() != null) { // editing a current lesson
			lessonsProcessingService.upsertLesson(lesson);
		}

		return routeToLessonList(session, model, m, lesson.getSkillLevelTypeId());
		// // retrieve lessons
		//// ArrayList<Lesson> lessonsList = (ArrayList<Lesson>)
		// lessonsProcessingService.retrieveLessonsList(lessonSessionAttribute.getSkillLevelTypeId());
		// ArrayList<Lesson> lessonsList = (ArrayList<Lesson>)
		// lessonsProcessingService.retrieveLessonsList(lesson.getSkillLevelTypeId());
		// m.addAttribute("lessonListTmp", lessonsList);
		//
		// // insert list into wrapper
		// LessonListWrapper lessonListWrapperTmp = new LessonListWrapper();
		// lessonListWrapperTmp.setLessonList(lessonsList);
		// m.addAttribute("lessonListWrapper", lessonListWrapperTmp);
		//
		// // model.addObject("lesson", lesson);
		// m.addAttribute("skillLevelTypeId", lesson.getSkillLevelTypeId());
		//
		// User userSessionAttribute =
		// (User)session.getAttribute("userSessionAttribute");
		// if (userSessionAttribute != null){ // admin is logged in
		// model.setViewName("admin/noviceAdminEntry");
		// } else {
		// model.setViewName("skilllevels/novice");
		// }
		//// model.setViewName("novice?userName=admin&skillLevelTypeId=1");
		//
		//// } else {
		//// throw new Exception ("Internal Error");
		//// }
		// return model;
	}

	private ModelAndView routeToLessonList(HttpSession session, ModelAndView modelAndView, Model model,
			String skillLevelTypeId) {
		log.debug("AdminController:routeToLessonList: " + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");

		ArrayList<Lesson> lessonsList = null;
		if (StringUtils.isEmpty(skillLevelTypeId)) { // admin flow
			lessonsList = (ArrayList<Lesson>) lessonsProcessingService.retrieveAllLessons();
		} else { // user flow coming in as a novice, intermediate or expert
					// skill
			lessonsList = (ArrayList<Lesson>) lessonsProcessingService.retrieveLessonsList(skillLevelTypeId);
		}

		// model.addAttribute("lessonListTmp", lessonsList);

		// insert list into wrapper
		LessonListWrapper lessonListWrapperTmp = new LessonListWrapper();
		lessonListWrapperTmp.setLessonList(lessonsList);
		model.addAttribute("lessonListWrapper", lessonListWrapperTmp);

		model.addAttribute("skillLevelTypeId", skillLevelTypeId);

		User userSessionAttribute = (User) session.getAttribute("userSessionAttribute");
		if (userSessionAttribute != null) { // admin is logged in
											// modelAndView.setViewName("admin/noviceAdminEntry");
			modelAndView.setViewName("admin/lesson/lessonEntry");
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
	public ModelAndView editLesson(ModelAndView model, @RequestParam("lessonId") String lessonId) throws IOException {
		log.debug("Editing LessonId: " + lessonId + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");

		Lesson lesson = lessonsProcessingService.findLessonByLessonId(lessonId);
		return lessonEntryUpdate(model, lesson);
	}

	/**
	 * Called by Admin to delete a lesson
	 * 
	 * @param model
	 * @param lessonId
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/deleteLesson")
	public ModelAndView deleteLesson(HttpSession session, ModelAndView modelAndView, Model model,
			@RequestParam("lessonId") String lessonId, @RequestParam("skillLevelTypeId") String skillLevelTypeId)
			throws IOException {
		log.debug("Deleting LessonId: " + lessonId + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");

		lessonsProcessingService.deleteLesson(lessonId);
		return routeToLessonList(session, modelAndView, model, skillLevelTypeId);
	}
	
	protected void loadLessonsIntoModel(Model model) throws IOException {
		log.debug("\nAdminController:loadLessonsIntoModel:: " + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");

		// add to session so it's available for subsequent screens
		// adding to session in lieu of trying to pass it via the hfref link
		// TODO: Ensure on logoff, to invalidate session!
		// session.setAttribute("lessonSessionAttribute",lesson);
		// log.debug (lesson + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");

		// retrieve lessons
		ArrayList<Lesson> lessonsList = (ArrayList<Lesson>) lessonsProcessingService.retrieveAllLessons();
		model.addAttribute("lessonListTmp", lessonsList);

		// insert list into wrapper
		LessonListWrapper lessonListWrapperTmp = new LessonListWrapper();
		lessonListWrapperTmp.setLessonList(lessonsList);
		model.addAttribute("lessonListWrapper", lessonListWrapperTmp);

	}
	
	@RequestMapping(value = "/cancelAdminLessonEntry")
	public ModelAndView cancelAdminLessonEntry(ModelAndView modelAndView) throws IOException {
		log.debug("In HomeController.cancelAdminLessonEntry" + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");

		// modelAndView.setViewName("skilllevels/skillLevels");
		modelAndView.setViewName("admin/adminLandingPage");
		return modelAndView;
	}
	
	public ModelAndView userEntry(ModelAndView modelAndView, Model model) throws IOException {

		log.debug("userEntry <<<<<<<<<<<<<<<<<<<>>>>>>>>>");

        // retrieve all users
		ArrayList<User> usersList = null;
		usersList = (ArrayList<User>) usersProcessingService.retrieveAllUsers();

		// insert list into wrapper
		UserListWrapper userListWrapperTmp = new UserListWrapper();
		userListWrapperTmp.setUserList(usersList);
		model.addAttribute("userListWrapper", userListWrapperTmp);
		
		modelAndView.setViewName("admin/user/userEntry");
		
    	return modelAndView;

	}
	
	/**
	 * Checkbox logic comes from :
	 * http://stackoverflow.com/questions/17692941/values-for-thfield-attributes-in-checkbox
	 * 
	 * @param modelAndView
	 * @param lesson
	 * @param skillLevelTypeId
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/userEntryForm")
	public ModelAndView userEntryForm(ModelAndView modelAndView, @ModelAttribute User user) throws IOException {
		log.debug("AdminController:userEntryForm: " + user + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");

		// set all operating systems available
		setToModelAndViewAllOperatingSystemApplicableItems(modelAndView);
        // set all lessons available
		setToModelAndViewAllLessonsApplicableItems(modelAndView);
		
		
		modelAndView.addObject("user", user);
		modelAndView.setViewName("admin/user/userEntryForm");
		return modelAndView;
	}

	@RequestMapping(value = "/cancelAdminUserEntryForm")
	public ModelAndView cancelAdminUserEntryForm(ModelAndView modelAndView, Model model) throws IOException {
		log.debug("In AdminController.cancelAdminUserEntryForm" + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");
		
		return userEntry(modelAndView, model);
	}
	
	/**
	 *  
	 * @param modelAndView
	 * @param model
	 * @param user
	 * @param bindingResult
	 * @return
	 * @throws IOException
	 */
	 // https://stackoverflow.com/questions/30297719/cannot-get-validation-working-with-spring-boot-and-thymeleaf
	@RequestMapping(value = "/saveUser")
	public ModelAndView saveUser(ModelAndView modelAndView, Model model, @ModelAttribute User user, BindingResult bindingResult)
			throws IOException {
		log.debug("\nAdminController:saveUser: " + user + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");

		  if( bindingResult.hasErrors())
		    {
		        log.debug( "There are errors! {}", bindingResult );		      
		    }
		  
		if (user.getId() == null) { // adding a new user
			usersProcessingService.saveUser(user);
			// add the user to the lesson(s) associated with the user
			usersProcessingService.associateUserToLesson(user);
		} else { // editing a current user
			// edited user add or remove lesson(s) associated with the user
			usersProcessingService.addOrRemoveUserToLesson(user);			
			// update the user
			usersProcessingService.upsertUser(user);
		}
		return userEntry(modelAndView, model);
	}

	@RequestMapping(value = "/editUser")
	public ModelAndView editUser(ModelAndView modelAndView, Model model,  @RequestParam("userId") String userId) throws IOException {
		log.debug("Editing LessonId: " + userId + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");

		User user = usersProcessingService.findById(userId);
		return userEntryFormUpdate(modelAndView, user);
	}

	@RequestMapping(value = "/deleteUser")
	public ModelAndView deleteUSer(HttpSession session, ModelAndView modelAndView, Model model,
			@RequestParam("userId") String userId)
			throws IOException {
		log.debug("Deleting UserId: " + userId + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");

		usersProcessingService.deleteUser(userId);
		return userEntry(modelAndView, model);
	}
	/**
	 * Render User entry update form to allow admin to edit user details.
	 * 
	 * @param modelAndView
	 * @param user
	 * @return
	 * @throws IOException
	 */
	public ModelAndView userEntryFormUpdate(ModelAndView modelAndView, @ModelAttribute User user) throws IOException {
		log.debug("AdminController:userEntryForm: " + user + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");

		// set all operating systems available
		setToModelAndViewAllOperatingSystemApplicableItems(modelAndView);
        // set all lessons available
		setToModelAndViewAllLessonsApplicableItems(modelAndView);

		modelAndView.addObject("user", user);
		modelAndView.setViewName("admin/user/userEntryUpdateForm");
		return modelAndView;
	}

	/**
 	 * Retrieve all lessons and add to model
 	 * 
	 * @param modelAndView
	 */
	private void setToModelAndViewAllLessonsApplicableItems(ModelAndView modelAndView) {
		// extract all lessons from repository
		ArrayList<Lesson> lessonsList  = (ArrayList<Lesson>) lessonsProcessingService.retrieveAllLessons();
		// add each lesson name to list for UI display
		List<String> allLessonsApplicableItems = new ArrayList<String>();
		for (Lesson lesson : lessonsList ) {
			allLessonsApplicableItems.add(lesson.getName());			
		}
		modelAndView.addObject("allLessonsApplicableItems", allLessonsApplicableItems);

		// 11/9: experimenting
		modelAndView.addObject("allLessonsApplicable", lessonsList);

		
	}

	/**
	 * Retrieve all operations systems and add to model
	 * 
	 * @param modelAndView
	 */
	private void setToModelAndViewAllOperatingSystemApplicableItems(ModelAndView modelAndView) {
		// extract all operating systems info from repository
		ArrayList<OperatingSystem> operatingSystemList  = (ArrayList<OperatingSystem>) operatingSystemProcessingService.retrieveAllOperatingSystems();
		// add each operating system name to list for UI display
		List<String> allOperatingSystemApplicableItems = new ArrayList<String>();
		for (OperatingSystem operatingSystem : operatingSystemList ) {
			allOperatingSystemApplicableItems.add(operatingSystem.getName());			
		}
		modelAndView.addObject("allOperatingSystemApplicableItems", allOperatingSystemApplicableItems);
	}
	

}
