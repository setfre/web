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
 * 公开资源路径 /resources/users/username/common/
 * 私有资源路径 /resources/users/username/person/
 * 系统资源路径 /resources/users/
 * 
 */
public class FileUtil {
	/*
	 * 生成抽象路径并生成父类目录
	 */
	private static File generateRealPathBy(String parent,String child){
		
		File file = new File(parent,child);
		//文件已存在
		if(file.exists()){
			return null;
		}
		//检查file的抽象路径名是否存在
		File abstractPath = file.getParentFile();
		//父级目录不存在就创建
		if(!abstractPath.exists()){
			
			abstractPath.mkdirs();
		}
		//只有当父目录存在时才创建
		//file.mkdir();
		//如果指定名称的文件尚不存在，createNewFile方法将创建一个新的空文件
		//file.createNewFile();
		return file;
	}
	
	public static void doUploadFile(MultipartFile multipartFile,String targetPath) throws IllegalStateException, IOException{
		doUploadFile(multipartFile, null, targetPath);
	}
	/**
	 * 
	 * @param multipartFile	spring mvc所提供的文件上传下载工具
	 * @param activeUser	处于登陆状态的用户（可空
	 * @param targetPath	上传文件存储到目标路径
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public static void doUploadFile(MultipartFile multipartFile,ActiveUser activeUser,String targetPath) throws IllegalStateException, IOException{
		//获得文件名
		String originalName = multipartFile.getOriginalFilename(); 
		//获得文件类型
		String type = multipartFile.getContentType();
		//获得文件大小
		long size = multipartFile.getSize();
		
		StringBuffer childPath = new StringBuffer("");
		
		if(activeUser != null){
			//私有资源
			childPath = childPath.append(activeUser.getUsercode()+File.separator);
		}
		//生成存储路径
		File file = generateRealPathBy(targetPath,childPath.append(originalName).toString());
		
		System.out.println("文件名:"+originalName+",文件类型:"+type+",文件大小:"+size
				+",上传时间:"+System.currentTimeMillis()+",文件目标目录:"+file.toString()
				+",文件的绝对路径:"+file.getAbsolutePath()+",文件的规范路径:"+file.getCanonicalPath());
			
		//存储到本地
		multipartFile.transferTo(file);
	}
	
	public static void downloadFile(String rootPath,String resourceName,OutputStream outputStream) throws IOException{
		downloadFile(rootPath, resourceName, null, outputStream);
	}
	
	public static void downloadFile(String rootPath,String resourceName,
			ActiveUser activeUser,OutputStream outputStream) throws IOException{
		
		StringBuffer childPath = new StringBuffer("");
		
		if(activeUser != null){
			//私有资源
			childPath = childPath.append(activeUser.getUsercode()+File.separator);
		}
		//生成请求资源绝对路径
		File file = new File(rootPath,resourceName);
		
		InputStream inputStream = null;
		//资源不存在或无法读取
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
	 * @param resourcePath	搜索目录
	 * @throws IOException 
	 */
	public static List<String> findImg(String resourcePath) throws IOException{
		return findFileBy(resourcePath,new String[]{".jpg",".jpeg"},true);
	}
	
