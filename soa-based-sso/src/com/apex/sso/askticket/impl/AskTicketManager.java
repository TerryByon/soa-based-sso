package com.apex.sso.askticket.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apex.commons.utils.DateUtil;
import com.apex.sso.askticket.IAskTicketDao;
import com.apex.sso.askticket.IAskTicketManager;
import com.apex.sso.entity.User;

import com.apex.sso.entity.Server;

@Service
public class AskTicketManager implements IAskTicketManager {
	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private IAskTicketDao dao;

	@Override
	public void insertUser(String serverid, User user) {
//		Server server = new Server();
//		server.setId(Integer.valueOf(serverid));
//		user.setServer(server);
		user.setServerid(Integer.valueOf(serverid));
		user.setValidtime(DateUtil.dateAdd(DateUtil.ADD_SECOND, 1000, "yyyyMMddHHmmssSSS", "yyyyMMddHHmmssSSS"));
		dao.deleteUser(user);
		dao.insertUser(user);
	}

}
