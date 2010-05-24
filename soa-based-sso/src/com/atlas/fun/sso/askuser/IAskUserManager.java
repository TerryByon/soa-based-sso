package com.atlas.fun.sso.askuser;

import com.atlas.fun.sso.entity.User;

public interface IAskUserManager {

	public User getUser(String[] ticketInfo);

	public void deleteUser(User user);

}
