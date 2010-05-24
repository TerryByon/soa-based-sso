package com.atlas.fun.sso.handShake.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import com.atlas.fun.sso.entity.Server;
import com.atlas.fun.sso.handShake.IHandShakeDao;
import com.atlas.fun.sso.support.HibernateSessionSupport;

@Repository
//@SuppressWarnings("unchecked")
public class HandShakeDao extends HibernateSessionSupport implements IHandShakeDao {
	protected final Log logger = LogFactory.getLog(getClass());

	@Override
	public Server getServer(String ip) {
		return (Server)getSession().createQuery("from Server where ip=:ip").setString("ip",ip).uniqueResult();
	}

}
