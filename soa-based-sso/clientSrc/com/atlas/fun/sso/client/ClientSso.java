package com.atlas.fun.sso.client;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;

import com.apex.commons.utils.ApexCommonsException;
import com.apex.commons.utils.DomForXPath;
import com.atlas.fun.sso.client.config.Configuration;
import com.atlas.fun.util.EnDeClip;

public class ClientSso {
	private static final String endStr = "</root>";

	private String ssoServerHost = "";
	private String ssoServerPort = "";

	private static final String handshakeUrl = "/sso/handshake.apx";
	private static final String askTicketUrl = "/sso/askticket.apx";
	private static final String askUserUrl = "/sso/askuser.apx";

	private Key	key = null;
	public Key getKey(){return key;}

	public ClientSso(){
		try {
			init();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void init() throws Exception {
		FileInputStream in = new FileInputStream(getClass().getResource(String.format("/%s", Configuration.getStringValue("sec-key-file-name"))).getFile());
		ObjectInputStream ois = new ObjectInputStream(in);
		key = (Key) ois.readObject();
		in.close();

		ssoServerHost = Configuration.getStringValue("sso-server-host");
		ssoServerPort = Configuration.getStringValue("sso-server-port");
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
			return EnDeClip.encrypt(data, getKey());
		}catch(Exception e){
			e.printStackTrace();
			return data;
		}
	}

	private String decrypt(String data){
		try{
			EnDeClip.setUp();
			return EnDeClip.decrypt(data, getKey());
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

	public String askTicket(String userId, String userInfo){
		SSOClient client = new SSOClient();
		if(handshake(client)){
			Map<String, String> params = new HashMap<String, String>();
			params.put("userid", encrypt(userId));
			params.put("userinfo", encrypt(userInfo));
			String xml = client.call(ssoServerHost, ssoServerPort, askTicketUrl, params);
			DomForXPath xpathResult;
			try {
				xpathResult = new DomForXPath(xml);
			} catch (ApexCommonsException e) {
				e.printStackTrace();
				return generateSuccessResult(false, "wrong xml format");
			}
			return generateSuccessResult(
					Boolean.valueOf(xpathResult.getText("//root/result").trim()),
					xpathResult.getText("//root/ticket"));
		}else
			return generateSuccessResult(false, "handshake error");
	}

	public String askUser(String ticket){
		SSOClient client = new SSOClient();
		if(handshake(client)){
			Map<String, String> params = new HashMap<String, String>();
			params.put("ticket", ticket);
			String xml = client.call(ssoServerHost, ssoServerPort, askUserUrl, params);
			DomForXPath xpathResult;
			try {
				xpathResult = new DomForXPath(xml);
			} catch (ApexCommonsException e) {
				e.printStackTrace();
				return generateSuccessResult(false, "wrong xml format");
			}
			return generateSuccessResult(
					Boolean.valueOf(xpathResult.getText("//root/result").trim()),
					decrypt(xpathResult.getText("//root/userinfo")));
		}else
			return generateSuccessResult(false, "handshake error");
	}

	public static void main(String[] atgs){
		ClientSso cso = new ClientSso();
		System.out.println("ssoServerHost="+cso.ssoServerHost+"\nssoServerPort="+cso.ssoServerPort);
	}

}
