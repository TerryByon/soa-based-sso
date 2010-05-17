package com.apex.sso.askuser;

import com.apex.sso.entity.User;

public interface IAskUserManager {

	public User getUser(String[] ticketInfo);

	public void deleteUser(User user);

}
