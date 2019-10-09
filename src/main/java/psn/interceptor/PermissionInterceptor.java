package psn.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import psn.pojo.ActiveUser;
import psn.pojo.Role;
import psn.pojo.SysPermission;
import psn.utils.ResourceUtil;

public class PermissionInterceptor implements HandlerInterceptor{

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object, Exception exception)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object object, ModelAndView modelAndView)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
		// TODO Auto-generated method stub
		
		//检验访问是否公开资源(无需认证即可访问)anonymousURL
		List<String> open_urls = ResourceUtil.getResourceUtil().getKeyList(ResourceUtil.ANONYMOUS_URL);
		
		//用户访问url
		String url = request.getRequestURI();
		for(String open_url:open_urls){
			//访问是公开资源则放行
			if(url.equals(open_url)){
				return true;
			}
		}
		
		//访问是否公共资源(认证通过无需分配权限即可访问)commonURL
		List<String> common_urls = ResourceUtil.getResourceUtil().getKeyList(ResourceUtil.COMMON_URL);
		for(String common_url:common_urls){
			//访问是公共资源则放行
			if(url.equals(common_url)){
				return true;
			}
		}
		
		//从session获取用户访问权限
		
		ActiveUser activeUser = (ActiveUser)request.getAttribute("activeUser");
		List<Role> roles = activeUser.getRoles();
		//校验 用户访问是否在权限内
		for(Role role:roles){
			//角色权限
			List<String> person_urls = ResourceUtil.getResourceUtil().getKeyList(null, role.getId());
			for(String person_url:person_urls){
				//拥有访问权限则放行
				if(url.equals(person_url)){
					return true;
				}
			}
		}
		
		//跳转
		request.getRequestDispatcher(".jsp").forward(request, response);
		return false;
	}

}
