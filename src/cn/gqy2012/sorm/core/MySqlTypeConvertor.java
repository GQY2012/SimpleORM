package cn.gqy2012.sorm.core;
/**
 * mysql数据类型和Java数据类型的互相转换
 * @author gqy2012
 *
 */
public class MySqlTypeConvertor implements TypeConvertor{

	@Override
	public String databaseType2JavaType(String databaseType) {
		switch (databaseType.toLowerCase()) {
			case "varchar":return "String";
			case "int":
			case "tinyint":
			case "integer":
			case "smallint":return "Integer";
			case "bigint":return "Long";
			case "double":
			case "float":return "Double";
			case "text":return "java.sql.Clob";
			case "blob":return "java.sql.Blob";
			case "date":return "java.sql.Date";
			case "time":return "java.sql.Time";
			case "timestamp":return "java.sql.Timestamp";
			default:return null;
		}
	}

	@Override
	public String JavaType2databaseType(String javaType) {
		// TODO 自动生成的方法存根
		return null;
	}

}
