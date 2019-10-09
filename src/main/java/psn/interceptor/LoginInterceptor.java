package psn.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import psn.pojo.ActiveUser;
import psn.utils.ResourceUtil;


public class LoginInterceptor implements HandlerInterceptor{

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
		
		//检验访问是否公开资源(无需认证即可访问)
		List<String> open_urls = ResourceUtil.getResourceUtil().getKeyList(ResourceUtil.ANONYMOUS_URL);
		
		//用户访问url
		String url = request.getRequestURI();
		System.out.println(url);
		for(String open_url:open_urls){
			//访问是公开资源则放行
			if(url.equals(open_url)){
				return true;
			}
		}
		
		//用户是否认证通过 
		ActiveUser activeUser = (ActiveUser)request.getAttribute("activeUser");
		if(activeUser != null){
			return true;
		}
		
		request.getSession().setAttribute("message", "请登陆");
		//请求转发时会保存当前请求路径 
		request.getRequestDispatcher("../WEB-INF/jsp/login.jsp").forward(request, response);
		return false;
	}

}
