package psn.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Panel;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class VerifyCodeUtil {

	public static final String VERIFY_CODES = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	//��֤��-->ͼ��-->�����
	public static String GenerateVerifyCode(int verifySize){
		return GenerateVerifyCode(verifySize, VERIFY_CODES);
	}
	
	public static String GenerateVerifyCode(int verifySize,String sources){
		if(sources == null || sources.length() == 0){
			sources = VERIFY_CODES;
		}
		Random random = new Random();
		int length = sources.length();
		StringBuffer buffer = new StringBuffer();
		for(int i = 0;i < verifySize;i++){
			buffer = buffer.append(sources.charAt(random.nextInt(length-1)));
		}
		return buffer.toString();
	}
	
	public static void outputVerifyImg(int width,int height,String verifyCode,OutputStream outputStream) throws IOException{
		
		ImageIO.write(GenerateVerifyImg(width,height,verifyCode), "jpeg", outputStream);
	}
	
	public static BufferedImage GenerateVerifyImg(int width,int height,String verifyCode){
		
		//����λ�ڻ����е�ͼ��
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		//����ͼ�������ĵĶ���
		Graphics g = image.getGraphics();
		
		Random random = new Random();
		//����ͼ�񱳾�ɫ
		g.setColor(getRandomColor(200,250));
		//�����Σ�����(0,0)
		g.fillRect(0, 0, width, height);
		//�趨�����ʽ
		g.setFont(new Font("Times New Random", Font.PLAIN, 18));
		//�趨������ɫ
		g.setColor(getRandomColor(160, 200));
		//����130�����������
		for(int i = 0;i < 130;i++){
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int x1 = random.nextInt(12);
			int y1 = random.nextInt(12);
			g.drawLine(x, y, x+x1, y+y1);
		}
		
		int codeSize = verifyCode.length();
		
		for(int i =0;i<codeSize;i++){ 
			//��ͬ��ɫ����֤��
			g.setColor(new Color(15+random.nextInt(120),15+random.nextInt(120),15+random.nextInt(120)));
			
			//����֤�����λ���ͼ����
			g.drawString(String.valueOf(verifyCode.charAt(i)), 13*i+6, 16);
		}
		//�ͷŴ�ͼ���������
		g.dispose();
		
		return image;
	}

	private static Color getRandomColor(int i, int j) {
		// TODO Auto-generated method stub
		Random random = new Random();
		Color randomColor = null;
		if(i > 255) i = 255;
		if(j > 255) j = 255;
		//����0~255֮��������ɫֵ
		int r = i + random.nextInt(j-i);
		int g = i + random.nextInt(j-i);
		int b = i + random.nextInt(j-i);
		randomColor = new Color(r,g,b);
		return randomColor;
	}
	
	public static void main(String[] args){
		JFrame jFrame = new JFrame();
		jFrame.add(new ImagePanel()); 
		jFrame.setVisible(true);
		jFrame.setSize(500, 300);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
class ImagePanel extends JPanel{

	public void paintComponent(Graphics g){
		String verifyCode = VerifyCodeUtil.GenerateVerifyCode(5);
		g.drawImage(VerifyCodeUtil.GenerateVerifyImg(60, 20, verifyCode),0,0,null);
	}
}
