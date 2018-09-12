package cn.gqy2012.sorm.pool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cn.gqy2012.sorm.core.DBManager;

/**
 * 连接池的类
 * @author gqy2012
 *
 */
public class DBConnPool {
	/**
	 * 连接池对象
	 */
	private static List<Connection> pool;
	/**
	 * 最大连接数
	 */
	private static final int POOL_MAX_SIZE = 100;
	/**
	 * 最小连接数
	 */
	private static final int POOL_MIN_SIZE = 10;
	/**
	 * 初始化连接池中连接数量达到最小值
	 */
	public void initPool() {
		if(pool == null) {
			pool = new ArrayList<Connection>();
		}
		
		while(pool.size()<DBConnPool.POOL_MIN_SIZE) {
			pool.add(DBManager.createConn());
		}
	}
	/**
	 * 从连接池取出一个连接
	 * @return
	 */
	public synchronized Connection getConnection() {
		int last_index = pool.size()-1;
		Connection conn = pool.get(last_index);
		pool.remove(conn);
		return conn;
	}
	/**
	 * "关闭"连接：将连接放入连接池
	 * @param conn
	 */
	public synchronized void close(Connection conn) {
		if(pool.size() >= POOL_MAX_SIZE) {
			if(conn!=null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		pool.add(conn);
	}
	
	public DBConnPool() {
		initPool();
	}
}
