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
 * MD5(��ϢժҪ�㷨�����)Ϊ�������ȫ����㷺ʹ�õ�һ��ɢ�к���,�����ṩ��Ϣ�������Ա���
 * 
 * MD5�ص�
 * 	ѹ����:���ⳤ�ȵ�����,�����MD5ֵ���ȶ��ǹ̶���
 * 	���׼���:��ԭ�������MD5ֵ������
 * 	���޸���:��Ԫ���ݽ����κθĶ�������ֻ�޸�1���ֽڣ��õ���MD5���кܴ�����
 * 	ǿ����ײ:��֪ԭ���ݺ���MD5ֵ,���ҵ�һ��������ͬMD5ֵ�������Ƿǳ��ѵ�
 * 
 * @author Administrator
 *	MD5�����ֽ�����
 */
public class MD5Util {

	private static final String HEX_STR = "0123456789ABCDEF";
	
	//16�����ַ���ת���ֽ�����
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
	//�ֽ�����ת��16�����ַ���
	private static String hexByteToString(byte[] hex) {
		
		StringBuffer sBuffer = new StringBuffer();
		for(int i = 0;i < hex.length;i++) {
			//	������Ը���byte���в�������ʱǰ24λ��λ��1,Ϊ�˱�֤����һ���ԣ�&0xFF
			String tmp = Integer.toHexString(hex[i] & 0xFF);
			if(tmp.length() == 1) {
				tmp = "0"+tmp;
			}
			sBuffer.append(tmp.toUpperCase());
		}
		return sBuffer.toString();
	}
	/**
	 * ʹ��MD5����password�������ַ�����ʽ
	 * @param password
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static String getEncryptedPaasword(String password){
		
		//	������ϢժҪ����
		MessageDigest md = null;
		
		byte[] pwd = null;
		try {
			md = MessageDigest.getInstance("MD5");
			//	����ָ�������ֽ��������ɼ���
			pwd = md.digest(password.getBytes("utf-8"));
			//	����16�����ַ���
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
	 * ͨ��UUID��ʱ�������Token
	 * @param uuid
	 * @param date
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 */
	public static String getToken(String uuid,Date date) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		
		//	������ϢժҪ����
		MessageDigest md = MessageDigest.getInstance("MD5");
		//	uuid+date��Ϊ����token����ϢժҪ
		String key = uuid+date.toString();
		//	����ָ�������ֽ��������ɼ���
		byte[] token = md.digest(key.getBytes("utf-8"));
		//	�����ַ���
		return hexByteToString(token);
	}
	
	//��֤����
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
