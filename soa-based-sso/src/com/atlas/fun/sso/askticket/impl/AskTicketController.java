package com.atlas.fun.sso.askticket.impl;

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


import com.atlas.fun.sso.askticket.IAskTicketController;
import com.atlas.fun.sso.askticket.IAskTicketManager;
import com.atlas.fun.sso.entity.Server;
import com.atlas.fun.sso.entity.User;
import com.atlas.fun.sso.handShake.IHandShakeManger;
import com.atlas.fun.sso.handShake.impl.HandShakeController;
import com.atlas.fun.util.EnDeClip;

@Controller
@RequestMapping("/askticket.apx")
public class AskTicketController implements IAskTicketController {
	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private IAskTicketManager manager;

	@Autowired
	private IHandShakeManger handManager;

	@RequestMapping
	public void getTicket(User user, HttpServletRequest request, HttpServletResponse response) {
		String isSession = (String) request.getSession().getAttribute("isSession");
		String serverId = (String) request.getSession().getAttribute("serverId");
		if (isSession == null || (isSession != null && !isSession.equals("isSession"))) {
			request.getSession().invalidate();
		}
		if (serverId == null) {
			request.getSession().invalidate();
		}
		response.setCharacterEncoding("UTF-8");
		String ip = request.getRemoteAddr();
		try {
			PrintWriter out = response.getWriter();
			out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			out.println("<root>");
			Server server = handManager.getServer(ip);
			if (server == null) {
				// 세션 끊김
				out.println("<result>flase</result>");
				request.getSession().invalidate();
				out.println("</root>");
				return;
			}
			try {
				ByteArrayInputStream bis = new ByteArrayInputStream(server.getSecurityKey());
				ObjectInputStream ois = new ObjectInputStream(bis);
				EnDeClip.setUp();
				Key key = (Key) ois.readObject();
				String userid = EnDeClip.decrypt(user.getUserid(), key);
				String userinfo = EnDeClip.decrypt(user.getUserinfo(), key);

				if (userid != null && userinfo != null) {
					user.setUserid(userid);
					user.setUserinfo(userinfo);
					manager.insertUser(serverId, user);
					out.println("<result>true</result>");
				} else {
					out.println("<result>false</result>");
				}
				String input = server.getId() + "//" + user.getId() + "//" + user.getUserid() + "//" + user.getValidtime();
				String ticket = EnDeClip.encrypt(input, HandShakeController.getServerKey());
				out.println(String.format("<ticket>%s</ticket>", ticket));
				out.println("</root>");
				// 세션 끊김
				request.getSession().invalidate();
			} catch (InvalidKeyException ie) {
				if (logger.isErrorEnabled()) logger.error(ie.getMessage(), ie);
				out.println("<result>flase</result>");
				request.getSession().invalidate();
				out.println("</root>");
			} catch (BadPaddingException bpe) {
				if (logger.isErrorEnabled()) logger.error(bpe.getMessage(), bpe);
				out.println("<result>flase</result>");
				request.getSession().invalidate();
				out.println("</root>");
			} catch (IllegalBlockSizeException ibse) {
				if (logger.isErrorEnabled()) logger.error(ibse.getMessage(), ibse);
				out.println("<result>flase</result>");
				request.getSession().invalidate();
				out.println("</root>");
			} catch (IOException e) {
				if (logger.isErrorEnabled()) logger.error(e.getMessage(), e);
				out.println("<result>flase</result>");
				request.getSession().invalidate();
				out.println("</root>");
			} catch (ClassNotFoundException e) {
				if(logger.isErrorEnabled()) logger.error(e.getMessage(), e);
				out.println("<result>flase</result>");
				request.getSession().invalidate();
				out.println("</root>");
			} catch (Exception ex) {
				if(logger.isErrorEnabled()) logger.error(ex.getMessage(), ex);
				out.println("<result>flase</result>");
				request.getSession().invalidate();
				out.println("</root>");
			}

			return;
		} catch (IOException e) {
			if (logger.isErrorEnabled()) logger.error(e.getMessage(), e);
		}
	}
}
