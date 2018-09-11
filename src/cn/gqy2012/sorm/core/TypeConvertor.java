package cn.gqy2012.sorm.core;
/**
 * 负责java数据类型和数据库类型的互相转换
 * @author gqy2012
 *
 */
public interface TypeConvertor {
	/**
	 * 将数据库数据类型转化为Java数据类型
	 * @param databaseType 数据库数据类型
	 * @return Java数据类型
	 */
	public String databaseType2JavaType(String databaseType);
	/**
	 * 将Java数据类型转化为数据库数据类型
	 * @param javaType Java数据类型
	 * @return 数据库数据类型
	 */
	public String JavaType2databaseType(String javaType);
}
