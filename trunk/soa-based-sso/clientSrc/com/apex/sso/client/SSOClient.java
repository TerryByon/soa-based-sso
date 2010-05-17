package com.apex.sso.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

public class SSOClient {
	private HttpClient m_client = new HttpClient(new MultiThreadedHttpConnectionManager());;
	private boolean isInitClient = false;

	private HttpClient getHttpClient() {
		synchronized(this.m_client) {
			if (!isInitClient) {
				this.m_client.getParams().setParameter("http.useragent",
						"Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; .NET CLR 1.1.4322; .NET CLR 2.0.50727;" +
						" .NET CLR 3.0.04506.30; .NET CLR 3.0.04506.648)");
				this.m_client.getHttpConnectionManager().getParams().setMaxTotalConnections(100000);
				this.m_client.getHttpConnectionManager().getParams().setSoTimeout(0);
				this.m_client.getHttpConnectionManager().getParams().setConnectionTimeout(0);
				this.m_client.getHttpConnectionManager().getParams().setReceiveBufferSize(1024*1000);

				isInitClient = true;
			}
		}
		return this.m_client;
	}

	public String call(String host, String port, String url, Map<String, String> parameters){

		url = String.format("%s:%s%s", host, port, url);

		HttpMethod method = null;
		// POST
		if (parameters != null && !parameters.isEmpty()) {
			method = new PostMethod(url);
			NameValuePair[] params = new NameValuePair[parameters.size()];
			int idx = 0;
			for (String key : parameters.keySet()) params[idx++] = new NameValuePair(key, parameters.get(key));

			((PostMethod) method).setRequestBody(params);
		}
		// GET
		else method = new GetMethod(url);

		InputStream is = null;
		BufferedReader br = null;

		try {
			int statusCode = getHttpClient().executeMethod(method);
			if (statusCode != HttpStatus.SC_OK) {
				throw new HttpException("접속에 실패 했습니다[STATUS_CODE: " + statusCode + ", STATUS_LINE: " + method.getStatusLine() + ", URL: " + method.getURI() +"].");
			}

			if(method instanceof GetMethod) {
				br = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream(), ((GetMethod)method).getResponseCharSet()));

				String str = null;
				StringBuffer sb = new StringBuffer();
				while ((str = br.readLine()) != null) sb.append(str).append("\n");

				return sb.toString();
			} else {
				return new String(method.getResponseBody());
			}
		} catch (HttpException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (br != null) br.close();
				if(is != null) is.close();
			} catch (IOException e) {}
			if (method != null) method.releaseConnection();
		}
	}

}
