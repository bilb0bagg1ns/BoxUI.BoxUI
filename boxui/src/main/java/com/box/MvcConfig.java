package com.box;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
* Reference: 
*   http://ryanjbaxter.com/2015/01/06/securing-rest-apis-with-spring-boot/
*   https://spring.io/guides/gs/securing-web/
*/

@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {
    
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/homes").setViewName("homes");
//        registry.addViewController("/").setViewName("homes");
//        registry.addViewController("/").setViewName("login");
        registry.addViewController("/hello").setViewName("hello");
        registry.addViewController("/login").setViewName("home/login");
    }

}
