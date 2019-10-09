package psn.mapper;

import java.util.List;

import psn.pojo.ActiveUser;
import psn.pojo.SysPermission;
import psn.pojo.SysUser;

public interface UserMapper extends BaseMapper<SysUser>{

	SysUser findSysuserByUsercode(String usercode);

	List<SysPermission> findSysPermissionList(String userId);

	List<SysPermission> findMenuList(String userId);

}
