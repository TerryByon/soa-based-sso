package com.apex.sso.askticket.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import com.apex.sso.askticket.IAskTicketDao;
import com.apex.sso.entity.User;
import com.apex.sso.support.HibernateSessionSupport;

@Repository
//@SuppressWarnings("unchecked")
public class AskTicketDao extends HibernateSessionSupport implements IAskTicketDao {
	protected final Log logger = LogFactory.getLog(getClass());

	@Override
	public void deleteUser(User user) {
		getSession().createQuery("delete from User where serverid=:serverid and userid=:userid").setInteger("serverid", user.getServerid()).setString("userid", user.getUserid()).executeUpdate();

	}

	@Override
	public void insertUser(User user) {
		getSession().save(user);
	}

}
