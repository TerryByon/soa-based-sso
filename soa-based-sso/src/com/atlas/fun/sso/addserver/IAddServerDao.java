package com.atlas.fun.sso.addserver;

import java.util.List;

import com.atlas.fun.sso.entity.Server;

public interface IAddServerDao {

	public void insertServer(Server server);

	public List<Server> getServerList();

	public boolean hasServer(Server server);
  
}
