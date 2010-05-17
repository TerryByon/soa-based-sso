package com.apex.sso.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

import util.EnDeClip;

import com.apex.commons.utils.DomForXPath;
import com.apex.sso.client.config.Configuration;

public class ClientController implements Runnable {
	private Socket socket;
	private ClientSocketServer server;
	private String ssoServerHost;
	private String ssoServerPort;
	public ClientController(Socket socket, ClientSocketServer server){
		this.socket = socket;
		this.server = server;
		ssoServerHost = Configuration.getStringValue("sso-server-hos");
		ssoServerPort = Configuration.getStringValue("sso-server-port");
		init();
		new Thread(this).start();
	}

	private void init(){
		try{
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream());

			socket.setSoTimeout(1000);

		}catch(Exception e){
			try{
				if(in!=null) in.close();
				if(out!=null) out.close();
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}

	private BufferedReader in;
	private PrintWriter out;

	private boolean isStop = false;

	private static final String endStr = "</root>";
	private static final String ASKTICKET = "askticket";
	private static final String ASKUSER = "askuser";

	private String handshakeUrl = "/sso/handshake.apx";
	private String askTicketUrl = "/sso/askticket.apx";
	private static final String askUserUrl = "/sso/askuser.apx";

	private void stop(){
		try{
			if(in!=null) in.close();
			if(out!=null) out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		isStop = true;
	}

	@Override
	public void run() {
		while(!isStop){
			try{
				String lineData;
				StringBuilder sb = new StringBuilder();
				while((lineData = in.readLine())!= null){
					sb.append(lineData);
					
					if(Boolean.parseBoolean(Configuration.getStringValue("debug-mode").trim())){					
						System.out.println(">" + lineData);
					}
					
					if(lineData.trim().endsWith(endStr)){
						out.println(process(sb.toString()));
						out.flush();

						stop(); //현재로서는 명령이 하나로 끝나기 때문.

					}
				}
			}catch(SocketTimeoutException ste){
			}catch(Exception e){
				stop();
			}
		}
	}

	private String generateSuccessResult(boolean result, String data){
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
		sb.append("<root>\n");
		sb.append(String.format("<result>%s</result>\n", result));
		sb.append(String.format("<data><![CDATA[%s]]></data>\n", data));
		sb.append(endStr);
		return sb.toString();
	}

	private String encrypt(String data){
		try{
			EnDeClip.setUp();
			return EnDeClip.encrypt(data, server.getKey());
		}catch(Exception e){
			e.printStackTrace();
			return data;
		}
	}

	private String decrypt(String data){
		try{
			EnDeClip.setUp();
			return EnDeClip.decrypt(data, server.getKey());
		}catch(Exception e){
			e.printStackTrace();
			return data;
		}
	}

	private boolean handshake(SSOClient client){
		Map<String, String> params = new HashMap<String, String>();
		params.put("serverinfo", encrypt(Configuration.getStringValue("my-id")));
		String xml = client.call(ssoServerHost, ssoServerPort, handshakeUrl, params);
		try{
			DomForXPath xpath = new DomForXPath(xml);
			return Boolean.valueOf(xpath.getText("//root/result").trim());
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	private String process(String command){
		if(command!=null&&command.length()>0){
			try{
				DomForXPath xpath = new DomForXPath(command);
				String commandStr = xpath.getText("//root/command").trim();

				SSOClient client = new SSOClient();

				if(ASKTICKET.equals(commandStr)){
					if(handshake(client)){
						Map<String, String> params = new HashMap<String, String>();
						params.put("userid", encrypt(xpath.getText("//root/id")));
						params.put("userinfo", encrypt(xpath.getText("//root/data")));
						String xml = client.call(ssoServerHost, ssoServerPort, askTicketUrl, params);
						DomForXPath xpathResult = new DomForXPath(xml);
						return generateSuccessResult(
								Boolean.valueOf(xpathResult.getText("//root/result").trim()),
								xpathResult.getText("//root/ticket"));
					}else
						return generateSuccessResult(false, "handshake error");
				}else if(ASKUSER.equals(commandStr)){
					if(handshake(client)){
						Map<String, String> params = new HashMap<String, String>();
						params.put("ticket", xpath.getText("//root/data"));
						String xml = client.call(ssoServerHost, ssoServerPort, askUserUrl, params);
						DomForXPath xpathResult = new DomForXPath(xml);
						return generateSuccessResult(
								Boolean.valueOf(xpathResult.getText("//root/result").trim()),
								decrypt(xpathResult.getText("//root/userinfo")));
					}else
						return generateSuccessResult(false, "handshake error");
				}else{
					return generateSuccessResult(false, "wrong command");
				}
			}catch(Exception e){
				e.printStackTrace();
				return generateSuccessResult(false, "wrong xml format");
			}
		}else{
			return generateSuccessResult(false, "command xml string is empty");
		}
	}

	public static void main(String[] args){
		Socket sc  = null;
		try{
			sc = new Socket("localhost", 8030);
			BufferedReader in = new BufferedReader(new InputStreamReader(sc.getInputStream()));
			PrintWriter out = new PrintWriter(sc.getOutputStream());

			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			String lineData;
			StringBuilder sb = new StringBuilder();

			boolean isStop = false;

			while(!isStop && (lineData = reader.readLine())!= null){
				sb.append(lineData);
				if(lineData.trim().endsWith(endStr)){
					out.println(sb.toString());
					out.flush();

					sb = new StringBuilder();
					while(!isStop && (lineData = in.readLine())!=null){
						sb.append(lineData);
						if(lineData.trim().endsWith(endStr)){
							System.out.println(sb.toString());
							isStop = true;
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
