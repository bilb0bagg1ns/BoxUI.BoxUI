package com.box;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.box.model.data.repository.UserRepository;
import com.box.model.domain.User;

/**
 * Reference: 
 *   http://ryanjbaxter.com/2015/01/06/securing-rest-apis-with-spring-boot/
 *   https://spring.io/guides/gs/securing-web/
 * 
 * @author mike.prasad
 *
 */
@Configuration
public class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter{

	 @Autowired
	  UserRepository userRepository;

	  @Override
	  public void init(AuthenticationManagerBuilder auth) throws Exception {
	    auth.userDetailsService(userDetailsService());
	  }

	  @Bean
	  UserDetailsService userDetailsService() {
	    return new UserDetailsService() {

	      @Override
	      public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	        User user = userRepository.findByUserName(username);
	        if(user != null) {
	        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), true, true, true, true,
	                AuthorityUtils.createAuthorityList("USER"));
	        } else {
	          throw new UsernameNotFoundException("could not find the user '"
	                  + username + "'");
	        }
	      }
	      
	    };
	  }
}
