package com.webserver.servlets;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Arrays;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

/**
 * register service
 * step: 1. get user information
 * 2. write info into user.dat
 * 3.response to client 
 * @author zxh
 *
 */
public class RegServlet extends HttpServlet {
	
	// proceed register service
	public void service(HttpRequest request, HttpResponse response) {
		String username=request.getParameter("username");
		String password=request.getParameter("password");
		String nickname=request.getParameter("nickname");
		Integer age=Integer.parseInt(request.getParameter("age"));
		
		try(
			RandomAccessFile raf=new RandomAccessFile("user.dat", "rw");	
		){
			// move pointer to the end of file, write username 			
			raf.seek(raf.length());
			byte[] data= username.getBytes("UTF-8");
			raf.write(Arrays.copyOf(data, 32));// write 32 byte
			
			//write password
			data=password.getBytes("UTF-8");
			raf.write(Arrays.copyOf(data, 32));
			
			//write nickname
			data=nickname.getBytes("UTF-8");
			raf.write(Arrays.copyOf(data, 32));
			
			//write age
			raf.writeInt(age);		
			
			foward("/myweb/reg_success.html", request, response);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
