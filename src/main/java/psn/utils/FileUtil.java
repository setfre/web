package psn.utils;

import java.io.Externalizable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.jasper.tagplugins.jstl.core.Out;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.web.multipart.MultipartFile;

import com.mysql.jdbc.Field;

import psn.pojo.ActiveUser;
/**
 * ������Դ·�� /resources/users/username/common/
 * ˽����Դ·�� /resources/users/username/person/
 * ϵͳ��Դ·�� /resources/users/
 * 
 */
public class FileUtil {
	/*
	 * ���ɳ���·�������ɸ���Ŀ¼
	 */
	private static File generateRealPathBy(String parent,String child){
		
		File file = new File(parent,child);
		//�ļ��Ѵ���
		if(file.exists()){
			return null;
		}
		//���file�ĳ���·�����Ƿ����
		File abstractPath = file.getParentFile();
		//����Ŀ¼�����ھʹ���
		if(!abstractPath.exists()){
			
			abstractPath.mkdirs();
		}
		//ֻ�е���Ŀ¼����ʱ�Ŵ���
		//file.mkdir();
		//���ָ�����Ƶ��ļ��в����ڣ�createNewFile����������һ���µĿ��ļ�
		//file.createNewFile();
		return file;
	}
	
	public static void doUploadFile(MultipartFile multipartFile,String targetPath) throws IllegalStateException, IOException{
		doUploadFile(multipartFile, null, targetPath);
	}
	/**
	 * 
	 * @param multipartFile	spring mvc���ṩ���ļ��ϴ����ع���
	 * @param activeUser	���ڵ�½״̬���û����ɿ�
	 * @param targetPath	�ϴ��ļ��洢��Ŀ��·��
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public static void doUploadFile(MultipartFile multipartFile,ActiveUser activeUser,String targetPath) throws IllegalStateException, IOException{
		//����ļ���
		String originalName = multipartFile.getOriginalFilename(); 
		//����ļ�����
		String type = multipartFile.getContentType();
		//����ļ���С
		long size = multipartFile.getSize();
		
		StringBuffer childPath = new StringBuffer("");
		
		if(activeUser != null){
			//˽����Դ
			childPath = childPath.append(activeUser.getUsercode()+File.separator);
		}
		//���ɴ洢·��
		File file = generateRealPathBy(targetPath,childPath.append(originalName).toString());
		
		System.out.println("�ļ���:"+originalName+",�ļ�����:"+type+",�ļ���С:"+size
				+",�ϴ�ʱ��:"+System.currentTimeMillis()+",�ļ�Ŀ��Ŀ¼:"+file.toString()
				+",�ļ��ľ���·��:"+file.getAbsolutePath()+",�ļ��Ĺ淶·��:"+file.getCanonicalPath());
			
		//�洢������
		multipartFile.transferTo(file);
	}
	
	public static void downloadFile(String rootPath,String resourceName,OutputStream outputStream) throws IOException{
		downloadFile(rootPath, resourceName, null, outputStream);
	}
	
	public static void downloadFile(String rootPath,String resourceName,
			ActiveUser activeUser,OutputStream outputStream) throws IOException{
		
		StringBuffer childPath = new StringBuffer("");
		
		if(activeUser != null){
			//˽����Դ
			childPath = childPath.append(activeUser.getUsercode()+File.separator);
		}
		//����������Դ����·��
		File file = new File(rootPath,resourceName);
		
		InputStream inputStream = null;
		//��Դ�����ڻ��޷���ȡ
		if(!file.exists() || !file.canRead()){
			return;
		}
		
		inputStream = new FileInputStream(file);
		
		byte[] bytes = new byte[1024*4];
		
		int len = 0;
		
		while((len = inputStream.read(bytes)) > 0){
			
			outputStream.write(bytes, 0, len);
			
		}
		
		inputStream.close();
	}
	/**
	 * 
	 * @param resourcePath	����Ŀ¼
	 * @throws IOException 
	 */
	public static List<String> findImg(String resourcePath) throws IOException{
		return findFileBy(resourcePath,new String[]{".jpg",".jpeg"},true);
	}
	
