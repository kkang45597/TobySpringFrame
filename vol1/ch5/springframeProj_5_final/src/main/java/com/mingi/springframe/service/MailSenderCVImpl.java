package com.mingi.springframe.service;

import java.util.Properties;

import org.springframework.mail.javamail.JavaMailSenderImpl;

public class MailSenderCVImpl extends JavaMailSenderImpl {
	
	Properties props;
	
	public void setProps(Properties props) {
		this.props = props;
		settingProperites();
	}
	
	private void settingProperites() {
		this.setProps(props);
	}

}