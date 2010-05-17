package com.apex.sso.handShake;

import com.apex.sso.entity.Server;

public interface IHandShakeDao {

	public Server getServer(String ip);

}
