package com.ck.stage1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @title 直接使用jdbc访问数据库
 * @author Chengkai
 * @date 2018年7月4日
 * @time 下午1:29:20
 * @describe
 */
public class JDBCOnly {
	public void getUser() {
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");																					//数据库驱动类加载时，driver就被注册到了DriverManager
			conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/my_test", "root", "mysql");
			stmt = conn.createStatement();
			String sql = "select * from users where name='tiger'";
			ResultSet result = stmt.executeQuery(sql);
			while(result.next()) {
				System.out.println(result.getString("name"));
				System.out.println(result.getInt("age"));
				System.out.println(result.getString("remark"));
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(stmt!=null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(conn!=null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String[] args) {
		JDBCOnly jo = new JDBCOnly();
		jo.getUser();
	}
}
