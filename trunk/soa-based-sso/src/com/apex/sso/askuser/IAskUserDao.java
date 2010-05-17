package com.apex.sso.askuser;

import com.apex.sso.entity.User;

public interface IAskUserDao {

	public User getUser(String[] ticketInfo);

	public void deleteUser(User user);

	public void deleteUserBeforeGet(String[] ticketInfo);

}
