package psn.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




public class BeanUtil {

	/**
	 * 	根据对应bean的字段名字寻找map中对应键值对,将其映射到实体类上
	 * 
	 * 	不支持实现序列化的bean，无法映射serialVersionUID
	 * @param <T>
	 * @param map
	 * @param beanClass
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	
	public static<T> T mapToBean(Map<String, Object> map,Class<T> beanClass) throws InstantiationException, IllegalAccessException {
		
		T t = beanClass.newInstance();
		//Set<Map.Entry<String, Object>> entries = map.entrySet();
		Field[] fields = beanClass.getDeclaredFields();
		for(Field field:fields) {
			field.setAccessible(true); 
			field.set(t, map.get(field.getName()));
		}
		return t;
	}

	public static<T> Map<String, Object> beanToMap(Object bean) throws IllegalArgumentException, IllegalAccessException {
		Map<String, Object> map = new HashMap<String, Object>();
		Field[] fields = bean.getClass().getDeclaredFields();
		for(Field field:fields) {
			field.setAccessible(true);
			map.put(field.getName(), field.get(bean));
		}
		return map; 
	}
	
	
	public static<T> T getBean(String fieldName,Object fieldValue,Class<T> bean) throws InstantiationException, IllegalAccessException, NoSuchFieldException, SecurityException {
		T t = bean.newInstance();
		Field field = bean.getDeclaredField(fieldName);
		field.set(t,fieldValue);
		return t;
	}
	
	/**
	 * 	将bean封装为JsonObject类型返回
	 * @param bean
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	/*
	public static JSONObject beanToJsonObject(Object bean) throws IllegalArgumentException, IllegalAccessException {
		Map<String, Object> map = beanToMap(bean);
		JSONObject obj = new JSONObject();
		obj.putAll(map);
		return obj;
	}
	
	public static JSONArray beanToJsonArray(Object bean) throws IllegalArgumentException, IllegalAccessException {
		JSONObject obj = beanToJsonObject(bean);
		JSONArray array = new JSONArray();
		array.add(obj);
		return array;
	}
	public static JSONArray beanToJsonArray(List<Object> bean) {
		return null;
	}
	*/
}