	@Test
	public void test(){ 
		try {
			List<String> paths = findImg("C:\\Users\\安政宇\\Desktop\\picture");
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
	 * @param resourcePath	待搜索路径
	 * @param postfixs		指定后缀		使用数组格式，指代不可更改
	 * @param checkAllPath		false只搜索当前路径 true搜索此路径下所有文件
	 * @return List<String> 封装返回路径地址
	 * @throws FileNotFoundException	不存在的路径时抛出
	 */
	public static List<String> findFileBy(String resourcePath,String[] postfixs,boolean checkAllPath) throws FileNotFoundException{
		
		File rootPath = new File(resourcePath);
		//指定路径不存在
		if(!rootPath.exists()){
			throw new FileNotFoundException();
		}
		
		List<String> paths = new ArrayList<String>();
		//此路径下所有文件
		File[] files = rootPath.listFiles();
		//遍历所有文件
		for(File file:files){
			String resourceName = file.getName();
			
			if(!file.isDirectory()){//不是目录
				
				for(String postfix:postfixs){
					 //查找指定后缀结尾的文件
					if(resourceName.endsWith(postfix)){
						
						paths.add(file.getAbsolutePath());
					}
				}
			}
			else if(checkAllPath){//全目录递归查找
				
				String relativePath = resourcePath + File.separator + file.getName() ;
				
				paths.addAll(findFileBy(relativePath, postfixs,checkAllPath));
			}
		}
		return paths;
	}
	/**
	 * 使用流输出指定后缀文件
	 * @param resourcePath 	待搜索路径
	 * @param postfixs	 	指定后缀
	 * @param outputStream	指定输出流
	 * @throws IOException
	 */
	public static void downloadFile(String resourcePath,String[] postfixs,OutputStream outputStream) throws IOException{
		
		File rootPath = new File(resourcePath);
		//指定路径不存在
		if(!rootPath.exists()){
			throw new FileNotFoundException();
		}
		//此路径下所有文件
		File[] files = rootPath.listFiles();
		
		InputStream inputStream = null;
		
		byte[] bytes = new byte[1024 * 4];
		
		int len = 0;
			//遍历所有文件
		for(File file:files){
			try {
				String resourceName = file.getName();
				
				for(String postfix:postfixs){
					//以指定后缀结尾且可读
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
	 * 批量压缩文件
	 * @param paths			批量压缩文件路径
	 * @param fileName		生成压缩文件名称
	 * @throws IOException
	 */
	public static void zip(String[] paths, String fileName) throws IOException{
		
		ZipOutputStream zipOutputStream = null;
		
		try {
			
			zipOutputStream = new ZipOutputStream(new FileOutputStream(fileName));
			//遍历所有待压缩文件
			for(String filePath:paths){
				
				File file = new File(filePath);
				//单文件相对路径
				String relativePath = file.getName();
				
				if(file.isDirectory()){//是目录就添加分隔符  separator:与系统相关的默认名称 - 分隔符字符，代表系统目录中的间隔符，解决兼容问题
					
					relativePath += File.separator;
				}
				//执行压缩
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
	 * @param file				当前索引文件
	 * @param relativePath		相对路径：1.非目录  2.目录  (由父级目录指定
	 * @param zipOutputStream	
	 * @throws IOException
	 */
	public static void zipFile(File file, String relativePath, ZipOutputStream zipOutputStream) throws IOException{
		
		InputStream inputStream = null;
		
		try{
			
			if(!file.isDirectory()){//是文件执行压缩
				//当前待压缩文件准备以zip格式压缩
				ZipEntry zipEntry = new ZipEntry(relativePath);
				//开始编写新的ZIP文件条目，并将流定位到条目数据的开头。
				zipOutputStream.putNextEntry(zipEntry);
				
				inputStream = new FileInputStream(file);
				
				byte[] bytes = new byte[1024 * 4];
				
				int len = 0;
				
				while((len = inputStream.read(bytes))>0){
					
					zipOutputStream.write(bytes, 0, len);
				}
				
				zipOutputStream.flush();
				//关闭当前的ZIP条目，并定位流以写入下一个条目。 
				zipOutputStream.closeEntry();
			}
			else{//是目录执行递归
				
				String tempPath = null;
				//遍历当前目录所有文件
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
	 * @param fileName	解压缩文件名
	 * @param path		生成文件路径
	 * @throws IOException
	 */
	public static void unzip(String fileName,String path) throws IOException{
		
		FileOutputStream outputStream = null;
		
		InputStream inputStream = null;
		
		try{
			//待解压缩文件
			ZipFile zipFile = new ZipFile(new File(fileName));
			//遍历文件根目录
			Enumeration en = zipFile.entries();
			
			while (en.hasMoreElements()) {
				//获得当前索引文件，并将索引后移
				ZipEntry zipEntry = (ZipEntry) en.nextElement();
				
				if(!zipEntry.isDirectory()){//不是目录就解压
					
					inputStream = zipFile.getInputStream(zipEntry);
					
					File file = new File(path + zipEntry.getName());
					//获得父级抽象目录
					File temp = file.getParentFile();
					//生成父级目录
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
		//File对象是不可变的，创建后他始终表示相同的路径名，当重命名时旧的File对象仍然表示原始路径
		//File对象表示路径名，而不是文件系统中的实际文件
		File file = new File(resourcePath);
		//立即删除
		//目录必须为空时才能删除。
		if(file.delete()){
			System.out.println("删除成功");
		}
		//延迟删除
		//在JVM终止时删除
		file.deleteOnExit();
	}
	
	public static void renameFile(String resourcePath,String newName){
		File file = new File(resourcePath);
		File newFile = new File(newName);
		if(file.renameTo(newFile)){
			System.out.println("重命名成功");
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
		 * ObjectOutputStream用于序列化对象
		 * ObjectInputStream用于反序列化对象
		 */
		Person person = new Person("123","123");
		Car car = new Car("234", "234");
		File file = new File("person.ser");
		try {
			ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
			//序列化
			//outputStream.writeObject(person);
			outputStream.writeObject(car);
			ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file));
			//反序列化
			//Person person2 = (Person) inputStream.readObject();
			Car car2 = (Car) inputStream.readObject();
			//System.out.println(person2);
			System.out.println(car2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//外部序列化需要实现Externalizable接口,需要默认构造方法
	}
	
	public static void tokenDemo(){
		/**基于字符串拆分*/
		//StringTokenizer对象根据对定界符的定义将字符串拆分为令牌
		//他一次返回一个令牌，并可随时更改分隔符
		String string = "This is a text,this is another test.";
		String delimiters = " ,";//一个空格和一个逗号
		StringTokenizer st = new StringTokenizer(string, delimiters);
		while(st.hasMoreTokens()){
			System.out.println(st.nextToken());
		}
		System.out.println("***");
		StringTokenizer st2 = new StringTokenizer(string, delimiters);
		while(st2.hasMoreElements()){
			System.out.println(st2.nextElement());
		}
		//也可使用String类的split()方法将字符串基于分隔符拆分
		//split方法接受正则表达式作为分隔符
		/**基于字符流访问令牌*/
		//可以使用FileReader对象或任何其他的Reader对象作为数据源
		StringReader sr = new StringReader(string);
		StreamTokenizer stt = new StreamTokenizer(sr);
		try {
			//重复调用nextToken方法，填充StreamTokenizer对象的3个字段。
			while(stt.nextToken()!=StreamTokenizer.TT_EOF){
				switch (stt.ttype) {//以读取的令牌类型
				//TT_EOF:已达到流的末尾
				//TT_EOL:以达到行尾
				//TT_WORD:单词(字符串)已从流中读取为令牌
				//TT_NUMBER:数字已从流中读为令牌
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