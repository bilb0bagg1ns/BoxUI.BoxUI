package com.box.converter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

import com.box.model.domain.Lesson;



/**
 * https://stackoverflow.com/questions/35025550/register-spring-converter-programmatically-in-spring-boot
 * 
 * Was getting an error (below), when I submitted the userEntryForm (adding new user with lessons and other items). The form would
 * submit a string for the lesson and I need a converter to convert the string into a lesson object.
 * 
 * Error:
 * Field error in object 'user' on field 'lessonList': rejected value [Lesson [id=5915ed47a6b94c2fc419e2aa, skillLevelTypeId=1, name=Access Rights Management, 
 * shortDescription=Access Rights Management, longDescription=null]]; 
 * codes [typeMismatch.user.lessonList,typeMismatch.lessonList,typeMismatch.java.util.List,typeMismatch]; 
 * arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [user.lessonList,lessonList]; 
 * arguments []; default message [lessonList]]; default message [Failed to convert property value of type 
 * 'java.lang.String' to required type 'java.util.List' for property 'lessonList'; 
 * nested exception is java.lang.IllegalStateException: Cannot convert value of type 'java.lang.String' to 
 * required type 'com.box.model.domain.Lesson' for property 'lessonList[0]': no matching editors or conversion 
 * strategy found]
 * 
 *
 */
//@Component
public class StringtoLessonConverter implements Converter<String, Lesson >{

	private final Logger log = LoggerFactory.getLogger(StringtoLessonConverter.class);

	private Lesson lesson = null;
	
    @Override
    public Lesson convert(String lessonString) {
		log.debug("\nStringtoLessonConverter:convert:: " + lessonString + "<<<<<<<<<<<<<<<<<<<>>>>>>>>>");
		
		if (lesson == null) {
			lesson = new Lesson();
		}
		
       if (lessonString.contains("id")) {
    	   lesson.setId(StringUtils.substringAfterLast(lessonString, "="));
       }
       if (lessonString.contains("skillLevelTypeId")) {
    	   lesson.setSkillLevelTypeId(StringUtils.substringAfterLast(lessonString, "="));
       }
       if (lessonString.contains("name")) {
    	   lesson.setName(StringUtils.substringAfterLast(lessonString, "="));
       }
       if (lessonString.contains("shortDescription")) {
    	   lesson.setShortDescription(StringUtils.substringAfterLast(lessonString, "="));
       }
       if (lessonString.contains("longDescription")) {
    	   lesson.setLongDescription(StringUtils.substringAfterLast(lessonString, "="));
       }
       
        return lesson;
    }
}
