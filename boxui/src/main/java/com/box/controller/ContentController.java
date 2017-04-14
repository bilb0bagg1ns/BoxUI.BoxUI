package com.box.controller;

import java.io.IOException;

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

import com.box.model.domain.SubjectFocus;
import com.box.model.services.AuthenticationService;

@Controller
public class ContentController {

	private final Logger log = LoggerFactory.getLogger(ContentController.class);

	@Inject
	private AuthenticationService authenticationService;

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
	@RequestMapping(value = "/focusArea", method = RequestMethod.POST, params = "action=linux")
	public ModelAndView focusAreaLinux(HttpServletRequest request, HttpSession session, ModelAndView model, Model m,
			@ModelAttribute SubjectFocus subjectFocus) {
		log.debug(
				"\nContentController:focusAreaLinux: SubjectFocus : " + subjectFocus + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");

		// return routeToLessonList(session, model, m,
		// lesson.getSkillLevelTypeId());
		return null;
	}

	@RequestMapping(value = "/focusArea", method = RequestMethod.POST, params = "action=windows")
	public ModelAndView focusAreaWindows(HttpServletRequest request, HttpSession session, ModelAndView model, Model m,
			@ModelAttribute SubjectFocus subjectFocus) {
		log.debug("\nContentController:focusAreaWindows: SubjectFocus : " + subjectFocus
				+ "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");

		// return routeToLessonList(session, model, m,
		// lesson.getSkillLevelTypeId());
		return null;
	}

}
