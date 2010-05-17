package com.apex.sso.askticket;

import com.apex.sso.entity.User;

public interface IAskTicketDao {

	public void insertUser(User user);

	public void deleteUser(User user);

}
