package com.apex.sso.addserver.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.apex.sso.addserver.IAddServerController;
import com.apex.sso.addserver.IAddServerManager;
import com.apex.sso.entity.Server;

@Controller
@RequestMapping("/addserver.apx")
public class AddServerController implements IAddServerController{
	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private IAddServerManager manager;

	@RequestMapping(params = "call=addServer", method=RequestMethod.POST)
	public void addServer(Server server, HttpServletRequest request, HttpServletResponse response) throws Exception{
		response.setCharacterEncoding("UTF-8");
		try{
			PrintWriter out = response.getWriter();
			String login = (String)request.getSession().getAttribute("login");
			if(login==null){
				out.format(String.format("{success : false, message : '%s'}", "로그인하시오."));
				return;
			}
			boolean isInsert = manager.insertServer(server);
			if(isInsert)
				out.format(String.format("{success : true, message : '%s'}","저장되었습니다."));
			else
				out.format(String.format("{success : false, message : '%s'}", "오류가 발생하였습니다."));
			return;

		} catch(IOException e) {
			if(logger.isErrorEnabled()) logger.error(e.getMessage(), e);
		}
	}

	@RequestMapping(params = "call=addServerForm")
	public String addServerForm(Model model, HttpServletRequest request) throws Exception{
		String login = (String)request.getSession().getAttribute("login");
		if(login==null){
			request.getSession().invalidate();
			return "adminLogin";
		}
		model.addAttribute("serverList", manager.getServerList());
		return "addServerForm";
	}

	@RequestMapping(params = "call=login")
	public String login(@RequestParam(required=false, value="id")String id, @RequestParam(required=false, value="pw")String pw,HttpServletRequest request, Model model) throws Exception{
		String tmpId = "zeronine";
		String tmpPw = "0seven";
		if(StringUtils.isBlank(id)){
			model.addAttribute("msg", "아이디를 입력하세요");
			return "adminLogin";
		}

		if(!tmpId.equals(id)){
			model.addAttribute("msg", "아이디가 잘못되었습니다.");
			return "adminLogin";
		}
		if(StringUtils.isBlank(pw)){
			model.addAttribute("msg", "비밀번호를 입력하세요");
			return "adminLogin";
		}
		if(!tmpPw.equals(pw)){
			model.addAttribute("msg", " 비밀번호가 잘못되었습니다.");
			return "adminLogin";
		}

		request.getSession().setAttribute("login", "login");

		model.addAttribute("serverList", manager.getServerList());
		return "addServerForm";
	}

	@RequestMapping(params = "call=adminLogin")
	public String adminLogin() throws Exception{
		return "adminLogin";
	}

	@RequestMapping(params = "call=zipDownload")
	public void zipDownload(HttpServletResponse response, HttpServletRequest request) throws Exception{
		String encoding = request.getCharacterEncoding();
		if(encoding == null) encoding = "UTF-8";

		response.setContentType("text/html;charset="+ encoding);

		String filePath = "/home/deploy/sso/ssoClient.zip";
		File f = new File(filePath);
		String fileName = "ssoClient.zip";
		String strAgent = request.getHeader("user-agent");
		try {
			if (strAgent.indexOf("MSIE 5.5") != -1) {
				fileName = URLEncoder.encode(fileName, encoding);
				response.setHeader("Content-Type", "doesn/matter;");
				response.setHeader("Content-Disposition", "filename=" + fileName + ";");
			} else if (strAgent.indexOf("MSIE 6.0") != -1) {
				fileName = URLEncoder.encode(fileName, encoding);
				response.setHeader("Content-Type", "application/octet-stream;");
				response.setHeader("Content-Disposition", "attachment;filename="+ fileName + ";");
			} else if(strAgent.indexOf("Mozilla/5.0") > -1) {
				response.setHeader("Content-Type", "application/octet-stream;");
				response.setHeader("Content-Disposition", "attachment;filename="+ new String(fileName.getBytes(encoding), "ISO-8859-1") + ";");
			} else {
				fileName = URLEncoder.encode(fileName, encoding);
				response.setHeader("Content-Type", "application/octet-stream;");
				response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ";");
			}

			response.setHeader("Content-Transfer-Encoding", "7bit");
			response.setHeader("Content-Length", "" + f.length());
			response.setHeader("Pragma", "no-cache;");
			response.setHeader("Expires", "-1;");

			BufferedInputStream in = new BufferedInputStream(new FileInputStream(f));
			BufferedOutputStream fout = new BufferedOutputStream(response.getOutputStream());

			final byte[] buff = new byte[(1024*512)];
			int read = 0;

			while ((read = in.read(buff)) != -1) {
				fout.write(buff, 0, read);
			}

			fout.flush();
			fout.close();
			in.close();
		}catch(IOException e){
			logger.error(e.getMessage(), e);
		}
	}

	@RequestMapping(params = "call=keyDownload")
	public void keyDownload(@RequestParam("ip") String ip, HttpServletResponse response, HttpServletRequest request) throws Exception{
		String encoding = request.getCharacterEncoding();
		if(encoding == null) encoding = "UTF-8";

		response.setContentType("text/html;charset="+ encoding);

		String filePath = "/home/deploy/sso/secFile/"+ip+".sec";
		File f = new File(filePath);

		String fileName = ip+".sec";

		String strAgent = request.getHeader("user-agent");
		try {
			if (strAgent.indexOf("MSIE 5.5") != -1) {
				fileName = URLEncoder.encode(fileName, encoding);
				response.setHeader("Content-Type", "doesn/matter;");
				response.setHeader("Content-Disposition", "filename=" + fileName + ";");
			} else if (strAgent.indexOf("MSIE 6.0") != -1) {
				fileName = URLEncoder.encode(fileName, encoding);
				response.setHeader("Content-Type", "application/octet-stream;");
				response.setHeader("Content-Disposition", "attachment;filename="+ fileName + ";");
			} else if(strAgent.indexOf("Mozilla/5.0") > -1) {
				response.setHeader("Content-Type", "application/octet-stream;");
				response.setHeader("Content-Disposition", "attachment;filename="+ new String(fileName.getBytes(encoding), "ISO-8859-1") + ";");
			} else {
				fileName = URLEncoder.encode(fileName, encoding);
				response.setHeader("Content-Type", "application/octet-stream;");
				response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ";");
			}

			response.setHeader("Content-Transfer-Encoding", "7bit");
			response.setHeader("Content-Length", "" + f.length());
			response.setHeader("Pragma", "no-cache;");
			response.setHeader("Expires", "-1;");

			BufferedInputStream in = new BufferedInputStream(new FileInputStream(f));
			BufferedOutputStream fout = new BufferedOutputStream(response.getOutputStream());

			final byte[] buff = new byte[(1024*512)];
			int read = 0;

			while ((read = in.read(buff)) != -1) {
				fout.write(buff, 0, read);
			}

			fout.flush();
			fout.close();
			in.close();
		}catch(IOException e){
			logger.error(e.getMessage(), e);
		}
	}
}
