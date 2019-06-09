package com.webserver.servlets;

import java.io.File;
import java.io.RandomAccessFile;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

public class LoginServlet extends HttpServlet {
// proceed login service
	public void service(HttpRequest request, HttpResponse response) {
		String username=request.getParameter("username");
		String password=request.getParameter("password");
		
		try (
			RandomAccessFile raf=new RandomAccessFile("user.dat", "r");
		){
			String nameDb="", passwdDb="";
			Boolean isReg=false;				
			for (int i = 0; i < raf.length()/100; i++) {
				raf.seek(i*100);
			
				//read username
				byte[] data=new byte[32];
				raf.read(data);
				nameDb=new String(data,"UTF-8").trim();
				
				if (!nameDb.equals(username))
					continue;
				
				//read password
				data=new byte[32];
				raf.read(data);
				passwdDb=new String(data,"UTF-8").trim();

				if(passwdDb.equals(password)) {
					isReg=true;
					break;
				}
			}
			
			if(isReg)
				foward("/myweb/login_success.html", request, response);
			else 
				foward("/myweb/login_fail.html", request, response);

		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
}
