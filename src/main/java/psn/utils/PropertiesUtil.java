package psn.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;



public class PropertiesUtil {

	private static Properties properties;
	private PropertiesUtil() {}
	private Properties getProperties() {
		if(properties == null)
			properties = new Properties();
		return properties;
	}
	public static String getProperty(String property) {
		return properties.getProperty(property);
	}
	public static ThreadLocal<StringBuffer> getMapperPackageName(final String property) {
		return new ThreadLocal<StringBuffer>() {
			@Override
			protected StringBuffer initialValue() {
				// 通过配置文件加载映射包名
				return new StringBuffer(PropertiesUtil.getProperty(property));
			}
		};
	}
	static {
		try {
			//加载配置文件
			properties = new Properties();
			//获取读取流 
			InputStream stream = null;
			//InputStream stream = StudentDaoImpl.class.getResourceAsStream("/mapperpkg.properties");
			if(stream!= null) {
				//从配置文件中读取数据
				properties.load(stream); 
				//关闭流
				stream.close();
				//获取映射
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
