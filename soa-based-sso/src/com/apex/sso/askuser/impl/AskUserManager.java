package com.apex.sso.askuser.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apex.sso.askuser.IAskUserDao;
import com.apex.sso.askuser.IAskUserManager;
import com.apex.sso.entity.User;

@Service
public class AskUserManager implements IAskUserManager {
	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private IAskUserDao dao;

	@Override
	public User getUser(String[] ticketInfo) {
		dao.deleteUserBeforeGet(ticketInfo);
		return dao.getUser(ticketInfo);
	}

	@Override
	public void deleteUser(User user) {
		dao.deleteUser(user);
	}




}
