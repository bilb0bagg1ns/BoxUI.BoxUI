package com.box;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	 
	  @Override
	  protected void configure(HttpSecurity http) throws Exception {
	  http
	  .authorizeRequests()
	      .antMatchers("/", "/homes").permitAll()
	      .anyRequest().authenticated()
	      .and()          
	  .formLogin()
	      .loginPage("/logins")
	      .permitAll()
	      .and()
	      .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login")
	      .and()
	  .httpBasic().disable();
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