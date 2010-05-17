package com.apex.sso.askuser.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import com.apex.commons.utils.DateUtil;
import com.apex.sso.askuser.IAskUserDao;
import com.apex.sso.entity.User;
import com.apex.sso.support.HibernateSessionSupport;

@Repository
@SuppressWarnings("unchecked")
public class AskUserDao extends HibernateSessionSupport implements IAskUserDao {
	protected final Log logger = LogFactory.getLog(getClass());

	@Override
	public void deleteUser(User user) {
		getSession().createQuery("delete from User where serverid = :serverid and userid=:userid").setInteger("serverid", user.getServerid()).setString("userid", user.getUserid()).executeUpdate();

	}

	@Override
	public User getUser(String[] ticketInfo) {
		return (User)getSession().createQuery("from User where id = :id").setInteger("id", Integer.valueOf(ticketInfo[1])).uniqueResult();
	}

	@Override
	public void deleteUserBeforeGet(String[] ticketInfo) {
		getSession().createQuery("delete from User where serverid = :serverid and userid=:userid and validtime < :validtime").setInteger("serverid", Integer.valueOf(ticketInfo[0]))
		.setString("userid", ticketInfo[2]).setString("validtime", DateUtil.getNow("yyyyMMddHHmmssSSS")).executeUpdate();

	}

}
