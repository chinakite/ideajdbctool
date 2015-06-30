/**
 * 
 */
package com.ideamoment.ideajdbc4eclipse.state;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;

import com.ideamoment.ideajdbc4eclipse.database.Conn;

/**
 * @author Chinakite
 *
 */
public class StateUtils {
	
	public static File getStateStoreFile() {
		IPath storePath = Platform.getStateLocation(Platform.getBundle("ideajdbc4eclipse"));
		String storePathStr = storePath.makeAbsolute().toOSString();
		
		File storePathDir = new File(storePathStr);
		if(!storePathDir.exists()) {
			storePathDir.mkdir();
		}
		
		File storeFile = new File(storePathStr + File.separator + "conns.xml");
		if(!storeFile.exists()) {
			try {
				storeFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return storeFile;
	}
	
	public static List<Conn> getAllConns() {
		File connsFile = getStateStoreFile();
		
		SAXReader reader = new SAXReader();  
        Document doc;
        try {
			doc = reader.read(connsFile);
			Element root = doc.getRootElement();
			
			List<Element> eles = root.elements();
			List<Conn> conns = new ArrayList<Conn>();
			for(Element ele : eles) {
				Conn conn = new Conn();
				
				String name = ele.elementTextTrim("name");
				String url = ele.elementTextTrim("url");
				String userName = ele.elementTextTrim("userName");
				String password = ele.elementTextTrim("password");
				
				conn.setName(name);
				conn.setUrl(url);
				conn.setUserName(userName);
				conn.setPassword(password);
				
				conns.add(conn);
			}
			return conns;
		} catch (DocumentException e) {
			e.printStackTrace();
			return null;
		}
            
	}
}
