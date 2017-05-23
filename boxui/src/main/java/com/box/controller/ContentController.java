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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.box.model.domain.Lesson;
import com.box.model.domain.LessonListWrapper;
import com.box.model.domain.LessonRows;
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

		// put subjectFocus (Windows or Linux) on session.
		// TODO: Need to figure out to pass value via Model (didn't work).
		// Tried via Flash attributes as well. Didn't work.
		// https://stackoverflow.com/questions/24301114/passing-model-attribute-during-redirect-in-spring-mvc-and-avoiding-the-same-in-u
		session.setAttribute("subjectFocus", subjectFocus);

		modelAndView.setViewName("selection/learnOrProveFocusArea");

		return modelAndView;
	}

	@RequestMapping(value = "/operatingSystemFocus", method = RequestMethod.POST, params = "action=windows")
	public ModelAndView operatingSystemFocusWindows(HttpServletRequest request, HttpSession session,
			ModelAndView modelAndView, Model model, @ModelAttribute SubjectFocus subjectFocus) {
		log.debug("\nContentController:focusAreaWindows: SubjectFocus : " + subjectFocus
				+ "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");
		subjectFocus.setOperatingSystem("windows");

		// put subjectFocus (Windows or Linux) on session.
		// TODO: Need to figure out to pass value via Model (didn't work).
		// Tried via Flash attributes as well. Didn't work.
		// https://stackoverflow.com/questions/24301114/passing-model-attribute-during-redirect-in-spring-mvc-and-avoiding-the-same-in-u
		session.setAttribute("subjectFocus", subjectFocus);

		modelAndView.setViewName("selection/learnOrProveFocusArea");
		return modelAndView;
	}

	@RequestMapping(value = "/focusArea", method = RequestMethod.POST, params = "action=learn")
	public ModelAndView focusAreaLearn(HttpServletRequest request, HttpSession session, ModelAndView modelAndView,
			Model model, @ModelAttribute("subjectFocus") SubjectFocus subjectFocus) {
		log.debug(
				"\nContentController:focusAreaLearn: SubjectFocus : " + subjectFocus + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");

		subjectFocus = (SubjectFocus) session.getAttribute("subjectFocus");

		// retrieve lessons
		ArrayList<Lesson> lessonsList = (ArrayList<Lesson>) lessonsProcessingService.retrieveAllLessons();
		model.addAttribute("lessonListTmp", lessonsList);

		// insert the lessons into LessonRows for rendering on UI in rows
		LessonListWrapper lessonListWrapperTmp = new LessonListWrapper();
		ArrayList<LessonRows> lessonRowsList = new ArrayList<LessonRows>();
		// create a lessons rows list with each element holding 4 lessons
		lessonRowsList = createLessonRowsList(lessonsList, lessonRowsList);
		// insert list into wrapper
		lessonListWrapperTmp.setLessonRowsList(lessonRowsList);
		model.addAttribute("lessonListWrapper", lessonListWrapperTmp);

		// subjectFocus.setOperatingSystem("windows");
		// model.addAttribute("subjectFocus", subjectFocus);
		modelAndView.setViewName("selection/learningTopics");

		// return routeToLessonList(session, model, m,
		// lesson.getSkillLevelTypeId());
		return modelAndView;
	}

	@RequestMapping(value = "/renderLearningTopic", method = RequestMethod.POST, params = "action=cancel")
	public ModelAndView focusAreaLearnCancel(HttpServletRequest request, HttpSession session, ModelAndView modelAndView,
			Model model, @ModelAttribute SubjectFocus subjectFocus) {
		log.debug("\nContentController:focusAreaLearnCancel: SubjectFocus : " + subjectFocus
				+ "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");

		modelAndView.setViewName("selection/learnOrProveFocusArea");

		return modelAndView;

	}

	@RequestMapping(value = "/renderLearningTopic", method = RequestMethod.POST)
	public ModelAndView renderLearningTopic(HttpServletRequest request, HttpSession session, ModelAndView modelAndView,
			Model model, @ModelAttribute("subjectFocus") SubjectFocus subjectFocus,
			@RequestParam(value = "action") String value) {
		log.debug("\nContentController:renderLearningTopic: SubjectFocus : " + subjectFocus
				+ "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");

		subjectFocus = (SubjectFocus) session.getAttribute("subjectFocus");

		// retrieve lessons
		ArrayList<Lesson> lessonsList = (ArrayList<Lesson>) lessonsProcessingService.retrieveAllLessons();
		model.addAttribute("lessonListTmp", lessonsList);

		// insert the lessons into LessonRows for rendering on UI in rows
		LessonListWrapper lessonListWrapperTmp = new LessonListWrapper();
		ArrayList<LessonRows> lessonRowsList = new ArrayList<LessonRows>();
		// create a lessons rows list with each element holding 4 lessons
		lessonRowsList = createLessonRowsList(lessonsList, lessonRowsList);
		// insert list into wrapper
		lessonListWrapperTmp.setLessonRowsList(lessonRowsList);
		model.addAttribute("lessonListWrapper", lessonListWrapperTmp);

		// subjectFocus.setOperatingSystem("windows");
		// model.addAttribute("subjectFocus", subjectFocus);
		modelAndView.setViewName("selection/learningTopics");

		// return routeToLessonList(session, model, m,
		// lesson.getSkillLevelTypeId());
		return modelAndView;
	}

	@RequestMapping(value = "/renderLesson")
	public ModelAndView editLesson(ModelAndView modelAndView, @RequestParam("lessonHtmlPage") String lessonHtmlPage)
			throws IOException {
		log.debug("Editing lessonHtmlPage: " + lessonHtmlPage + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");

		modelAndView.setViewName(lessonHtmlPage);
		// modelAndView.setViewName("greeting_tmp");
		return modelAndView;
	}

	/**
	 * TODO: Need to render lessons in rows, instead all in a column. Ugly hack,
	 * by creating a LessonsList with each element holding 4 lessons.
	 * 
	 * Better solution would be for the UI to walk through a typical list and
	 * render 'n' lessons per row. Need to figure out how to do this in
	 * Thymeleaf
	 * 
	 */
	private ArrayList<LessonRows> createLessonRowsList(ArrayList<Lesson> lessonsList,
			ArrayList<LessonRows> lessonRowsList) {
		// iterate over lessons list
		for (int i = 0, j = lessonsList.size(); i < lessonsList.size(); i++) {
			// if we have 4 lessons, then add each one to LessonRows
			if (j >= 4) {
				LessonRows lessonRows = new LessonRows();
				lessonRows.setLessonOne(lessonsList.get(i));
				lessonRows.setLessonTwo(lessonsList.get(i + 1));
				lessonRows.setLessonThree(lessonsList.get(i + 2));
				lessonRows.setLessonFour(lessonsList.get(i + 3));
				j = j - 4;
				i = i + 3;
				lessonRowsList.add(lessonRows); // now 4 lessons added to list
			} else {
				// we have less then 4, so add them based on remaining left
				LessonRows lessonRows = new LessonRows();
				switch (j) {
				case 1:
					lessonRows.setLessonOne(lessonsList.get(i));
					break;
				case 2:
					lessonRows.setLessonOne(lessonsList.get(i));
					lessonRows.setLessonTwo(lessonsList.get(i + 1));
					break;
				case 3:
					lessonRows.setLessonOne(lessonsList.get(i));
					lessonRows.setLessonTwo(lessonsList.get(i + 1));
					lessonRows.setLessonThree(lessonsList.get(i + 2));
					break;
				default:
					break;
				}
				lessonRowsList.add(lessonRows); // now remaining lessons added
												// to list
				break;
			}

		}
		return lessonRowsList;
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
