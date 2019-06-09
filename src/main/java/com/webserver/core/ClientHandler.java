package com.webserver.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;
import com.webserver.servlets.HttpServlet;
import com.webserver.servlets.LoginServlet;
import com.webserver.servlets.RegServlet;
import com.webserver.servlets.UpdateServlet;

public class ClientHandler implements Runnable {
	private Socket socket;
	public ClientHandler(Socket socket) {
		this.socket=socket;
	}
	public void run() {
		try {
			/**
			 * parse request
			 * handle request
			 * send response
			 */
			HttpRequest request=new HttpRequest(socket);
			HttpResponse response=new HttpResponse(socket);
			
			// get abstract path, and get resource in the file 
			String URI=request.getRequestURI();
			
			Map<String, String> map=new HashMap<String, String>();
			
			String servletName=ServerContext.getServletName(URI);
			
			if (servletName!=null) {
				/**
				 * reflect class name to Class object
				 * create servlet object
				 * call service()
				 */
				Class cls=Class.forName(servletName);
				HttpServlet servlet=(HttpServlet)cls.newInstance();
				servlet.service(request, response);
			}
			
			//handle request
			else {
				File file=new File("webapps"+URI);
				
				if(file.exists()) {			
					response.setEntity(file);
				}else {
					// set code 404
					response.setStatusCode(404);
					response.setEntity(new File("./webapps/root/404.html"));
				}		
			}	
			
			//send response
			response.flush();
			
		}catch (EmptyRequestException e) {
			// empty request 
			System.out.println("Empty Request");
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			// disconnect client 
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
