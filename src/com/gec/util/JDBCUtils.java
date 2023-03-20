package com.gec.util;

import java.io.InputStream;
import java.sql.*;
import java.util.Arrays;
import java.util.Properties;


public class JDBCUtils {
	private static Properties p = new Properties();

	//为了后面及daoImpl的变量使用
	public static Connection conn;
	public static PreparedStatement psmt;
	public static ResultSet rs;


	static {
		try(InputStream is = JDBCUtils.class.getClassLoader().getResourceAsStream("db.properties")){
			System.out.println("is:" + is);
			p.load(is);

			// System.out.println(p.getProperty("username") + ",password:" + p.getProperty("password"));

		} catch (Exception e) {

		}

		//注册驱动
		try{
			Class.forName(p.getProperty("driverClass"));
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	//提供连接
	public static Connection getConnection() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(p.getProperty("url"),p.getProperty("username"),p.getProperty("password"));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return conn;
	}

	//统一的查询
	public static ResultSet executeQuery(String sql ,Object...args){
		System.out.println("sql:" + sql + ",args:" + Arrays.toString(args));
		conn = getConnection();

		try {
			psmt = conn.prepareStatement(sql);

			//判断
			if(args !=null && args.length>0){
				//循环赋值给问号
				for(int i = 0;i < args.length;i++){
					psmt.setObject(i+1,args[i]);
				}
			}

			//执行查询
			rs = psmt.executeQuery();
			System.out.println("执行的sql:" + psmt);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return rs;
	}

	//统一dml(数据的添加，修改，删除）操作
	public static void excuteUpdate(String sql ,Object... args){
		conn = getConnection();
		try {
			psmt = conn.prepareStatement(sql);

			//判断
			if(args !=null && args.length>0){
				//循环赋值给问号
				for(int i = 0;i < args.length;i++){
					psmt.setObject(i+1,args[i]);
				}
			}

			//执行update
			psmt.executeUpdate();
			System.out.println("执行的sql:" + psmt);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	//释放连接
	public static void closeConn(Connection conn,PreparedStatement psmt ,ResultSet rs) {
		try {
			if(rs != null) rs.close();
			if(psmt != null) psmt.close();
			if(conn != null) conn.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Connection getConn() {
		return conn;
	}

	public static void setConn(Connection conn) {
		JDBCUtils.conn = conn;
	}

	public static PreparedStatement getPsmt() {
		return psmt;
	}

	public static void setPsmt(PreparedStatement psmt) {
		JDBCUtils.psmt = psmt;
	}

	public static ResultSet getRs() {
		return rs;
	}

	public static void setRs(ResultSet rs) {
		JDBCUtils.rs = rs;
	}

	public static void main(String[] args) {

	}
}



//	//释放连接
//	public static void closeConn(Connection conn) {
//		try {
//			if(conn != null) conn.close();
//		}catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
