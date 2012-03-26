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
	 * ���� �߼� 
	 */
	public void send(String to, String subject,String contents){
		new Thread(new MailSenderThread(to, subject,contents)).start();
	}
	
	/**
	 * ����Ŭ���� 
	 * ���� ���� �߼� Ŭ���� �̸� response ������������ ���� �������  �����Ѵ�.
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
				
				logger.info("���� : {}", subject);
				logger.info("�۽���: {}", from);
				logger.info("������ : {}", to);
				logger.info("���۽ð�: {}ms", (System.currentTimeMillis() - start));
						
			} catch (Exception e) {
				logger.warn(e.getMessage(), e);
			}
		}
	}
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
}
