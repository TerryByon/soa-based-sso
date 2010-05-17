package com.apex.sso.handShake.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apex.sso.entity.Server;
import com.apex.sso.handShake.IHandShakeDao;
import com.apex.sso.handShake.IHandShakeManger;

@Service
public class HandShakeManager implements IHandShakeManger {
	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private IHandShakeDao dao;

	@Override
	public Server getServer(String ip) {
		// TODO Auto-generated method stub
		return dao.getServer(ip);
	}

}
