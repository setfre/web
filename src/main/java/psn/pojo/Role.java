package psn.pojo;

import java.util.List;

public class Role {

	private Integer id;
	private String name;	//��ɫ��
	private List<SysPermission> sysPermissions;//��һ��ɫ��Ӧ��Ȩ��
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<SysPermission> getSysPermissions() {
		return sysPermissions;
	}
	public void setSysPermissions(List<SysPermission> sysPermissions) {
		this.sysPermissions = sysPermissions;
	}
	
}
