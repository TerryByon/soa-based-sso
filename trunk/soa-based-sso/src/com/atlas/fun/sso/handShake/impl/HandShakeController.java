package com.atlas.fun.sso.handShake.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.security.InvalidKeyException;
import java.security.Key;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import com.atlas.fun.sso.entity.Server;
import com.atlas.fun.sso.handShake.IHandShakeController;
import com.atlas.fun.sso.handShake.IHandShakeManger;
import com.atlas.fun.util.EnDeClip;

@Controller
@RequestMapping("/handshake.apx")
public class HandShakeController implements IHandShakeController {
	protected final Log logger = LogFactory.getLog(getClass());

	private static Key serverKey;

	public static Key getServerKey() {
		return serverKey;
	}

	static {
		try {
			serverKey = EnDeClip.makeKey();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Autowired
	private IHandShakeManger manager;

	@RequestMapping
	public void handShake(@RequestParam("serverinfo") String serverInfo, HttpServletRequest request, HttpServletResponse response) {
		response.setCharacterEncoding("UTF-8");
		String ip = request.getRemoteAddr();
		try {
			PrintWriter out = response.getWriter();
			Server server = manager.getServer(ip);
			out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			out.println("<root>");
			if (server == null) {
				// 세션 끊김
				out.println("<result>false</result>");
				request.getSession().invalidate();
				out.println("</root>");
				return;
			}
			ByteArrayInputStream bis = new ByteArrayInputStream(server.getSecurityKey());
			ObjectInputStream ois = new ObjectInputStream(bis);
			Key key = (Key) ois.readObject();
			EnDeClip.setUp();

			String id = EnDeClip.decrypt(serverInfo, key);

			if (id != null && id.equals(server.getId() + "")) {
				// 세션 연결
				out.println("<result>true</result>");
				request.getSession().setAttribute("isSession", "isSession");
				request.getSession().setAttribute("serverId", id);
			} else {
				// 세션 끊김
				out.println("<result>false</result>");
				request.getSession().invalidate();
			}
			out.println("</root>");
			return;
		} catch (InvalidKeyException ie) {
			if (logger.isErrorEnabled()) logger.error(ie.getMessage(), ie);
		} catch (BadPaddingException bpe) {
			if (logger.isErrorEnabled()) logger.error(bpe.getMessage(), bpe);
		} catch (IllegalBlockSizeException ibse) {
			if (logger.isErrorEnabled()) logger.error(ibse.getMessage(), ibse);
		} catch (IOException e) {
			if (logger.isErrorEnabled()) logger.error(e.getMessage(), e);
		} catch (ClassNotFoundException e) {
			if(logger.isErrorEnabled()) logger.error(e.getMessage(), e);
		} catch (Exception e) {
			if(logger.isErrorEnabled()) logger.error(e.getMessage(), e);
		}
	}
}