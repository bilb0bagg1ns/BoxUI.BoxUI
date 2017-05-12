package com.box.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.box.model.domain.Lesson;
import com.box.model.domain.LessonListWrapper;
import com.box.model.domain.SubjectFocus;
import com.box.model.services.AuthenticationService;
import com.box.model.services.LessonsProcessingService;

@Controller
public class ContentController {

	private final Logger log = LoggerFactory.getLogger(ContentController.class);

	@Inject
	private AuthenticationService authenticationService;

	@Inject
	private LessonsProcessingService lessonsProcessingService;

	@RequestMapping(value = "/chooseOperatingSystem")
	public ModelAndView chooseOperatingSystem(ModelAndView model) throws IOException {
		log.debug("ContentController:chooseOperatingSystem: " + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");

		model.setViewName("selection/operatingSystem");
		return model;
	}

	/**
	 * http://stackoverflow.com/questions/31401669/thymeleaf-multiple-submit-button-in-one-form
	 * http://stackoverflow.com/questions/804581/spring-mvc-controller-redirect-to-previous-page
	 * 
	 * @return
	 */
	@RequestMapping(value = "/operatingSystemFocus", method = RequestMethod.POST, params = "action=linux")
	public ModelAndView operatingSystemFocusLinux(HttpServletRequest request, HttpSession session,
			ModelAndView modelAndView, Model model, @ModelAttribute SubjectFocus subjectFocus) {
		log.debug(
				"\nContentController:focusAreaLinux: SubjectFocus : " + subjectFocus + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");
		subjectFocus.setOperatingSystem("linux");
		model.addAttribute("subjectFocus", subjectFocus);
		modelAndView.setViewName("selection/learnOrProveFocusArea");

		// return routeToLessonList(session, model, m,
		// lesson.getSkillLevelTypeId());
		return modelAndView;
	}

	@RequestMapping(value = "/operatingSystemFocus", method = RequestMethod.POST, params = "action=windows")
	public ModelAndView operatingSystemFocusWindows(HttpServletRequest request, HttpSession session,
			ModelAndView modelAndView, Model model, @ModelAttribute SubjectFocus subjectFocus) {
		log.debug("\nContentController:focusAreaWindows: SubjectFocus : " + subjectFocus
				+ "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");
		subjectFocus.setOperatingSystem("windows");
		model.addAttribute("subjectFocus", subjectFocus);
		modelAndView.setViewName("selection/learnOrProveFocusArea");

		// return routeToLessonList(session, model, m,
		// lesson.getSkillLevelTypeId());
		return modelAndView;
	}

	@RequestMapping(value = "/focusArea", method = RequestMethod.POST, params = "action=learn")
	public ModelAndView focusAreaLearn(HttpServletRequest request, HttpSession session, ModelAndView modelAndView,
			Model model, @ModelAttribute SubjectFocus subjectFocus) {
		log.debug(
				"\nContentController:focusAreaLearn: SubjectFocus : " + subjectFocus + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");

		// retrieve lessons
		ArrayList<Lesson> lessonsList = (ArrayList<Lesson>) lessonsProcessingService.retrieveAllLessons();
		model.addAttribute("lessonListTmp", lessonsList);

		// insert list into wrapper
		LessonListWrapper lessonListWrapperTmp = new LessonListWrapper();
		lessonListWrapperTmp.setLessonList(lessonsList);
		model.addAttribute("lessonListWrapper", lessonListWrapperTmp);

		subjectFocus.setOperatingSystem("windows");
		model.addAttribute("subjectFocus", subjectFocus);
		modelAndView.setViewName("selection/learningTopics");

		// return routeToLessonList(session, model, m,
		// lesson.getSkillLevelTypeId());
		return modelAndView;
	}

	@RequestMapping(value = "/focusArea", method = RequestMethod.POST, params = "action=prove")
	public ModelAndView focusAreaProve(HttpServletRequest request, HttpSession session, ModelAndView modelAndView,
			Model model, @ModelAttribute SubjectFocus subjectFocus) {
		log.debug(
				"\nContentController:focusAreaProve: SubjectFocus : " + subjectFocus + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");
		model.addAttribute("subjectFocus", subjectFocus);
		modelAndView.setViewName("selection/learnOrProveFocusArea");

		// return routeToLessonList(session, model, m,
		// lesson.getSkillLevelTypeId());
		return modelAndView;
	}

}
