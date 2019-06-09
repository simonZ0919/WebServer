package com.webserver.http;
/**
 * define the context of Http
 * @author zxh
 *
 */

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class HttpContext {
	
	public static final int CR=13,LF=10;
	/**
	 * key: status code 
	 * value: status reason
	 */
	private static Map<Integer, String> 
	status_code_reason_mapping=new HashMap<Integer, String>();
	
	/**
	 * key: file extension
	 * value: response content type
	 */
	private static Map<String, String> mimeMap=new HashMap<String, String>();
	
	static {
		initStatusMapping();
		initMimeMap();
	}
	
	private static void initStatusMapping() {
		status_code_reason_mapping.put(200,"OK");
		status_code_reason_mapping.put(404,"Not Found");
		status_code_reason_mapping.put(502,"Bad Gateway");	
	}
	
	private static void initMimeMap() {
//		mimeMap.put("html", "text/html");
//		mimeMap.put("png", "image/png");
		/**
		 * parse <mime-maping> from conf/web.xml
		 * key: extension
		 * value: mime-type
		 */
		try {
			SAXReader reader=new SAXReader();
			Document document=reader.read(
					new FileInputStream("conf/web.xml"));
			List<Element> elements=
					document.getRootElement().elements("mime-mapping");
			for (Element e : elements) {
				mimeMap.put(e.elementText("extension"),
						e.elementText("mime-type"));
			}
							
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	public static String getStatusReason(int code) {
		return status_code_reason_mapping.get(code);
	}
	
	public static String getContentType(String ext) {
		return mimeMap.get(ext);
	}
	
//	public static void main(String[] args) {
//		System.out.println(mimeMap.get("jpg"));
//	}
}
