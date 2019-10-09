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
		
		//��������Ƿ񹫿���Դ(������֤���ɷ���)
		List<String> open_urls = ResourceUtil.getResourceUtil().getKeyList(ResourceUtil.ANONYMOUS_URL);
		
		//�û�����url
		String url = request.getRequestURI();
		System.out.println(url);
		for(String open_url:open_urls){
			//�����ǹ�����Դ�����
			if(url.equals(open_url)){
				return true;
			}
		}
		
		//�û��Ƿ���֤ͨ�� 
		ActiveUser activeUser = (ActiveUser)request.getAttribute("activeUser");
		if(activeUser != null){
			return true;
		}
		
		request.getSession().setAttribute("message", "���½");
		//����ת��ʱ�ᱣ�浱ǰ����·�� 
		request.getRequestDispatcher("../WEB-INF/jsp/login.jsp").forward(request, response);
		return false;
	}

}
