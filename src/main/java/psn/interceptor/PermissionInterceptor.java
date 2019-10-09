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
		
		//��������Ƿ񹫿���Դ(������֤���ɷ���)anonymousURL
		List<String> open_urls = ResourceUtil.getResourceUtil().getKeyList(ResourceUtil.ANONYMOUS_URL);
		
		//�û�����url
		String url = request.getRequestURI();
		for(String open_url:open_urls){
			//�����ǹ�����Դ�����
			if(url.equals(open_url)){
				return true;
			}
		}
		
		//�����Ƿ񹫹���Դ(��֤ͨ���������Ȩ�޼��ɷ���)commonURL
		List<String> common_urls = ResourceUtil.getResourceUtil().getKeyList(ResourceUtil.COMMON_URL);
		for(String common_url:common_urls){
			//�����ǹ�����Դ�����
			if(url.equals(common_url)){
				return true;
			}
		}
		
		//��session��ȡ�û�����Ȩ��
		
		ActiveUser activeUser = (ActiveUser)request.getAttribute("activeUser");
		List<Role> roles = activeUser.getRoles();
		//У�� �û������Ƿ���Ȩ����
		for(Role role:roles){
			//��ɫȨ��
			List<String> person_urls = ResourceUtil.getResourceUtil().getKeyList(null, role.getId());
			for(String person_url:person_urls){
				//ӵ�з���Ȩ�������
				if(url.equals(person_url)){
					return true;
				}
			}
		}
		
		//��ת
		request.getRequestDispatcher(".jsp").forward(request, response);
		return false;
	}

}
