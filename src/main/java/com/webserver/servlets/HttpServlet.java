package com.webserver.servlets;

import java.io.File;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

/**
 * superclass of all servlets
 * @author simon
 *
 */
public abstract class HttpServlet {
	public abstract void service(HttpRequest request, 
			HttpResponse response);
	/**
	 * jump to the target location, dispatcher
	 * @param path
	 * @param request
	 * @param response
	 */
	public void foward(String path, HttpRequest request,
			HttpResponse response) {
		response.setEntity(new File("webapps"+path));
	}
}
