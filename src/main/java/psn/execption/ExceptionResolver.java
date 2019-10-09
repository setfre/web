package psn.execption;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

public class ExceptionResolver implements HandlerExceptionResolver{

	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object object,
			Exception exception) {
		// TODO Auto-generated method stub
		//����쳣
		exception.printStackTrace();
		
		//ͳһ�쳣����
		//���ϵͳ�Զ����쳣����
		String message = null;
		BaseException baseException = null;
		
		if(exception instanceof InLoginException){
			baseException = (InLoginException) exception;
		}
		else{
			baseException = new InLoginException("δ֪����");
		}
		message = baseException.getMessage();
		
		request.setAttribute("message", message+"???");
		
		try {
			request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new ModelAndView();
	}

}
