package com.jaehan.portal.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.jaehan.portal.domain.User;
import com.jaehan.portal.persistence.UserMapper;

@Service
@Transactional
public class UserServiceImpl implements UserService{
	
	@Autowired private UserMapper userMapper;
	
	/**
	 * 회원가입 서비스
	 */
	@Override
	public void signup(User user) {
		userMapper.add(user);	// db insert
	}
	
	/**
	 * 유저 카운트 조회  서비스 select key login
	 */
	@Override
	public int getUserCountByLogin(String login) {
		User user = new User();
		user.setLogin(login);
		return userMapper.count(user);
	}
	
	/**
	 * 유저 카운트 조회  서비스 select key alias
	 */
	@Override
	public int getUserCountByAlias(String alias) {
		User user = new User();
		user.setAlias(alias);
		return userMapper.count(user);
	}
	
	/**
	 * 유저 목록조회 서비스
	 */
	@Override
	public List<User> getUserList() {
		return null;
	}
	
	/**
	 * 유저 찾기 서비스
	 */
	@Override
	public User getUser(String login) {
		return userMapper.get(login);
	}
	
	/**
	 * Myinfo 변경 서비스 
	 */
	@Override
	public void updateMyinfo(User user) {
		// 패스워드는 controller 에서 검증용으로 사용하기 위함이며, 업데이트 대상이 아니기 때문에 null로 세팅.
		user.setPassword(null);	
		userMapper.update(user);
	}
	
	/**
	 * Myinfo 삭제 서비스 
	 */
	@Override
	public void deleteMyinfo(User user) {
		userMapper.delete(user);
	}
	
	/**
	 * 패스워드 변경 서비스  
	 */
	@Override
	public void updatePassword(User user) {
		user.setPassword(user.getNew_password()); // 기존패스워드를 새로운패스워드로 세팅
		userMapper.update(user);
	}
	/**
	 * 이메일 토큰 인증 
	 */
	@Override
	public void emailCertification(User targetUser){
		User findUser = userMapper.get(targetUser.getLogin());
		
		if(findUser.getToken_email() != null || findUser.getToken_email().equals("")){
			if(!findUser.getToken_email().equals(targetUser.getToken_email())){
				//TODO Exception 처리하기
				logger.warn("이메일 토큰이 맞지 않음 : "+targetUser.getToken_email()+" , "+findUser.getToken_email());
			}else{
				userMapper.update(targetUser);
			}
		}
		
	}
	
	private Logger logger = Logger.getLogger(this.getClass());

}
