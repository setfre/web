package psn.pojo;

import java.util.List;

public class SysUser {

	private String uuid;	//id
	private String usercode;//ÕËºÅ
	private String password;//ÃÜÂë
	private String headerImgPath;//Í·ÏñÂ·¾¶
	private List<Role> roles;
	
		
	public List<Role> getRoles() {
		return roles;
	}
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	public String getHeaderImgPath() {
		return headerImgPath;
	}
	public void setHeaderImgPath(String headerImgPath) {
		this.headerImgPath = headerImgPath;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getUsercode() {
		return usercode;
	}
	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public String toString() {
		return "SysUser [uuid=" + uuid + ", usercode=" + usercode + ", password=" + password + ", roles=" + roles + "]";
	}
	
}
