package cn.gqy2012.sorm.core;
/**
 * Query对象的工厂类
 * @author gqy2012
 *
 */
public class QueryFactory {
	//public Query createQuery();

	private static QueryFactory factory = new QueryFactory();
	private static Query prototype;//原型对象
	
	static {
		try {
			Class c = Class.forName(DBManager.getConf().getQueryClass());
			prototype = (Query) c.newInstance();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	private QueryFactory() {
		
	}
	/**
	 * 返回Query对象
	 * @return
	 */
	public static Query createQuery() {
		/*try {
		 	//使用反射，效率比较低
			return (Query) c.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}*/
		try {
			//使用原型模式，提高效率
			return (Query) prototype.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}
}
