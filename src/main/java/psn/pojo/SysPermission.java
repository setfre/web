package psn.pojo;

import java.util.List;

public class SysPermission {

	private Integer id;
	private String resourse_name;
	private String resourse_url;
	private boolean isAnonymous;
	private boolean isCommon;
	private List<Role> roles;
	
	public List<Role> getRoles() {
		return roles;
	}
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	
	public boolean isAnonymous() {
		return isAnonymous;
	}
	public void setAnonymous(boolean isAnonymous) {
		this.isAnonymous = isAnonymous;
	}
	public boolean isCommon() {
		return isCommon;
	}
	public void setCommon(boolean isCommon) {
		this.isCommon = isCommon;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getResourse_name() {
		return resourse_name;
	}
	public void setResourse_name(String resourse_name) {
		this.resourse_name = resourse_name;
	}
	public String getResourse_url() {
		return resourse_url;
	}
	public void setResourse_url(String resourse_url) {
		this.resourse_url = resourse_url;
	}
	
}
