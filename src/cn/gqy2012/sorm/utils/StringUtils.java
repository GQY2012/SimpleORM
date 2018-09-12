package cn.gqy2012.sorm.utils;
/**
 * 字符串常用操作
 * @author gqy2012
 *
 */
public class StringUtils {
	/**
	 * 将字符串首字母变为大写
	 * @param str
	 * @return
	 */
	public static String firstChar2UpperCase(String str) {
		char ch = str.charAt(0);
		if(Character.isUpperCase(ch))
			return str;
		ch = (char)((int) ch - 32);
		return ch + str.substring(1);
	}
}
