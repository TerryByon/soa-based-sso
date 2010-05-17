package com.apex.sso.addserver;

import java.util.List;

import com.apex.sso.entity.Server;

public interface IAddServerManager {

	public boolean insertServer(Server server);

	public List<Server> getServerList();
	
}
