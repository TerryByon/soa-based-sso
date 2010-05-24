package com.atlas.fun.sso.askticket;

import com.atlas.fun.sso.entity.User;

public interface IAskTicketDao {

	public void insertUser(User user);

	public void deleteUser(User user);

}
