package cn.gqy2012.sorm.utils;

import java.sql.PreparedStatement;
import java.sql.SQLException;


/**
 * JDBC查询常用操作
 * @author gqy2012
 *
 */
public class JDBCUtils {
	/**
	 * 给sql设参
	 * @param ps
	 * @param params
	 */
	public static void handleParams(PreparedStatement ps,Object[] params) {
		if(params!=null) {
			for(int i = 0;i<params.length;i++) {
				try {
					ps.setObject(i+1, params[i]);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
