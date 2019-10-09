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
				// ͨ�������ļ�����ӳ�����
				return new StringBuffer(PropertiesUtil.getProperty(property));
			}
		};
	}
	static {
		try {
			//���������ļ�
			properties = new Properties();
			//��ȡ��ȡ�� 
			InputStream stream = null;
			//InputStream stream = StudentDaoImpl.class.getResourceAsStream("/mapperpkg.properties");
			if(stream!= null) {
				//�������ļ��ж�ȡ����
				properties.load(stream); 
				//�ر���
				stream.close();
				//��ȡӳ��
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
