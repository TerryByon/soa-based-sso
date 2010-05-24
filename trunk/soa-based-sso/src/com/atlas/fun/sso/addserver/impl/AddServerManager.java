package com.atlas.fun.sso.addserver.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atlas.fun.sso.addserver.IAddServerDao;
import com.atlas.fun.sso.addserver.IAddServerManager;
import com.atlas.fun.sso.entity.Server;
import com.atlas.fun.util.EnDeClip;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.security.Key;
import java.util.List;


@Service
public class AddServerManager implements IAddServerManager {

	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private IAddServerDao dao;


	@Override
	public List<Server> getServerList() {
		return dao.getServerList();
	}


	@Override
	public boolean insertServer(Server server) {
		try{
			boolean hasServer = dao.hasServer(server);
			if(logger.isDebugEnabled()) logger.debug("hasServer = "+hasServer);
			if(!hasServer){
				String filePath = "/home/deploy/sso/secFile/"+server.getIp()+".sec";
				File checkFile = new File(filePath);
				if(checkFile.exists()) checkFile.delete();
				Key key = EnDeClip.makeKey();
				FileOutputStream out = new FileOutputStream(filePath);
				ObjectOutputStream oos = new ObjectOutputStream(out);
				oos.writeObject(key);
				oos.flush();
				oos.close();
				out.close();
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				oos = new ObjectOutputStream(bos);
				oos.writeObject(key);
				oos.flush();
				oos.close();
				server.setSecurityKey(bos.toByteArray());
				out.close();
				dao.insertServer(server);
				return true;
			}else return false;
		}catch(Exception e){
			if(logger.isErrorEnabled()) logger.error(e.getMessage(), e);
			return false;
		}
	}

}
