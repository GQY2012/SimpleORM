package cn.gqy2012.sorm.utils;
/**
 * 封装生成Java文件(源代码)操作
 * @author gqy2012
 *
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.gqy2012.sorm.bean.ColumnInfo;
import cn.gqy2012.sorm.bean.JavaFieldGetSet;
import cn.gqy2012.sorm.bean.TableInfo;
import cn.gqy2012.sorm.core.DBManager;
import cn.gqy2012.sorm.core.MySqlTypeConvertor;
import cn.gqy2012.sorm.core.TableContext;
import cn.gqy2012.sorm.core.TypeConvertor;

public class JavaFileUtils {
	/**
	 * 根据字段信息生成Java属性信息及相应的get、set方法。varchar username --> private String username,
	 * @param column 字段信息
	 * @param convertor 类型转化器
	 * @return java属性及get、set方法的源码
	 */
	private static JavaFieldGetSet createFieldGetSetSRC(ColumnInfo column,TypeConvertor convertor) {
		JavaFieldGetSet jfgs= new JavaFieldGetSet();
		//数据库类型转Java类型
		String javaFieldType = convertor.databaseType2JavaType(column.getDataType());
		//生成属性代码
		jfgs.setFieldInfo("\tprivate "+javaFieldType+" "+column.getName()+";\n");
		//生成set方法源码
		StringBuilder getSrc = new StringBuilder();
		getSrc.append("\tpublic "+javaFieldType
				+" get"+StringUtils.firstChar2UpperCase(column.getName())+"(){\n");
		getSrc.append("\t\treturn "+column.getName()+";\n");
		getSrc.append("\t}\n");
		jfgs.setGetInfo(getSrc.toString());
		//生成get方法源码
		StringBuilder setSrc = new StringBuilder();
		setSrc.append("\tpublic void set"+StringUtils.firstChar2UpperCase(column.getName())
				+"("+javaFieldType+" "+column.getName()+"){\n");
		setSrc.append("\t\tthis."+column.getName()+" = "+column.getName()+";\n");
		setSrc.append("\t}\n");
		jfgs.setSetInfo(setSrc.toString());
		
		return jfgs;
	}
	/**
	 * 生成JavaBean类源代码
	 * @param tableInfo
	 * @param convertor
	 * @return JavaBean的源代码
	 */
	public static String createJavaSrc(TableInfo tableInfo,TypeConvertor convertor) {
		StringBuilder src = new StringBuilder();
		Map<String,ColumnInfo> columns = tableInfo.getColumns();
		List<JavaFieldGetSet> javafields = new ArrayList<>();
		
		for(ColumnInfo c:columns.values()) {
			javafields.add(createFieldGetSetSRC(c, convertor));
		}
		//生成package
		src.append("package "+DBManager.getConf().getPoPackage()+";\n\n");
		//生成import
		src.append("import java.sql.*;\n");
		src.append("import java.util.*;\n\n");
		//生成类声明语句
		src.append("public class "+StringUtils.firstChar2UpperCase(tableInfo.getTname())+"{\n\n");
		//生成属性列表
		for(JavaFieldGetSet f:javafields) {
			src.append(f.getFieldInfo());
		}
		//生成set方法列表
		for(JavaFieldGetSet f:javafields) {
			src.append(f.getSetInfo());
		}
		//生成get列表
		for(JavaFieldGetSet f:javafields) {
			src.append(f.getGetInfo());
		}
		//类结束
		src.append("}\n");
		return src.toString();
	}
	/**
	 * 生成JavaBean类的.java源文件
	 * @param tableInfo
	 * @param convertor
	 */
	public static void createJavaPOFile(TableInfo tableInfo,TypeConvertor convertor) {
		String src = createJavaSrc(tableInfo,new MySqlTypeConvertor());
		String srcPath = DBManager.getConf().getSrcPath()+"/";
		String packagePath = DBManager.getConf().getPoPackage().replaceAll("\\.", "/");
		File f = new File(srcPath+packagePath);
		if(!f.exists()) {
			f.mkdirs();
		}
		
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(
					new FileWriter(f+"/"+StringUtils.firstChar2UpperCase(tableInfo.getTname())+".java"));
			bw.write(src);
			System.out.println("建立表"+tableInfo.getTname()+"对应的JavaBean类："
					+StringUtils.firstChar2UpperCase(tableInfo.getTname())+".java");
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	public static void main(String[] args) {
		/*ColumnInfo ci = new ColumnInfo("username","int",0);
		JavaFieldGetSet jfgs = createFieldGetSetSRC(ci,new MySqlTypeConvertor());
		System.out.println(jfgs.toString());*/
				
		Map<String,TableInfo> map = TableContext.tables;
		for(TableInfo t:map.values()) {
			createJavaPOFile(t,new MySqlTypeConvertor());
		}
		
	}
}
