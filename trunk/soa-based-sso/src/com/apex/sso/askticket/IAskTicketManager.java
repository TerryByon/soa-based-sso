package com.apex.sso.askticket;

import com.apex.sso.entity.User;

public interface IAskTicketManager {

	public void insertUser(String serverId, User user);

}
