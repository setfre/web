package demo.nio;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.LockSupport;

public class HeavySocketClient {

	private static ExecutorService tp = Executors.newCachedThreadPool();
	//�ȴ�ʱ��
	private static final int sleep_time = 1000 * 1000 * 1000;
	
	public static void main(String[] args){
		
		EchoClient ec = new EchoClient();
		//�̳߳ش���10���߳������������
		for(int i = 0;i < 10;i++){
			tp.execute(ec);
		}
	}
	
	public static class EchoClient implements Runnable{
		public void run(){
			Socket clint = null;
			PrintWriter writer = null;
			BufferedReader reader = null;
			try {
				clint = new Socket();
				//��8080�˿ڷ�����������
				clint.connect(new InetSocketAddress("localhost",8080));
				
				writer = new PrintWriter(clint.getOutputStream(),true);
				writer.print("H");
				LockSupport.parkNanos(sleep_time);
				writer.print("L");
				LockSupport.parkNanos(sleep_time);
				writer.print("W");
				LockSupport.parkNanos(sleep_time);
				writer.print("O");
				LockSupport.parkNanos(sleep_time);
				writer.print("D");
				LockSupport.parkNanos(sleep_time);
				writer.println();
				writer.flush();
				//���շ�������������
				reader = new BufferedReader(new InputStreamReader(clint.getInputStream()));
				System.out.println("from server:" + reader.readLine());
			} catch (Exception e) {
				e.printStackTrace();
			} finally{
				try {
					if(writer != null)
						writer.close();
					if(reader != null)
						reader.close();
					if(clint != null)
						clint.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
