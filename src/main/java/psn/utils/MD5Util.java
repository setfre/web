package psn.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import org.junit.Test;

/**
 * Message Digest Algorithm
 * MD5(消息摘要算法第五版)为计算机安全领域广泛使用的一种散列函数,用以提供消息的完整性保护
 * 
 * MD5特点
 * 	压缩性:任意长度的数据,算出的MD5值长度都是固定的
 * 	容易计算:从原数计算出MD5值很容易
 * 	抗修改性:对元数据进行任何改动，哪怕只修改1个字节，得到的MD5都有很大区别
 * 	强抗碰撞:已知原数据和其MD5值,想找到一个具有相同MD5值得数据是非常难的
 * 
 * @author Administrator
 *	MD5生成字节数组
 */
public class MD5Util {

	private static final String HEX_STR = "0123456789ABCDEF";
	
	//16进制字符串转换字节数组
	private static byte[] hexStringToByte(String hex) {
		
		int length = hex.length() / 2;
		char[] array = hex.toCharArray();
		byte[] result = new byte[length];
		//32 /2 = 16 [i,i+1]    [0,1] [2,3] [4,5] [6,7] 0*2 = 0  0+1=1 1
		for(int i = 0;i < length;i++) {
			//51 -->  0101 0001 
			result[i] = (byte)(HEX_STR.indexOf(array[i*2])<<4 | HEX_STR.indexOf(array[i*2+1]));
		}
		
		return result;
		
	}
	//字节数组转换16进制字符串
	private static String hexByteToString(byte[] hex) {
		
		StringBuffer sBuffer = new StringBuffer();
		for(int i = 0;i < hex.length;i++) {
			//	计算机对负数byte进行补码运算时前24位高位补1,为了保证数据一致性，&0xFF
			String tmp = Integer.toHexString(hex[i] & 0xFF);
			if(tmp.length() == 1) {
				tmp = "0"+tmp;
			}
			sBuffer.append(tmp.toUpperCase());
		}
		return sBuffer.toString();
	}
	/**
	 * 使用MD5加密password并返回字符串格式
	 * @param password
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static String getEncryptedPaasword(String password){
		
		//	创建消息摘要对象
		MessageDigest md = null;
		
		byte[] pwd = null;
		try {
			md = MessageDigest.getInstance("MD5");
			//	按照指定编码字节序列生成加密
			pwd = md.digest(password.getBytes("utf-8"));
			//	返回16进制字符串
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hexByteToString(pwd);
	}
	/**
	 * 通过UUID和时间戳创建Token
	 * @param uuid
	 * @param date
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 */
	public static String getToken(String uuid,Date date) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		
		//	创建消息摘要对象
		MessageDigest md = MessageDigest.getInstance("MD5");
		//	uuid+date最为生成token的消息摘要
		String key = uuid+date.toString();
		//	按照指定编码字节序列生成加密
		byte[] token = md.digest(key.getBytes("utf-8"));
		//	生成字符串
		return hexByteToString(token);
	}
	
	//验证密码
	public boolean validPassword(String password,String passwordInDB) {
		
		return Arrays.equals(hexStringToByte(getEncryptedPaasword(password)), hexStringToByte(passwordInDB));
	}
	
	@Test
	public void test() {
		try {
			getEncryptedPaasword("hello");
			System.out.println();
			String str = getToken(UUID.randomUUID().toString(), new Date());
			System.out.println(validPassword("hello", "5D41402ABC4B2A76B9719D911017C592"));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
