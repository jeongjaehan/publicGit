package com.jaehan.portal.service;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailSenderServiceImpl implements MailSenderService{
	@Autowired	private JavaMailSender mailSender;
	
	@Value("#{mailProperties['mail.from']}")
	private String from;	
	
	/**
	 * 메일 발송 
	 */
	public void send(String to, String subject,String contents){
		new Thread(new MailSenderThread(to, subject,contents)).start();
	}
	
	/**
	 * 내부클래스 
	 * 실제 메일 발송 클래스 이며 response 지연현상으로 인해 쓰레드로  동작한다.
	 */
	private class MailSenderThread implements Runnable{
		
		private String to;
		
		private String subject;
		
		private String contents;
		
		public MailSenderThread(String to, String subject,String contents) {
			this.to = to;
			this.subject = subject;
			this.contents = contents;
		}
		
		@Override
		public void run() {
			try {
				long start = System.currentTimeMillis();
				MimeMessage message = mailSender.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(message);
				helper.setTo(to);
				helper.setFrom(from);
				helper.setSubject(subject);
				helper.setText(contents);
				mailSender.send(message);
				
				logger.info("제목 : {}", subject);
				logger.info("송신자: {}", from);
				logger.info("수신자 : {}", to);
				logger.info("전송시간: {}ms", (System.currentTimeMillis() - start));
						
			} catch (Exception e) {
				logger.warn(e.getMessage(), e);
			}
		}
	}
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
}
