package cn.gqy2012.sorm.utils;
/**
 * 字符串常用操作
 * @author gqy2012
 *
 */
public class StringUtils {
	public static String firstChar2UpperCase(String str) {
		char c = str.charAt(0);
		c = (char)((int) c - 32);
		return c + str.substring(1);
	}
}
