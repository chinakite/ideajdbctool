/**
 * 
 */
package com.ideamoment.ideajdbctool.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Chinakite
 *
 */
public class DataBaseUtils {
	public static Connection getConnection(String url, String userName, String password) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection(url, userName, password);
			return conn;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static boolean closeConnection(Connection conn) {
		try {
			conn.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			conn = null;
		}
		return false;
	}
	
	public static List<Tbl> getAllTables(Connection conn) {
		List<Tbl> tables = new ArrayList<Tbl>();
		try {
			ResultSet rs = conn.getMetaData().getTables(null, "%", "%", new String[]{"TABLE"});
			while(rs.next()) {
				Tbl tbl = new Tbl();
				tbl.setName(rs.getString("TABLE_NAME"));
				tables.add(tbl);
			}
			closeConnection(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tables;
	}
	
	public static List<Col> getAllCols(Conn connInfo, String tblName) {
		List<Col> cols = new ArrayList<Col>();
		Connection conn = getConnection(connInfo.getUrl(), connInfo.getUserName(), connInfo.getPassword());
		try {
			ResultSet rs = conn.getMetaData().getColumns(null, "%", tblName, "%");
			while(rs.next()) {
				Col col = new Col();
				col.setName(rs.getString("COLUMN_NAME"));
				col.setChecked(true);
				cols.add(col);
			}
			closeConnection(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cols;
	}
}	
