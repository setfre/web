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
 * Spring�ڴ����л�ȡbean�ļ��ַ�ʽ
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
		
		System.out.println("�ұ�������"+beanName);
		
		BaseMapper baseMapper = null;
		
		if(beanName.indexOf("SysUser")>-1) {
			baseMapper = userMapper;
		}

		Pagination<T> pagination = new Pagination<T>();
		//�����ܼ�¼��
		pagination.setDbRecordSize(baseMapper.retrieveSize());
		//����ҳ����ʾ��
		pagination.setRecordShowSize(recordShowSize);
		//���õ�ǰ�±�
		pagination.setCurrentIndex(currentIndex); 
		//�洢��¼
		pagination.setBean(baseMapper.pagination(pagination.getRange()));
		
		return pagination;
	}

}
