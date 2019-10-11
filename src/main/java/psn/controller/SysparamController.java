package psn.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.ResolverUtil.NameEndsWith;
import org.eclipse.jdt.internal.compiler.util.Util.Displayable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import psn.execption.InLoginException;
import psn.pojo.ActiveUser;
import psn.pojo.Pagination;
import psn.pojo.Role;
import psn.pojo.SysUser;
import psn.service.SysService;
import psn.utils.FileUtil;
import psn.utils.VerifyCodeUtil;

@Controller
@RequestMapping("sysparam")
public class SysparamController {
	
	@Autowired
	private SysService sysService;
	
	public static Logger logger = Logger.getLogger(SysparamController.class);
	
	@RequestMapping("doExit")
	public void exit(HttpSession session,HttpServletResponse response) throws IOException{
		
		session.invalidate();
		response.sendRedirect("toShow.action");
	}
	
	@RequestMapping("verifyCode")
	public void VerifyCode(HttpServletRequest request,HttpServletResponse response){
		
		String verifyCode = VerifyCodeUtil.GenerateVerifyCode(5);
		
		OutputStream outputStream = null;
		
		try {
			outputStream = response.getOutputStream();
			
			request.getSession().setAttribute("verifyCode", verifyCode);
			//3:1���������֤��
			VerifyCodeUtil.outputVerifyImg(114, 38, verifyCode, outputStream);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(outputStream != null){
					
					outputStream.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@RequestMapping("doLogin")
	public ModelAndView loginSubmit(HttpServletRequest request,HttpServletResponse response,
			String usercode,String password,String randomCode){
		
		ModelAndView modelAndView = new ModelAndView();

		String verifyCode = (String) request.getSession().getAttribute("verifyCode");
		//�û������֤
		try {
			//У����֤�� 
			if(!randomCode.toUpperCase().equals(verifyCode)){
				throw new InLoginException("��֤�����");
			}
			//��֤
			SysUser sysUser = sysService.authenticat(usercode, password);
			//��¼session
			request.getSession().setAttribute("activeUser", sysUser);
			
			modelAndView.setViewName("jsp/show");
		} catch (InLoginException e) {
			logger.debug("randomCode:"+randomCode+",verifyCode:"+verifyCode
					+"\nexceptionInfo:"+e.getMessage());
			
			modelAndView.addObject("message", e.getMessage());
			
			modelAndView.setViewName("jsp/login");
		}
		
		//��ת����ҳ
		return modelAndView;
	}
	//��ҵ����׳��쳣���Ʋ㲶��������
	//ҵ��㷵�ذ�װ����Ʋ��������
	//ȫ���쳣����ͨ���쳣������Ϣ����
	@RequestMapping("doRegist")
	public ModelAndView registSubmit(HttpSession session,SysUser user,String randomCode){
		
		ModelAndView modelAndView = new ModelAndView();
		//��ȡ��֤�벢У��
		String verifyCode = (String)session.getAttribute("verifyCode");
		
		System.out.println("\n randomCode:"+randomCode+",verifyCode:"+verifyCode);
		
		try {
			if(!randomCode.toUpperCase().equals(verifyCode)){
				throw new InLoginException("��֤�����");
			}
			sysService.register(user);
			
			modelAndView.setViewName("jsp/login");
		} catch (InLoginException e) {
			// TODO Auto-generated catch block
			if(logger.isDebugEnabled()){
				logger.debug("randomCode:"+randomCode+",verifyCode:"+verifyCode);
			}
			
			modelAndView.addObject("message", e.getMessage());
			
			modelAndView.setViewName("jsp/register");
		}
		
		return modelAndView;
	}

	@RequestMapping("doUploadUserHeaderImg")
	public ModelAndView doUploadUserHeaderImg(HttpServletRequest request,
			@RequestParam("file")MultipartFile multipartFile) throws IOException{
		
		ModelAndView modelAndView = new ModelAndView();
		//����û���
		SysUser activeUser = (SysUser) request.getSession().getAttribute("activeUser");
		System.out.println("\n"+activeUser);
		try {
			if(activeUser == null){
				throw new InLoginException("���ȵ�½!");
			}
			if(multipartFile.isEmpty()){
				throw new InLoginException("�Ѵ��ڵ��ļ�!");
			}
			//��÷�������Ŀ¼
			StringBuffer rootPath = new StringBuffer(WebUtils.getRealPath(request.getServletContext(), ""));
			//���츸��Ŀ¼
			File file = new File(rootPath.toString()).getParentFile();
			//��������Ŀ��·��
			rootPath.append("resources\\users\\" + activeUser.getUsercode()+ "\\common");
			//�ϴ������ط��ؾ���·��
			String absolutePaths = FileUtil.doUploadFile(multipartFile,rootPath.toString());
			//ת��Ŀ¼ͼƬ�����·��
			String relativePath = absolutePaths.substring(file.toString().length(), absolutePaths.length());
			
			activeUser.setHeaderImgPath(relativePath);
			//���浽�������ݿ�
			sysService.updateUserHeaderImg(activeUser);
			
			modelAndView.addObject("message", "�ϴ��ɹ�");
			
			modelAndView.setViewName("jsp/success");
		} catch (FileAlreadyExistsException e) {
			
			modelAndView.addObject("message", e.getMessage());
			
			modelAndView.setViewName("jsp/error");
		} catch (InLoginException e) {
			modelAndView.addObject("message", e.getMessage());
			
			modelAndView.setViewName("jsp/error");
		}

		return modelAndView;
	}
	
	@RequestMapping(value = "doUpload",method = RequestMethod.POST)
	public String doUploadFile(HttpServletRequest request,
			@RequestParam("file")MultipartFile multipartFile) throws IOException{
		
		//����û���
		ActiveUser activeUser = (ActiveUser) request.getAttribute("activeUser");
		
		if(!multipartFile.isEmpty()){
			//��÷�������Ŀ¼
			String rootPath = WebUtils.getRealPath(request.getServletContext(), "/resources/users");
			
			System.out.println(rootPath);
			//�ϴ�������
			FileUtil.doUploadFile(multipartFile, rootPath);
			
			return "success";
		}
		return "error";
	}
	
	@RequestMapping(value = "doBatchUpload",method = RequestMethod.POST)
	public String doBatchUploadFile(HttpServletRequest request,
			@RequestParam("file")MultipartFile[] multipartFiles) throws IOException{
		//����û���
		ActiveUser activeUser = (ActiveUser) request.getAttribute("activeUser");
		
		System.out.println();
		for(MultipartFile multipartFile:multipartFiles){
			
			System.out.println(multipartFile.getOriginalFilename());
			
			if(!multipartFile.isEmpty()){
				//��÷�������Ŀ¼
				String rootPath = WebUtils.getRealPath(request.getServletContext(), "/resources/users");
				//�ϴ�������
				FileUtil.doUploadFile(multipartFile, rootPath);
				
				return "success";
			}
		}
		return "error";
	}
	
	@RequestMapping("download")	
	public String downloadFile(@RequestParam("fileName")String resourceName,String resourcePath,
			HttpServletRequest request,HttpServletResponse response) throws IOException {
		
		if(resourceName != null && resourceName != ""){
			
			String rootPath = WebUtils.getRealPath(request.getServletContext(), "/resources/users");
			
			ActiveUser activeUser = (ActiveUser) request.getAttribute("activeUser");
			
			OutputStream outputStream = null;
			
			try {
				outputStream = response.getOutputStream();
				
				FileUtil.downloadFile(rootPath, resourceName, outputStream);
				
				response.setContentType("application/force-download");
				//�����ļ�����
				response.setHeader("Content-Disposition", "attachment;filename="+resourceName);
				
			} finally {
				try {
					if(outputStream != null){
						outputStream.close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	@RequestMapping("toUserManager")
	public String userManager(HttpServletRequest request,
			@RequestParam(defaultValue="1")Integer currentIndex,
			@RequestParam(defaultValue="5")Integer recordShowSize) throws IOException{
		
		ActiveUser activeUser = (ActiveUser) request.getAttribute("activeUser");
		
		if(activeUser!= null){
			for(Role role:activeUser.getRoles()){
				if(role.getId()!=1){
					//�ǹ���Ա
				}
			}
		}
		Pagination<SysUser> pagination = sysService.retrieveUserInfoPagination(currentIndex,recordShowSize);
		//��÷�������Ŀ¼
		StringBuffer rootPath = new StringBuffer(WebUtils.getRealPath(request.getServletContext(), ""));
		//���츸��Ŀ¼
		File file = new File(rootPath.toString()).getParentFile();
		//��������Ŀ��·��
		rootPath.append("resources\\end");
		//����ָ��Ŀ¼ͼƬ�ľ���·��
		List<String> absolutePaths = FileUtil.findImg(rootPath.toString());
		//ת��Ŀ¼ͼƬ�����·��
		List<String> relativePaths = new ArrayList<String>();
		for(String path:absolutePaths){
			relativePaths.add(path.substring(file.toString().length(), path.length()));
		}
		
		request.setAttribute("imgPaths", relativePaths);
		request.setAttribute("pagination", pagination);
		
		return "jsp/show";
	}
	
	@RequestMapping("toPic")
	public ModelAndView display(HttpServletRequest request){
		ModelAndView modelAndView = new ModelAndView();
		
		try {
			//��÷�������Ŀ¼
			StringBuffer rootPath = new StringBuffer(WebUtils.getRealPath(request.getServletContext(), ""));
			//���츸��Ŀ¼
			File file = new File(rootPath.toString()).getParentFile();
			//��������Ŀ��·��
			rootPath.append("resources\\picture");
			//����ָ��Ŀ¼ͼƬ�ľ���·�� 
			List<String> absolutePaths = FileUtil.findImg(rootPath.toString());
			//ת��Ŀ¼ͼƬ�����·��
			List<String> relativePaths = new ArrayList<String>();
			for(String path:absolutePaths){
				relativePaths.add(path.substring(file.toString().length(), path.length()));
			}
			
			modelAndView.addObject("imgPaths",  relativePaths);
			modelAndView.addObject("message", "��ͼƬ");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			modelAndView.addObject("message", "��ͼƬ");
			e.printStackTrace();
		}
		modelAndView.setViewName("jsp/pic");;
		return modelAndView; 
	}
	@RequestMapping("toShow")
	public ModelAndView toShow(){
		ModelAndView modelAndView = new ModelAndView();
		
		try {
			modelAndView.addObject("imgPaths",  FileUtil.findImg("C:\\Users\\������\\Desktop\\picture"));
			modelAndView.addObject("message", "��ͼƬ");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			modelAndView.addObject("message", "��ͼƬ");
			e.printStackTrace();
		} 
		modelAndView.setViewName("jsp/show");
		return modelAndView;
	}
	@RequestMapping("toError")
	public String toError(){
		return "jsp/error";
	}
}
