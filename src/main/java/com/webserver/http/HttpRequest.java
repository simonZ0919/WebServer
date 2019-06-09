package com.webserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import com.webserver.core.EmptyRequestException;

/**
 * store objects of http request
 * request line, header, context
 * @author zxh
 *
 */
public class HttpRequest {
	
	private Socket socket;
	private InputStream is;	
	
	// request line
	private String method, url, protocol;

	// parameter and request of url
	private String requestURI, queryString;
	// map for input parameter
	private Map<String, String> parameters=new HashMap<String, String>();

	// map for header
	private Map<String, String> headers=new HashMap<String, String>();
	
	public HttpRequest(Socket socket) throws EmptyRequestException {
		try {
			this.socket=socket;
			this.is=socket.getInputStream();
			/**
			 * parse requestline, header and context
			 */
			parseRequestLine();
			parseHeaders();
			parseContext();
		} catch (EmptyRequestException e) {
			throw e;
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void parseRequestLine() throws EmptyRequestException {
		try {
			String line=readLine();
			String[] data=line.split("\\s");
			if (data.length!=3) {
				throw new EmptyRequestException();
			}
			method=data[0];
			url=data[1];
			parseURL();// further parse URL
			protocol=data[2];
			System.out.println(method);
			System.out.println(url);
			System.out.println(protocol);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * separate path and input parameter by ?, store in requestURI, quereyString
	 * extract name and value from quereyString and put into map
	 * notes: url can has no parameter, url?
	 * parameter has no value, name=&...
	 */
	private void parseURL() {
		String[] urlData=url.split("\\?");
		requestURI=urlData[0];
		if(urlData.length==1) 	
			return;
		
		//parse parameters
		queryString=urlData[1];	
		parseParameter(queryString);
	}

	private void parseHeaders() {
		try {
			/**
			 * keep reading header until readLine() return empty string, two CRLF
			 */
			String line=null;
			while(!"".equals(line=readLine())) {
				String[] data=line.split(": ");
				headers.put(data[0], data[1]);				
			}
			System.out.println(headers);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * get context length and type from header
	 * read context data
	 */
	private void parseContext() {
		
		try {
			if(headers.containsKey("Content-Length")) {
				int length=Integer.parseInt(headers.get("Content-Length"));
				byte[] data=new byte[length];
				is.read(data);
				
				// get string for form type
				String contenType=headers.get("Content-Type");
				if("application/x-www-form-urlencoded".equals(contenType)) {
					String line=new String(data,"ISO8859-1");
					parseParameter(line);
				}	
			}
			
			
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void  parseParameter(String line) {
		// decode hex to language
		try {
			line=URLDecoder.decode(line,"UTF-8");
		} catch (Exception e) {
			// TODO: handle exception
		}
		String[] queryData=line.split("\\&");
		String[] entry;
		for (String para : queryData) {
			entry=para.split("=");
			parameters.put(entry[0], (entry.length>1)?entry[1]:"");
		}
	}
	
	/**
	 * read one line of input, until CRLF, ASCII: 13,10
	 * @return
	 * @throws IOException
	 */
	private String readLine() throws IOException{
		StringBuilder builder=new StringBuilder();
		int d=-1;
		// c1: last byte, c2: current byte
		char c1='a',c2='a';
		while((d=is.read())!=-1) {
			c2=(char)d;
			if(c1==HttpContext.CR && c2==HttpContext.LF)
				break;
			builder.append(c2);
			c1=c2;
		}
		return builder.toString().trim();
	}

	public String getMethod() {
		return method;
	}

	public String getUrl() {
		return url;
	}

	public String getProtocol() {
		return protocol;
	}
	
	public String getHeader(String name) {
		return headers.get(name);
	}

	public String getRequestURI() {
		return requestURI;
	}

	public String getQueryString() {
		return queryString;
	}
	
	public String getParameter(String name) {
		return parameters.get(name);
	}
	
}
