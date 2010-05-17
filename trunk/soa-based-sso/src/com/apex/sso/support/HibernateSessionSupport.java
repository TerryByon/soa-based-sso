package com.apex.sso.support;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class HibernateSessionSupport {
	@Autowired
	private SessionFactory sessionFactory;
	public void setSessionFactory(SessionFactory sessionFactory) {this.sessionFactory = sessionFactory;}
	public Session getSession() {
		return this.sessionFactory.getCurrentSession();
	}
}
