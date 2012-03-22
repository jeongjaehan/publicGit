package com.paran.bizportal.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Provider;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.paran.bizportal.domain.LoginInfo;
import com.paran.bizportal.domain.User;
import com.paran.bizportal.service.UserService;
import com.paran.bizportal.validation.PasswordUpdateValidator;

/**
 * ��������  Action �� ������ ��Ʈ�ѷ� 
 * @author ������
 *
 */
@Controller
@RequestMapping("/account")
public class AccountController {

	@Autowired 	private Provider<LoginInfo> loginInfoProvider;
	@Autowired 	private UserService userService;
	@Autowired 	private PasswordUpdateValidator passwordUpdateValidator;

	@ModelAttribute("currentUser")
	public User currentUser(){
		return loginInfoProvider.get().currentUser();
	}
	
	/**
	 * ������ ��ȸ
	 * @return
	 */
	@RequestMapping(value="{login}/myinfo",method=RequestMethod.GET)
	public String myInfoForm(@PathVariable String login , ModelMap model){
		User user = userService.getUser(login);
		
		if(user==null || !login.equals(currentUser().getLogin())) // ��ȸ�� ����ڰ� ���ų�, ���ǻ��� ���̵�� Ʋ����� ���ٿ��� ó��
			return "/accessdenied"; 
			
		model.addAttribute("user", userService.getUser(login));
		return "account/myinfo";
	}
	
	/**
	 * ������ ����
	 * @return
	 */
	@RequestMapping(value="{login}/myinfo",method=RequestMethod.PUT)
	public String updateMyinfo(@ModelAttribute @Valid User user, BindingResult result){
		if (result.hasErrors()) return "account/myinfo";
		
		userService.updateMyinfo(user);							// DB update
		currentUser().setType_profile(user.getType_profile());	// session type_profile update
		
		return "account/myinfo";
	}
	
	/**
	 * �н����� ���� (ajax)
	 * @return json
	 */
	@RequestMapping(value="{login}/myinfo/password",method=RequestMethod.PUT)
	@ResponseBody
	public  Map<String, ? extends Object> updatePassword(@ModelAttribute @Valid User user, BindingResult result){
		passwordUpdateValidator.validate(user, result);
		
		Map<String, Object> reponseMap = new HashMap<String, Object>();
		
		if (result.hasErrors()){
			
			List<String> errorCodes = new ArrayList<String>();
			
			if(result.hasGlobalErrors()){ // �ʵ� ������ ������ �۷ι� ������ ó����.
				for (ObjectError objectError : result.getGlobalErrors()) {
					errorCodes.add(objectError.getCode()); // �����ڵ常 ����
				}
			}
			
			reponseMap.put("isErrors", true);
			reponseMap.put("errorCodes", errorCodes);
//			reponseMap.put("errors", result.getAllErrors());
			
		}else{
			reponseMap.put("isErrors", false);
			userService.updatePassword(user);	// �н����� DB �ݿ�
		}
		
		return reponseMap;
	}


	
	/**
	 * ������ ����
	 * @return
	 */
	@RequestMapping(value="{login}/myinfo",method=RequestMethod.DELETE)
	public String deleteMyInfo(@ModelAttribute @Valid User user, BindingResult result){
		if (result.hasErrors()) return "account/myinfo";
		passwordUpdateValidator.validate(user, result);
		if (result.hasErrors()) return "account/myinfo";
		
		userService.deleteMyinfo(user);							// DB delete
		
		return "redirect:/logout";
	}
	
	/**
	 * Profile ��ȯ
	 * @return
	 */
	@RequestMapping(value="switch/profile",method=RequestMethod.GET)
	public String switchProfile(){
		if(currentUser().getType_profile()==null || currentUser().getType_profile().equals(""))
			return "/accessdenied";
		
		String type_profile = currentUser().getType_profile().equals("D") ? "P" : "D";	 // ������-> ��Ʈ��, ��Ʈ�� -> ������
		currentUser().setType_profile(type_profile);
		
		return "redirect:/";
	}


	private Logger logger = LoggerFactory.getLogger(this.getClass());
}
