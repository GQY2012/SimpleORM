package cn.gqy2012.sorm.core;
/**
 * 根据配置信息，维持连接对象的管理(连接池)
 * @author gqy2012
 *
 */

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import cn.gqy2012.sorm.bean.Configuration;
import cn.gqy2012.sorm.pool.DBConnPool;

public class DBManager {
	private static Configuration conf;
	private static DBConnPool pool;
	
	static {
		Properties pros = new Properties();
		try {
			pros.load(Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("db.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		conf = new Configuration();
		conf.setDriver(pros.getProperty("mysqlDriver"));
		conf.setPoPackage(pros.getProperty("poPackage"));
		conf.setPwd(pros.getProperty("mysqlPwd"));
		conf.setSrcPath(pros.getProperty("srcPath"));
		conf.setUrl(pros.getProperty("mysqlURL"));
		conf.setUser(pros.getProperty("mysqlUser"));
		conf.setUsingDB(pros.getProperty("usingDB"));
		conf.setQueryClass(pros.getProperty("queryClass"));
	}
	public static Connection createConn() {
		try {
			Class.forName(conf.getDriver());
			return DriverManager.getConnection(conf.getUrl(), conf.getUser(), conf.getPwd());
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 获得连接对象
	 * @return
	 */
	public static Connection getConn() {
		if(pool == null) {
			 pool = new DBConnPool();
		}
		return pool.getConnection();
	}
	/**
	 * 关闭ResultSet,Statement,Connection
	 * @param rs
	 * @param ps
	 * @param conn
	 */
	public static void close(ResultSet rs,Statement ps,Connection conn) {
		 if(rs!=null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(ps!=null) {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(conn!=null) {
			//放回连接池
			pool.close(conn);
		}
	 }

	public static Configuration getConf() {
		return conf;
	}

	public static void setConf(Configuration conf) {
		DBManager.conf = conf;
	}
	
	
}
