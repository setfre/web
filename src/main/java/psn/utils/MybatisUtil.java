package psn.utils;

import java.io.IOException;
import java.io.Reader;


import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class MybatisUtil {

	private static ThreadLocal<SqlSession> threadLocal = new ThreadLocal<SqlSession>();
	private static SqlSessionFactory sqlSessionFactory;
	static {
		try {
			//加载mybatis配置文件
			Reader reader = Resources.getResourceAsReader("mybatis.xml");
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	private MybatisUtil() {}
	/**
	 * 获得session
	 * @return
	 */
	public static SqlSession getSqlSession() {
		SqlSession sqlSession = threadLocal.get();
		if(sqlSession == null) {
			sqlSession = sqlSessionFactory.openSession();
			threadLocal.set(sqlSession);
		}
		return sqlSession;
	}
	public static void commit() {
		SqlSession sqlSession = threadLocal.get();
		if(sqlSession != null) {
			sqlSession.commit();
		}
	}
	/**
	 * 关闭session
	 */
	public static void closeSqlSession() {
		SqlSession sqlSession = threadLocal.get();
		if(sqlSession!=null) {
			sqlSession.close();
			threadLocal.remove();
		}
	}
}
