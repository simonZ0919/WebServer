package com.webserver.core;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ServerContext {
	private static Map<String, String> servletMap=
			new HashMap<String, String>();
	static {
		initialMap();
	}
	
	private static void initialMap() {
//		servletMap.put("/myweb/reg", "com.webserver.servlets.RegServlet");
//		servletMap.put("/myweb/login", "com.webserver.servlets.LoginServlet");
//		servletMap.put("/myweb/update", "com.webserver.servlets.UpdateServlet");
		// read serlvet and classname from xml 
		try {
			SAXReader reader=new SAXReader();
			Document doc=reader.read(new File("conf/servlets.xml"));
			List<Element> list=doc.getRootElement().elements();
			for (Element element : list) {
				String url=element.attributeValue("url");
				String className=element.attributeValue("className");
				servletMap.put(url, className);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static String getServletName(String url) {
		return servletMap.get(url);
	}
	
}
