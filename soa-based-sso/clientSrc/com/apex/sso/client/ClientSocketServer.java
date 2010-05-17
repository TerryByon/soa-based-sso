package com.apex.sso.client;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.security.Key;

import com.apex.sso.client.config.Configuration;

public class ClientSocketServer implements Runnable {
	private static final int serverPort = 8030;
	
	public static void main(String[] agrs) throws Exception {
		ClientSocketServer ss = new ClientSocketServer();
		ss.init();
		new Thread(ss).start();
	}
	
	private boolean isStop = false;
	private Key	key = null;
	public Key getKey(){return key;}
	
	private void init() throws Exception {
		FileInputStream in = new FileInputStream(getClass().getResource(String.format("/%s", Configuration.getStringValue("sec-key-file-name"))).getFile());
		ObjectInputStream ois = new ObjectInputStream(in);
		key = (Key) ois.readObject();
		in.close();
	}
	
	@Override
	public void run() {
		System.out.println("Socket server started!!!");
		ServerSocket ss = null;
		try{
			ss = new ServerSocket(serverPort);
			ss.setSoTimeout(1000);
			Socket s = null;
			while(!isStop){
				try{
					if((s = ss.accept())!=null){
						new ClientController(s, this);
					}
				}catch(SocketTimeoutException ste){}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void stop(){
		this.isStop = true;
	}
}
