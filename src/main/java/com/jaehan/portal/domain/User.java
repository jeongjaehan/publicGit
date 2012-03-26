package com.jaehan.portal.domain;

import java.sql.Timestamp;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Email;

import com.jaehan.portal.validation.Telephone;


public class User {
	@Size(min=4, max=100)
	@Email
	private String login;
	@Size(min=4, max=32)
	private String password;
	@Size(min=4, max=32)
	private String new_password;
	@Size(min=4, max=32)
	private String re_password;
	@Size(min=1, max=45)
	@Pattern(regexp = "[\uAC00-\uD7A3, a-z, A-Z]+", message = "영문,한글만 입력 가능합니다.")
	private String name;
	private String type_profile; 
	@Size(min=1, max=45)
	private String alias;
	@Size(min=4, max=100)
	@Email
	private String email;
	private String token_email;
	private String token_auth;
	@Telephone
	private String phone;
	private int validated;
	private Timestamp date_registered;
	private Timestamp date_validated;
	private Timestamp date_motified;
	private boolean sameId;
	

	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setType_profile(String type_profile) {
		this.type_profile = type_profile;
	}
	public String getType_profile() {
		return type_profile;
	}
	public String getAlias() {
		return alias;
	}	
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getToken_auth() {
		return token_auth;
	}
	public void setToken_auth(String token_auth) {
		this.token_auth = token_auth;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public int getValidated() {
		return validated;
	}
	public void setValidated(int validated) {
		this.validated = validated;
	}
	public Timestamp getDate_registered() {
		return date_registered;
	}
	public void setDate_registered(Timestamp date_registered) {
		this.date_registered = date_registered;
	}
	public Timestamp getDate_validated() {
		return date_validated;
	}
	public void setDate_validated(Timestamp date_validated) {
		this.date_validated = date_validated;
	}
	public Timestamp getDate_motified() {
		return date_motified;
	}
	public void setDate_motified(Timestamp date_motified) {
		this.date_motified = date_motified;
	}
	public String getToken_email() {
		return token_email;
	}
	public void setToken_email(String token_email) {
		this.token_email = token_email;
	}
	public boolean isSameId() {
		return sameId;
	}
	public void setSameId(boolean sameId) {
		this.sameId = sameId;
	}
	public String getNew_password() {
		return new_password;
	}
	public void setNew_password(String new_password) {
		this.new_password = new_password;
	}
	public String getRe_password() {
		return re_password;
	}
	public void setRe_password(String re_password) {
		this.re_password = re_password;
	}
	
	
}
