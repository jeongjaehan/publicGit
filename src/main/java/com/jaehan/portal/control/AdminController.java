package com.jaehan.portal.control;

import javax.inject.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jaehan.portal.domain.LoginInfo;
import com.jaehan.portal.domain.User;

@Controller
@RequestMapping("/admin")
public class AdminController {
	@Autowired	private Provider<LoginInfo> loginInfoProvider;

	@ModelAttribute("currentUser")
	public User currentUser() {
		return loginInfoProvider.get().currentUser();
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public String main(){
		return "admin/main";
	}
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
}
