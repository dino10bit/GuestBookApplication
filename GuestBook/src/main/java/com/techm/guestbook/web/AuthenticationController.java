package com.techm.guestbook.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.techm.guestbook.payload.AuthenticationRequest;
import com.techm.guestbook.security.CustomUserDetailsService;
import com.techm.guestbook.security.JwtUtil;

@Controller
public class AuthenticationController {

	private final Logger log = LoggerFactory.getLogger(AuthenticationController.class);
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Autowired
	private JwtUtil jwtUtil;

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public String createAuthenticationToken(Model model,
             @ModelAttribute("login") AuthenticationRequest authenticationRequest)
			throws Exception {
		
		try {
			log.info("In auth controller");
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationRequest.getUsername(), authenticationRequest.getPassword()));
//			 SecurityContextHolder.getContext().setAuthentication(authentication);
		} catch(DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		}
		catch(BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
		UserDetails userdetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		String token = jwtUtil.generateToken(userdetails);
		model.addAttribute("token",token);
		model.addAttribute("user", authenticationRequest.getUsername());
		if(userdetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
			return "adminpage";
		}else {
			return "userpage";
		}
		
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout()
			throws Exception {
		
		try {
			log.info("In auth controller logout");
		} catch(DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		}
		catch(BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
		return "index";
		
	}
}

