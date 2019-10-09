package psn.mapper;

import java.util.List;

import psn.pojo.SysPermission;

public interface PermissionMapper {

	public List<SysPermission> retrieveNonprivatePermission();

}
