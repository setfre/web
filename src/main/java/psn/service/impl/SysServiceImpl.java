package psn.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import psn.execption.InLoginException;
import psn.pojo.ActiveUser;
import psn.pojo.Pagination;
import psn.pojo.SysPermission;
import psn.pojo.SysUser;
import psn.service.BaseService;
import psn.service.SysService;
import psn.utils.MD5Util;
import psn.utils.PaginationUtil;

@Service
public class SysServiceImpl extends BaseService implements SysService{

	public SysUser authenticat(String usercode, String password) throws InLoginException{
		// TODO Auto-generated method stub
		SysUser sysUser = userMapper.findSysuserByUsercode(usercode);
		
		//��֤�û�����
		if(sysUser == null){
			throw new InLoginException("�û�������");
		}
		 
		//��֤�û�����
		if (!sysUser.getPassword().equals(MD5Util.getEncryptedPaasword(password))) {
			throw new InLoginException("�������");
		}
		
		return sysUser;
	}

	private ActiveUser acceptAuthenticat(SysUser sysUser) {
		// TODO Auto-generated method stub
		ActiveUser activeUser = new ActiveUser();
		activeUser.setUuid(sysUser.getUuid());
		activeUser.setUsercode(sysUser.getUsercode());
		activeUser.setPassword(sysUser.getPassword());
		return activeUser;
	}

	public void register(SysUser user) throws InLoginException{
		
		//�û��Ѵ���
		if(isExists(user)){
			throw new InLoginException("�û��Ѵ���");
		}
		//�û���Ϣ��Ч
		if(!isValid(user)){
			throw new InLoginException("�û�ע�����Ч");
		}
		//����UUIDΨһ��ʶ��
		user.setUuid(UUID.randomUUID().toString());
		//MD5����
		user.setPassword(MD5Util.getEncryptedPaasword(user.getPassword()));
		//�������ݿ�
		userMapper.create(user);
	}
	public void updateUserHeaderImg(SysUser sysUser){
		userMapper.update(sysUser);
	}
	public boolean isExists(SysUser user){
		SysUser sysUser = userMapper.findSysuserByUsercode(user.getUsercode());
		if(sysUser == null){
			return false;
		}
		return true;
	}
	
	public boolean isValid(SysUser user){
		return true;
	}
	
	public SysUser findSysuserByUsercode(String usercode) {
		// TODO Auto-generated method stub
		return userMapper.findSysuserByUsercode(usercode);
	}
	
	public List<SysPermission> findSysPermissionList(String userid) {
		// TODO Auto-generated method stub
		return userMapper.findSysPermissionList(userid);
	}

	public List<SysPermission> findMenuList(String userid) {
		// TODO Auto-generated method stub
		return userMapper.findMenuList(userid);
	}

	public Pagination<SysUser> retrieveUserInfoPagination(Integer currentIndex, Integer recordShowSize) {
		// TODO Auto-generated method stub
		WebApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
		PaginationUtil ps = (PaginationUtil) applicationContext.getBean("paginationUtil");
		return ps.getPagination(currentIndex, recordShowSize, SysUser.class);
	}

}
