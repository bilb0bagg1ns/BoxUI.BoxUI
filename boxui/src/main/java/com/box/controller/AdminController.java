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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.box.model.domain.Lesson;
import com.box.model.domain.LessonListWrapper;
import com.box.model.domain.OperatingSystem;
import com.box.model.domain.OperatingSystemListWrapper;
import com.box.model.domain.User;
import com.box.model.services.LessonsProcessingService;
import com.box.model.services.OperatingSystemProcessingService;

@Controller
public class AdminController {

	private final Logger log = LoggerFactory.getLogger(AdminController.class);
	

	@Inject
	private LessonsProcessingService lessonsProcessingService;
	
	@Inject
	private OperatingSystemProcessingService operatingSystemProcessingService;
	
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

	@RequestMapping(value = "/contentManagement")
	public ModelAndView contentManagement(HttpSession session, ModelAndView modelAndView, Model model, @ModelAttribute Lesson lesson) throws IOException {

		log.debug("AdminController:contentManagement: " + lesson + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");

		modelAndView.setViewName("admin/contentManagement");
		return modelAndView;

	}
	
	@RequestMapping(value = "/operatingSystemEntry")
	public ModelAndView operatingSystemEntry(HttpSession session, ModelAndView modelAndView, Model model, @ModelAttribute OperatingSystem operatingSystem) throws IOException {

		log.debug("AdminController:operatingSystemEntry: " + operatingSystem + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");

		ArrayList<OperatingSystem> operatingSystemList  = (ArrayList<OperatingSystem>) operatingSystemProcessingService.retrieveAllOperatingSystems();

		// insert list into wrapper
		OperatingSystemListWrapper operatingSystemListWrapperTmp = new OperatingSystemListWrapper();
		operatingSystemListWrapperTmp.setOperatingSystemList(operatingSystemList);
		model.addAttribute("operatingSystemListWrapper", operatingSystemListWrapperTmp);

		modelAndView.setViewName("admin/operatingSystem/operatingSystemEntry");
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
	@RequestMapping(value = "/operatingSystemEntryForm")
	public ModelAndView operatingSystemEntryForm(ModelAndView modelAndView, @ModelAttribute OperatingSystem operatingSystem) throws IOException {
		log.debug("AdminController:operatingSystemEntryForm: " + operatingSystem + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");

		modelAndView.addObject("operatingSystem", operatingSystem);
		modelAndView.setViewName("admin/operatingsystem/operatingSystemEntryForm");
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
		ArrayList<OperatingSystem> operatingSystemList  = (ArrayList<OperatingSystem>) operatingSystemProcessingService.retrieveAllOperatingSystems();

		// insert list into wrapper
		OperatingSystemListWrapper operatingSystemListWrapperTmp = new OperatingSystemListWrapper();
		operatingSystemListWrapperTmp.setOperatingSystemList(operatingSystemList);
		model.addAttribute("operatingSystemListWrapper", operatingSystemListWrapperTmp);

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
	@RequestMapping(value = "/operatingSystemEntryUpdate")
	public ModelAndView operatingSystemEntryUpdate(ModelAndView modelAndView, @ModelAttribute OperatingSystem operatingSystem) throws IOException {

		modelAndView.addObject("operatingSystem", operatingSystem);
		modelAndView.setViewName("admin/operatingSystem/operatingSystemEntryUpdateForm");
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

		// initialize checkbox. Used in rendering checkboxes in lesson entry and
		// lesson entry update
		List<String> allSkillLevelApplicableItems = new ArrayList<String>();
		allSkillLevelApplicableItems.add("novice");
		allSkillLevelApplicableItems.add("intermediate");
		allSkillLevelApplicableItems.add("expert");
		modelAndView.addObject("allSkillLevelApplicableItems", allSkillLevelApplicableItems);

		List<String> allOperatingSystemApplicableItems = new ArrayList<String>();
		allOperatingSystemApplicableItems.add("windows");
		allOperatingSystemApplicableItems.add("linux");
		modelAndView.addObject("allOperatingSystemApplicableItems", allOperatingSystemApplicableItems);

		// initialize default value
		List<String> checkedItems = new ArrayList<String>();
		// novice will be checked by default.
		checkedItems.add("novice");
		lesson.setCheckedSkillsLevelsApplicableTo(checkedItems);

		modelAndView.addObject("lesson", lesson);
		modelAndView.setViewName("admin/lesson/lessonEntryForm");
		return modelAndView;
	}

	@RequestMapping(value = "/lessonEntryUpdate")
	public ModelAndView lessonEntryUpdate(ModelAndView modelAndView, @ModelAttribute Lesson lesson) throws IOException {
		// initialize checkbox. Used in rendering checkboxes in lesson entry and
		// lesson entry update
		List<String> allSkillLevelApplicableItems = new ArrayList<String>();
		allSkillLevelApplicableItems.add("novice");
		allSkillLevelApplicableItems.add("intermediate");
		allSkillLevelApplicableItems.add("expert");
		modelAndView.addObject("allSkillLevelApplicableItems", allSkillLevelApplicableItems);

		List<String> allOperatingSystemApplicableItems = new ArrayList<String>();
		allOperatingSystemApplicableItems.add("windows");
		allOperatingSystemApplicableItems.add("linux");
		modelAndView.addObject("allOperatingSystemApplicableItems", allOperatingSystemApplicableItems);

		modelAndView.addObject("lesson", lesson);
		modelAndView.setViewName("admin/lesson/lessonEntryUpdateForm");
		return modelAndView;
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
}
