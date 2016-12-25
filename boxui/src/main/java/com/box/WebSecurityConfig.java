package com.box;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	 /** 
	  * Spring Security will force login page first before registering. To have it ignore this enforcement for registration, we have to do the following.
	  */
	 @Override
	 public void configure(WebSecurity web) throws Exception {
        web
        .ignoring()
        .antMatchers("/register", "/registration");
	  }
	   
	 /*-
	  * 1. Had to disable csrf (Cross Site Request Forgery), else registration page had "invalid csrf issue". See http://tinyurl.com/disable-CSRF
	  */
	  @Override
	  protected void configure(HttpSecurity http) throws Exception {
	  http
	  .csrf().disable()  // 1. see comment above
	  .authorizeRequests()
	      .antMatchers("/", "/homes").permitAll()
	      .anyRequest().authenticated()
	      .and()          
	  .formLogin()
	      .loginPage("/login")
	      .permitAll()
	      .and()
	      .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login")
	      .and()    
	  .httpBasic().disable();  // disable basic authentication (else authentication dialog will popup
	  
	}

// Works for everything but for logout	  
//	  @Override
//	  protected void configure(HttpSecurity http) throws Exception {
//	  http
//	  .authorizeRequests()
//	      .antMatchers("/", "/homes").permitAll()
//	      .anyRequest().authenticated()
//	      .and()          
//	  .formLogin()
//	      .loginPage("/logins")
//	      .permitAll()
//	      .and()
//	  .logout()
//	      .permitAll().and()
//	  .httpBasic().disable();
//	}	

	  
}