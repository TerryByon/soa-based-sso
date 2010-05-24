package com.atlas.fun.sso.addserver.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import com.atlas.fun.sso.addserver.IAddServerDao;
import com.atlas.fun.sso.entity.Server;
import com.atlas.fun.sso.support.HibernateSessionSupport;

@Repository
@SuppressWarnings("unchecked")
public class AddServerDao extends HibernateSessionSupport implements IAddServerDao {
	protected final Log logger = LogFactory.getLog(getClass());


	@Override
	public List<Server> getServerList() {
		return getSession().createQuery("from Server").list();
	}


	@Override
	public boolean hasServer(Server server) {
		Number num = (Number)getSession().createQuery("select count(*) from Server where ip = :ip").setString("ip", server.getIp()).uniqueResult();
		if(num!=null && num.intValue()>0) return true;
		else return false;
	}


	@Override
	public void insertServer(Server server) {
		getSession().save(server);
	}

}
