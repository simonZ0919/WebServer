package com.webserver.servlets;

import java.io.File;
import java.io.RandomAccessFile;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

/**
 * update password
 * @author simon
 *
 */
public class UpdateServlet extends HttpServlet{
	public void service(HttpRequest request, HttpResponse response) {
		String username=request.getParameter("username");
		String oldPasswd=request.getParameter("old_passwd");
		String newPasswd=request.getParameter("new_passwd");
		
		try (
			RandomAccessFile raf=new RandomAccessFile("user.dat", "rw");
		){
			String nameDb="", passwdDb="";
			Boolean isUpdate=false;	
			for (int i = 0; i < raf.length()/100; i++) {
				raf.seek(i*100);
				
				//read username
				byte[] data=new byte[32];
				raf.read(data);
				nameDb=new String(data,"UTF-8").trim();
				
				if (!nameDb.equals(username))
					continue;
				
				raf.read(data);
				passwdDb=new String(data,"UTF-8").trim();
				
				if(passwdDb.equals(oldPasswd)) {
					raf.seek(i*100+32);
					raf.write(newPasswd.getBytes("UTF-8"));
					isUpdate=true;
					break;
				}
			}
			
			if(isUpdate)
				foward("/myweb/update_succeed.html", request, response);
			else 
				foward("/myweb/update_fail.html", request, response);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
