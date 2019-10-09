package psn.utils;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import psn.mapper.PermissionMapper;
import psn.mapper.RoleMapper;
import psn.pojo.Role;
import psn.pojo.SysPermission;

public class ResourceUtil implements ApplicationContextAware{

	public static final String ANONYMOUS_URL = "anonymousURL";
	public static final String COMMON_URL = "commonURL"; 
	
	private List<Role> roles;	//私人角色权限
	private List<SysPermission> sysPermissions;//公开或公共权限
	
	private static ResourceUtil resourceUtil = null;
	
	@Autowired
	private RoleMapper roleMapper;
	@Autowired
	private PermissionMapper permissionMapper;
	@Autowired
	private static ApplicationContext applicationContext;
	
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// TODO Auto-generated method stub
		ResourceUtil.applicationContext = applicationContext;
	}

	@PostConstruct
	public void init(){
		roles = roleMapper.retrieveAllRole();
		sysPermissions = permissionMapper.retrieveNonprivatePermission();
	} 
	
	public static ResourceUtil getResourceUtil(){
		if(ResourceUtil.resourceUtil == null){
			ResourceUtil.resourceUtil = (ResourceUtil) applicationContext.getBean("resourceUtil");
		}
		return ResourceUtil.resourceUtil;
	}
	
	public List<String> getKeyList(String key){
		return getKeyList(key, null);
	}
	//公开地址，公共地址，私人权限地址-->(多角色不同权限)  角色与权限多对多关系
	public List<String> getKeyList(String key,Integer roleId){
		List<String> urls = new ArrayList<String>();

		//公开地址
		if(key.contains(ANONYMOUS_URL)){
			for(SysPermission permission:sysPermissions){
				if(permission.isAnonymous()){
					urls.add(permission.getResourse_url());
				}
			}
			return urls;
		}
		
		//公共地址
		if(key.contains(COMMON_URL)){  
			for(SysPermission permission:sysPermissions){
				if(permission.isCommon()){
					urls.add(permission.getResourse_url());
				}
			}
			return urls;
		}
		
		//私人权限地址
		for(Role role:roles){
			if(roleId.equals(role.getId())){
				for(SysPermission permission:role.getSysPermissions()){
					urls.add(permission.getResourse_url());
				}
			}
		}
		return urls;
	}

	
}
