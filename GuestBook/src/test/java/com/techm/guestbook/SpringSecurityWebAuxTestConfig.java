package com.techm.guestbook;

import java.util.Arrays;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@TestConfiguration
public class SpringSecurityWebAuxTestConfig {

    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        
		User basicUser = new User("guest","$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6",Arrays.asList(new SimpleGrantedAuthority("ROLE_GUEST")));
		User managerUser =  new User("admin","$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6",Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));

        return new InMemoryUserDetailsManager(Arrays.asList(
        		basicUser, managerUser
        ));
    }
}
