package com.apex.sso.addserver;

import java.util.List;

import com.apex.sso.entity.Server;

public interface IAddServerDao {

	public void insertServer(Server server);

	public List<Server> getServerList();

	public boolean hasServer(Server server);
  
}
