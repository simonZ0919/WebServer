package com.webserver.core;
/**
 * main class of WebServer
 * @author zxh
 *
 */

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebServer {
	private ServerSocket server;
	private ExecutorService threadPool;
	/**
	 * construct WebServer
	 */
	public WebServer() {
		try {
			server=new ServerSocket(8088);
			threadPool=Executors.newFixedThreadPool(50);// pool size 50
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void start() {
		try {
			while (true) {
				Socket socket=server.accept();

				// run thread pool
				threadPool.execute(new ClientHandler(socket));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		WebServer server=new WebServer();
		server.start();
	}
}
