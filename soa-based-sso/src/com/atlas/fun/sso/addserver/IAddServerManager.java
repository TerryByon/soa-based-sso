package com.atlas.fun.sso.addserver;

import java.util.List;

import com.atlas.fun.sso.entity.Server;

public interface IAddServerManager {

	public boolean insertServer(Server server);

	public List<Server> getServerList();

}
