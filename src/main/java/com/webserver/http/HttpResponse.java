package com.webserver.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * send response: status line, header, context
 * @author zxh
 *
 */
public class HttpResponse {
	// member for response header
	private Map<String, String> headers=new HashMap<String, String>();
	private int statusCode=200;
	private String stateStr="OK";
	private File entity;
	
	private Socket socket;
	private OutputStream os;
	public HttpResponse(Socket socket) {
		try {
			this.socket=socket;
			this.os=socket.getOutputStream();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public void flush() {
		try {
			sendStatusline();
			sendHeader();
			sendContext();			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void sendStatusline() {
		try {
			String line="Http/1.1"+" "+statusCode+" "+stateStr;
			println(line);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void sendHeader() {
		try {
			// send header
			Set<Entry<String, String>> set=headers.entrySet();
			for (Entry<String, String> header : set) {
				String line=header.getKey()+": "+header.getValue();
				println(line);
			}
			
			// finish header with CRLF
			println("");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void sendContext() {
		try(
			// send context, index.html	
			FileInputStream fis=new FileInputStream(entity);		
			) {				
			byte[] data=new byte[1024*10];
			int len=-1;
			while((len=fis.read(data))!=-1) {
				os.write(data, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	public void setEntity(File entity) {
		this.entity = entity;
		// set headers to configure content: Context-File, Content-Length 
		this.headers.put("Content-Length", String.valueOf(entity.length()));
		
		// get file extension and put content type
		String[] fileName=entity.getName().split("\\.");
		String ext=fileName[fileName.length-1];
		String type=HttpContext.getContentType(ext);
		this.headers.put("Content-Type", type);
	}

	public int getStatusCode() {
		return statusCode;
	}
	
	// set statusCode
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
		this.stateStr=HttpContext.getStatusReason(statusCode);
	}

	public String getStateStr() {
		return stateStr;
	}

	public void setStateStr(String stateStr) {
		this.stateStr = stateStr;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}
	
	// add header name and value
	public void putHeader(String name, String val) {
		this.headers.put(name, val);
	}
	
	// send a line, ends with CRLF
	private void println(String line) {
		try {
			os.write(line.getBytes("ISO8859-1"));
			os.write(HttpContext.CR);// CR
			os.write(HttpContext.LF);//LF
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
