package psn.service;

import java.util.List;

import psn.execption.InLoginException;
import psn.pojo.ActiveUser;
import psn.pojo.Pagination;
import psn.pojo.SysPermission;
import psn.pojo.SysUser;

public interface SysService {

	//用户身份认证
	public ActiveUser authenticat(String usercode,String password) throws InLoginException;
	
	//注册用户
	public void register(SysUser user) throws InLoginException;
	
	//根据账号查询用户
	public SysUser findSysuserByUsercode(String usercode);
	
	//根据用户id获取权限
	public List<SysPermission> findSysPermissionList(String userid);
	
	//根据用户id获取菜单
	public List<SysPermission> findMenuList(String userid);

	//获取用户信息分页
	public Pagination<SysUser> retrieveUserInfoPagination(Integer currentIndex, Integer recordShowSize);

}
