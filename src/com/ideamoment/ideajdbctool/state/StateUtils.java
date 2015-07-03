/**
 * 
 */
package com.ideamoment.ideajdbctool.state;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;

import com.ideamoment.ideajdbctool.database.Conn;

/**
 * @author Chinakite
 * 
 */
public class StateUtils {

	public static File getStateStoreFile() {
		IPath storePath = Platform.getStateLocation(Platform
				.getBundle("ideajdbctool"));
		String storePathStr = storePath.makeAbsolute().toOSString();

		File storePathDir = new File(storePathStr);
		if (!storePathDir.exists()) {
			storePathDir.mkdir();
		}

		File storeFile = new File(storePathStr + File.separator + "conns.xml");
		if (!storeFile.exists()) {
			try {
				storeFile.createNewFile();
				
				OutputFormat format = OutputFormat.createPrettyPrint();
	            format.setEncoding("UTF-8");
				
				Document document = DocumentHelper.createDocument();
				Element root = document.addElement("conns");
				XMLWriter writer = new XMLWriter(new FileWriter(storeFile),
						format);
				writer.write(document);
				writer.close();
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
			for (Element ele : eles) {
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
