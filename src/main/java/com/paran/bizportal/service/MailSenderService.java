package com.paran.bizportal.service;

public interface MailSenderService {
	public void send(String to, String subject,String contents);
}
