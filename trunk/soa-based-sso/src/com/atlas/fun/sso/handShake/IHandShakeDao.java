package com.atlas.fun.sso.handShake;

import com.atlas.fun.sso.entity.Server;

public interface IHandShakeDao {

	public Server getServer(String ip);

}
