package psn.service;

import java.util.List;

import psn.execption.InLoginException;
import psn.pojo.ActiveUser;
import psn.pojo.Pagination;
import psn.pojo.SysPermission;
import psn.pojo.SysUser;

public interface SysService {

	//�û������֤
	public ActiveUser authenticat(String usercode,String password) throws InLoginException;
	
	//ע���û�
	public void register(SysUser user) throws InLoginException;
	
	//�����˺Ų�ѯ�û�
	public SysUser findSysuserByUsercode(String usercode);
	
	//�����û�id��ȡȨ��
	public List<SysPermission> findSysPermissionList(String userid);
	
	//�����û�id��ȡ�˵�
	public List<SysPermission> findMenuList(String userid);

	//��ȡ�û���Ϣ��ҳ
	public Pagination<SysUser> retrieveUserInfoPagination(Integer currentIndex, Integer recordShowSize);

}
