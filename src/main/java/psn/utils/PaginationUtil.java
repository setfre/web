package psn.utils;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import psn.mapper.BaseMapper;
import psn.mapper.UserMapper;
import psn.pojo.Pagination;
import psn.service.BaseService;


/**
 * Spring在代码中获取bean的几种方式
 * https://www.cnblogs.com/yjbjingcha/p/6752265.html
 */
@Service
public class PaginationUtil extends BaseService implements ApplicationContextAware{

	@Autowired
	private ApplicationContext applicationContext;
	
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// TODO Auto-generated method stub
		this.applicationContext = applicationContext; 
	}
	@PostConstruct
	public void init() {
		
	} 
	
	public<T> Pagination<T> getPagination(Integer currentIndex,Integer recordShowSize,
			Class<T> beanClass){
		
		String beanName = beanClass.getName();
		
		if(beanName == null) {
			return null;
		}
		
		System.out.println("我被调用啦"+beanName);
		
		BaseMapper baseMapper = null;
		
		if(beanName.indexOf("SysUser")>-1) {
			baseMapper = userMapper;
		}

		Pagination<T> pagination = new Pagination<T>();
		//设置总记录数
		pagination.setDbRecordSize(baseMapper.retrieveSize());
		//设置页面显示数
		pagination.setRecordShowSize(recordShowSize);
		//设置当前下表
		pagination.setCurrentIndex(currentIndex); 
		//存储记录
		pagination.setBean(baseMapper.pagination(pagination.getRange()));
		
		return pagination;
	}

}
