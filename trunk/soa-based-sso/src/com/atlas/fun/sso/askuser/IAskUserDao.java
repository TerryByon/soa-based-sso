package com.atlas.fun.sso.askuser;

import com.atlas.fun.sso.entity.User;

public interface IAskUserDao {

	public User getUser(String[] ticketInfo);

	public void deleteUser(User user);

	public void deleteUserBeforeGet(String[] ticketInfo);

}
