package com.atlas.fun.sso.askuser.impl;

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


import com.apex.commons.utils.DateUtil;
import com.atlas.fun.sso.askuser.IAskUserController;
import com.atlas.fun.sso.askuser.IAskUserManager;
import com.atlas.fun.sso.entity.Server;
import com.atlas.fun.sso.entity.User;
import com.atlas.fun.sso.handShake.IHandShakeManger;
import com.atlas.fun.sso.handShake.impl.HandShakeController;
import com.atlas.fun.util.EnDeClip;

@Controller
@RequestMapping("/askuser.apx")
public class AskUserController implements IAskUserController {
	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private IAskUserManager manager;

	@Autowired
	private IHandShakeManger handManager;

	@RequestMapping
	public void getUser(@RequestParam("ticket") String ticket, HttpServletRequest request, HttpServletResponse response) {
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
				EnDeClip.setUp();
				String deTicket = EnDeClip.decrypt(ticket, HandShakeController.getServerKey());
				// String input = server.getId()+"//"+user.getId()+"//"+user.getUserid() +"//"+ user.getValidtime();
				String[] ticketInfo = deTicket.split("\\//");
				if (ticketInfo == null || (ticketInfo != null && ticketInfo.length != 4)) {
					// 세션 끊김
					out.println("<result>flase</result>");
					request.getSession().invalidate();
					out.println("</root>");
					return;
				}
				User user = manager.getUser(ticketInfo);
				if (user == null) {
					// manager.deleteUser(user);
					// 세션 끊김
					out.println("<result>flase</result>");
					request.getSession().invalidate();
					out.println("</root>");
					return;
				}
				int timeDef = DateUtil.getDateDiff(DateUtil.getNow("yyyyMMddHHmmssSSS"), ticketInfo[3], DateUtil.DATE_INTERVAL_SECOND,
						"yyyyMMddHHmmssSSS", false);
				if (timeDef > 0) {
					manager.deleteUser(user);
					// 세션 끊김
					out.println("<result>flase</result>");
					request.getSession().invalidate();
					out.println("</root>");
					return;
				}
				ByteArrayInputStream bis = new ByteArrayInputStream(server.getSecurityKey());
				ObjectInputStream ois = new ObjectInputStream(bis);
				Key key = (Key) ois.readObject();
				String userStr = "id:" + user.getUserid() + "//" + user.getUserinfo();
				String userinfo = EnDeClip.encrypt(userStr, key);

				if (userinfo != null) {
					manager.deleteUser(user);
					out.println("<result>true</result>");
				} else {
					out.println("<result>false</result>");
				}
				out.println(String.format("<userinfo>%s</userinfo>", userinfo));
				out.println("</root>");
				// 세션 끊김
				request.getSession().invalidate();
			} catch (InvalidKeyException ie) {
				if (logger.isErrorEnabled())
					logger.error(ie.getMessage(), ie);
				out.println("<result>flase</result>");
				request.getSession().invalidate();
				out.println("</root>");
			} catch (BadPaddingException bpe) {
				if (logger.isErrorEnabled())
					logger.error(bpe.getMessage(), bpe);
				out.println("<result>flase</result>");
				request.getSession().invalidate();
				out.println("</root>");
			} catch (IllegalBlockSizeException ibse) {
				if (logger.isErrorEnabled())
					logger.error(ibse.getMessage(), ibse);
				out.println("<result>flase</result>");
				request.getSession().invalidate();
				out.println("</root>");
			} catch (IOException e) {
				if (logger.isErrorEnabled())
					logger.error(e.getMessage(), e);
				out.println("<result>flase</result>");
				request.getSession().invalidate();
				out.println("</root>");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				out.println("<result>flase</result>");
				request.getSession().invalidate();
				out.println("</root>");
			} catch (Exception ex) {
				ex.printStackTrace();
				out.println("<result>flase</result>");
				request.getSession().invalidate();
				out.println("</root>");
			}
			return;
		} catch (IOException e) {
			if (logger.isErrorEnabled())
				logger.error(e.getMessage(), e);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public static void main(String[] arg) {
		String date = DateUtil.getNow("yyyyMMddHHmmssSSS");
		String date1 = DateUtil.dateAdd(0, 1, "yyyyMMddHHmmssSSS", "yyyyMMddHHmmssSSS");
		String date2 = DateUtil.dateAdd(1, 1, "yyyyMMddHHmmssSSS", "yyyyMMddHHmmssSSS");
		String date3 = DateUtil.dateAdd(2, 1, "yyyyMMddHHmmssSSS", "yyyyMMddHHmmssSSS");
		String date14 = DateUtil.dateAdd(13, 30, "yyyyMMddHHmmssSSS", "yyyyMMddHHmmssSSS");
		// String date15 = DateUtil.dateAdd(14, 1, "yyyyMMddHHmmssSSS", "yyyyMMddHHmmssSSS");
		// String date16 = DateUtil.dateAdd(15, 1, "yyyyMMddHHmmssSSS", "yyyyMMddHHmmssSSS");
		// String date17 = DateUtil.dateAdd(16, 1, "yyyyMMddHHmmssSSS", "yyyyMMddHHmmssSSS");
		String date8 = DateUtil.dateAdd(7, 1, "yyyyMMddHHmmssSSS", "yyyyMMddHHmmssSSS");
		String date9 = DateUtil.dateAdd(8, 1, "yyyyMMddHHmmssSSS", "yyyyMMddHHmmssSSS");
		String date10 = DateUtil.dateAdd(9, 1, "yyyyMMddHHmmssSSS", "yyyyMMddHHmmssSSS");
		String date11 = DateUtil.dateAdd(10, 1, "yyyyMMddHHmmssSSS", "yyyyMMddHHmmssSSS");
		String date12 = DateUtil.dateAdd(11, 1, "yyyyMMddHHmmssSSS", "yyyyMMddHHmmssSSS");
		String date13 = DateUtil.dateAdd(12, 1, "yyyyMMddHHmmssSSS", "yyyyMMddHHmmssSSS");

		int dateDef = DateUtil.getDateDiff(DateUtil.getNow("yyyyMMddHHmmssSSS"), DateUtil.dateAdd(13, 30, "yyyyMMddHHmmssSSS",
				"yyyyMMddHHmmssSSS"), 1000, "yyyyMMddHHmmssSSS");

		System.out.println("date = " + date);
		System.out.println("date = " + date14);
		System.out.println("dateDef = " + dateDef);
		System.out.println("date1 = " + date1);
		System.out.println("date2 = " + date2);
		System.out.println("date3 = " + date3);

		// System.out.println("date15 = "+date15);
		// System.out.println("date16 = "+date16);
		// System.out.println("date17 = "+date17);
		System.out.println("date8 = " + date8);
		System.out.println("date9 = " + date9);
		System.out.println("date10 = " + date10);
		System.out.println("date11 = " + date11);
		System.out.println("date12 = " + date12);
		System.out.println("date13 = " + date13);

	}
}