	@Test
	public void test(){ 
		try {
			List<String> paths = findImg("C:\\Users\\������\\Desktop\\picture");
			for(String path:paths){
				System.out.println(path);
			}
			System.out.println(paths.size());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @param resourcePath	������·��
	 * @param postfixs		ָ����׺		ʹ�������ʽ��ָ�����ɸ���
	 * @param checkAllPath		falseֻ������ǰ·�� true������·���������ļ�
	 * @return List<String> ��װ����·����ַ
	 * @throws FileNotFoundException	�����ڵ�·��ʱ�׳�
	 */
	public static List<String> findFileBy(String resourcePath,String[] postfixs,boolean checkAllPath) throws FileNotFoundException{
		
		File rootPath = new File(resourcePath);
		//ָ��·��������
		if(!rootPath.exists()){
			throw new FileNotFoundException();
		}
		
		List<String> paths = new ArrayList<String>();
		//��·���������ļ�
		File[] files = rootPath.listFiles();
		//���������ļ�
		for(File file:files){
			String resourceName = file.getName();
			
			if(!file.isDirectory()){//����Ŀ¼
				
				for(String postfix:postfixs){
					 //����ָ����׺��β���ļ�
					if(resourceName.endsWith(postfix)){
						
						paths.add(file.getAbsolutePath());
					}
				}
			}
			else if(checkAllPath){//ȫĿ¼�ݹ����
				
				String relativePath = resourcePath + File.separator + file.getName() ;
				
				paths.addAll(findFileBy(relativePath, postfixs,checkAllPath));
			}
		}
		return paths;
	}
	/**
	 * ʹ�������ָ����׺�ļ�
	 * @param resourcePath 	������·��
	 * @param postfixs	 	ָ����׺
	 * @param outputStream	ָ�������
	 * @throws IOException
	 */
	public static void downloadFile(String resourcePath,String[] postfixs,OutputStream outputStream) throws IOException{
		
		File rootPath = new File(resourcePath);
		//ָ��·��������
		if(!rootPath.exists()){
			throw new FileNotFoundException();
		}
		//��·���������ļ�
		File[] files = rootPath.listFiles();
		
		InputStream inputStream = null;
		
		byte[] bytes = new byte[1024 * 4];
		
		int len = 0;
			//���������ļ�
		for(File file:files){
			try {
				String resourceName = file.getName();
				
				for(String postfix:postfixs){
					//��ָ����׺��β�ҿɶ�
					if(resourceName.endsWith(postfix)&& file.canRead()){
						
						inputStream = new FileInputStream(new File(resourcePath+file.separator+resourceName));
						
						while((len = inputStream.read(bytes))>0){
							
							outputStream.write(bytes,0,len);
						}
						
						inputStream.close();
					}
				}
			} finally {
				try {
					if(inputStream != null){
						inputStream.close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * ����ѹ���ļ�
	 * @param paths			����ѹ���ļ�·��
	 * @param fileName		����ѹ���ļ�����
	 * @throws IOException
	 */
	public static void zip(String[] paths, String fileName) throws IOException{
		
		ZipOutputStream zipOutputStream = null;
		
		try {
			
			zipOutputStream = new ZipOutputStream(new FileOutputStream(fileName));
			//�������д�ѹ���ļ�
			for(String filePath:paths){
				
				File file = new File(filePath);
				//���ļ����·��
				String relativePath = file.getName();
				
				if(file.isDirectory()){//��Ŀ¼����ӷָ���  separator:��ϵͳ��ص�Ĭ������ - �ָ����ַ�������ϵͳĿ¼�еļ�����������������
					
					relativePath += File.separator;
				}
				//ִ��ѹ��
				zipFile(file,relativePath, zipOutputStream);
			}
		} finally {
			try {
				if(zipOutputStream != null){
					
					zipOutputStream.close();
				}
				
			} catch (IOException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 
	 * @param file				��ǰ�����ļ�
	 * @param relativePath		���·����1.��Ŀ¼  2.Ŀ¼  (�ɸ���Ŀ¼ָ��
	 * @param zipOutputStream	
	 * @throws IOException
	 */
	public static void zipFile(File file, String relativePath, ZipOutputStream zipOutputStream) throws IOException{
		
		InputStream inputStream = null;
		
		try{
			
			if(!file.isDirectory()){//���ļ�ִ��ѹ��
				//��ǰ��ѹ���ļ�׼����zip��ʽѹ��
				ZipEntry zipEntry = new ZipEntry(relativePath);
				//��ʼ��д�µ�ZIP�ļ���Ŀ����������λ����Ŀ���ݵĿ�ͷ��
				zipOutputStream.putNextEntry(zipEntry);
				
				inputStream = new FileInputStream(file);
				
				byte[] bytes = new byte[1024 * 4];
				
				int len = 0;
				
				while((len = inputStream.read(bytes))>0){
					
					zipOutputStream.write(bytes, 0, len);
				}
				
				zipOutputStream.flush();
				//�رյ�ǰ��ZIP��Ŀ������λ����д����һ����Ŀ�� 
				zipOutputStream.closeEntry();
			}
			else{//��Ŀ¼ִ�еݹ�
				
				String tempPath = null;
				//������ǰĿ¼�����ļ�
				for(File f:file.listFiles()){
					
					tempPath = relativePath + f.getName();
					
					if(f.isDirectory()){
						
						zipFile(file, tempPath, zipOutputStream);
					}
				}
			}
		} finally {
			if(inputStream != null){
				
				inputStream.close();
			}
		}
	}
	
	/**
	 * 
	 * @param fileName	��ѹ���ļ���
	 * @param path		�����ļ�·��
	 * @throws IOException
	 */
	public static void unzip(String fileName,String path) throws IOException{
		
		FileOutputStream outputStream = null;
		
		InputStream inputStream = null;
		
		try{
			//����ѹ���ļ�
			ZipFile zipFile = new ZipFile(new File(fileName));
			//�����ļ���Ŀ¼
			Enumeration en = zipFile.entries();
			
			while (en.hasMoreElements()) {
				//��õ�ǰ�����ļ���������������
				ZipEntry zipEntry = (ZipEntry) en.nextElement();
				
				if(!zipEntry.isDirectory()){//����Ŀ¼�ͽ�ѹ
					
					inputStream = zipFile.getInputStream(zipEntry);
					
					File file = new File(path + zipEntry.getName());
					//��ø�������Ŀ¼
					File temp = file.getParentFile();
					//���ɸ���Ŀ¼
					temp.mkdirs();
					
					outputStream = new FileOutputStream(path + zipEntry.getName());
					
					byte[] bytes = new byte[1024 * 4];
					
					int len = 0;
					
					while((len = inputStream.read(bytes))>0){
						
						outputStream.write(bytes, 0, len);
					}
				}
			}
		} finally {
			try {
				if(inputStream != null){
					inputStream.close();
				}
				if(outputStream != null){
					outputStream.close();
				}
			} catch (IOException e) {
				// TODO: handle exception
			}
		}
	}
	
	public static void deleteFile(String resourcePath){
		//File�����ǲ��ɱ�ģ���������ʼ�ձ�ʾ��ͬ��·��������������ʱ�ɵ�File������Ȼ��ʾԭʼ·��
		//File�����ʾ·�������������ļ�ϵͳ�е�ʵ���ļ�
		File file = new File(resourcePath);
		//����ɾ��
		//Ŀ¼����Ϊ��ʱ����ɾ����
		if(file.delete()){
			System.out.println("ɾ���ɹ�");
		}
		//�ӳ�ɾ��
		//��JVM��ֹʱɾ��
		file.deleteOnExit();
	}
	
	public static void renameFile(String resourcePath,String newName){
		File file = new File(resourcePath);
		File newFile = new File(newName);
		if(file.renameTo(newFile)){
			System.out.println("�������ɹ�");
		}
	}
	public static Logger logger = Logger.getLogger(FileUtil.class);
	
	public static void LogDemo(){
		System.out.println("logger");
		logger.debug("logger");
		logger.info("logger");
		logger.error("logger");
	}
	
	public static void serializationObject() {
		/**
		 * ObjectOutputStream�������л�����
		 * ObjectInputStream���ڷ����л�����
		 */
		Person person = new Person("123","123");
		Car car = new Car("234", "234");
		File file = new File("person.ser");
		try {
			ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
			//���л�
			//outputStream.writeObject(person);
			outputStream.writeObject(car);
			ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file));
			//�����л�
			//Person person2 = (Person) inputStream.readObject();
			Car car2 = (Car) inputStream.readObject();
			//System.out.println(person2);
			System.out.println(car2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//�ⲿ���л���Ҫʵ��Externalizable�ӿ�,��ҪĬ�Ϲ��췽��
	}
	
	public static void tokenDemo(){
		/**�����ַ������*/
		//StringTokenizer������ݶԶ�����Ķ��彫�ַ������Ϊ����
		//��һ�η���һ�����ƣ�������ʱ���ķָ���
		String string = "This is a text,this is another test.";
		String delimiters = " ,";//һ���ո��һ������
		StringTokenizer st = new StringTokenizer(string, delimiters);
		while(st.hasMoreTokens()){
			System.out.println(st.nextToken());
		}
		System.out.println("***");
		StringTokenizer st2 = new StringTokenizer(string, delimiters);
		while(st2.hasMoreElements()){
			System.out.println(st2.nextElement());
		}
		//Ҳ��ʹ��String���split()�������ַ������ڷָ������
		//split��������������ʽ��Ϊ�ָ���
		/**�����ַ�����������*/
		//����ʹ��FileReader������κ�������Reader������Ϊ����Դ
		StringReader sr = new StringReader(string);
		StreamTokenizer stt = new StreamTokenizer(sr);
		try {
			//�ظ�����nextToken���������StreamTokenizer�����3���ֶΡ�
			while(stt.nextToken()!=StreamTokenizer.TT_EOF){
				switch (stt.ttype) {//�Զ�ȡ����������
				//TT_EOF:�Ѵﵽ����ĩβ
				//TT_EOL:�Դﵽ��β
				//TT_WORD:����(�ַ���)�Ѵ����ж�ȡΪ����
				//TT_NUMBER:�����Ѵ����ж�Ϊ����
				case StreamTokenizer.TT_WORD://
					System.out.println("String value: " + stt.sval);
					break;
				case StreamTokenizer.TT_NUMBER:
					System.out.println("String value: " + stt.nval);
					break;
				}
			}
		} catch (IOException e) {
			// TODO: handle exception
		}
	}
	
	public static void main(String[] args){
		//FileUtil.serializationObject();
		FileUtil.tokenDemo();
	}
	
	
}
class Person implements Serializable{
	private String name;
	private String sex;
	
	@Override
	public String toString() {
		return "Person [name=" + name + ", sex=" + sex + "]";
	}
	public Person(String name, String sex) {
		super();
		this.name = name;
		this.sex = sex;
	}
}
class Car implements Externalizable{

	private String name = "Unknown";
	private String sex = "Unknown";
	public Car(){
		
	}
	public Car(String name, String sex) {
		super();
		this.name = name;
		this.sex = sex;
	}

	@Override
	public String toString() {
		return "Car [name=" + name + ", sex=" + sex + "]";
	}

	public void writeExternal(ObjectOutput out) throws IOException {
		// TODO Auto-generated method stub
		out.writeUTF(this.name);
		out.writeUTF(this.sex);
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		this.name = in.readUTF();
		this.sex  = in.readUTF();
	}
	
}