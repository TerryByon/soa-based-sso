package com.atlas.fun.sso.askticket;

import com.atlas.fun.sso.entity.User;

public interface IAskTicketManager {

	public void insertUser(String serverId, User user);

}
