package net.learnpark.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DBUtil {
	private static Properties prop;
	
	static{
		prop = new Properties();
		InputStream inStream = null;
		try {
			inStream = DBUtil.class.getClassLoader().getResourceAsStream("db.properties");
			prop.load(inStream);
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if(inStream != null){
				try {
					inStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public static void close(Connection conn,Statement st,ResultSet rs){
		if(conn != null){
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(st != null){
			try {
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(rs != null){
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public static Connection getConn(){
		String user = prop.getProperty("user");
		String password = prop.getProperty("password");
		String url = prop.getProperty("url");
		String driver = prop.getProperty("driver");
		try {
			Class.forName(driver);
			return DriverManager.getConnection(url,user,password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	

	public static void main(String[] args) {
//		String user = prop.getProperty("user");
//		System.out.println(user);
		Connection conn = getConn();
		if(conn != null){
			System.out.println("good");
		}
		try {
			DatabaseMetaData dmd = conn.getMetaData();
			String pn = dmd.getDatabaseProductName();
			System.out.println(pn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
