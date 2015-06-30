/**
 * 
 */
package com.ideamoment.ideajdbc4eclipse.database;

/**
 * @author Chinakite
 *
 */
public class Conn {
	private String url;
	private String name;
	private String userName;
	private String password;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getXmlStr() {
		StringBuffer sb = new StringBuffer();
		
		sb.append("<conn>");
		sb.append("<name>").append(this.name).append("</name>");
		sb.append("<url>").append(this.url).append("</url>");
		sb.append("<userName>").append(this.userName).append("</userName>");
		sb.append("<password>").append(this.password).append("</password>");
		sb.append("</conn>");
		
		return sb.toString();
	}
}
