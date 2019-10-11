package demo.nio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class NIOServer {
	//����ѡ����
	private Selector selector;
	//�����̳߳�
	public ExecutorService tp = Executors.newCachedThreadPool();
	//����ͻ�����������
	private static Map<Socket, Long> time_stat = new HashMap<Socket, Long>(10240);
	
	public static void main(String[] args) throws Exception{
		
		EchoClient ec = new EchoClient();
		NIOServer nioServer = new NIOServer();
		
		nioServer.startServer();

	}
	
	public void startServer() throws Exception{
		//ѡ�����Ϳ�ѡ��ͨ���ķ����ṩ���ࡣ 
		selector = SelectorProvider.provider().openSelector();
		//��ʾ��������ʵ��
		ServerSocketChannel ssc = ServerSocketChannel.open();
		//���÷�����ģʽ
		ssc.configureBlocking(false);
		//�����������˿�
		InetSocketAddress isa = new InetSocketAddress(8080);
		//������λ���ڲ������ܲ����� 
		SelectionKey acceptKey = ssc.register(selector, SelectionKey.OP_ACCEPT);
		
		for(;;){//�ȴ�--�ַ�������Ϣ
			selector.select();
			//��ȡ׼���õ�Channel����
			Set<SelectionKey> readyKeys = selector.selectedKeys();
			//��ȡ����Ԫ�ؼ���
			Iterator<SelectionKey> i = readyKeys.iterator();
			//�������˴���һ���ͻ��˺�ʱ
			long e = 0;
			//��������ͨ��
			while (i.hasNext()) {
				
				SelectionKey sk = (SelectionKey) i.next();
				//ÿ�δ���һ��SelectionKey�ͽ�����������Ƴ��������ظ�����
				i.remove();
				
				if(sk.isAcceptable()){//�жϵ�ǰChannel�Ƿ��ڿɽ���״̬
					//ִ�пͻ��˽���
					doAccept(sk);
				}
				else if(sk.isValid() && sk.isReadable()){//�ɶ�
					if(!time_stat.containsKey(((SocketChannel)sk.channel()).socket())){
						time_stat.put(((SocketChannel)sk.channel()).socket(), 
								System.currentTimeMillis());
					}
					//�ӿͻ��˶�ȡ
					doRead(sk);
				}
				else if(sk.isValid() && sk.isWritable()){//��д
					//��ͻ���д��
					doWrite(sk);
					//��¼ʱ��
					e = System.currentTimeMillis();
					//���������Ƴ�������
					long b = time_stat.remove(((SocketChannel)sk.channel()).socket());
					System.out.println("spend:"+(e-b)+"ms");
				}
			}
		}
	}

	private void doWrite(SelectionKey sk) {
		//���ͨ������
		SocketChannel channel = (SocketChannel) sk.channel();
		//��ȡ��ͨ���󶨵ĸ���
		EchoClient echoClient = (EchoClient) sk.attachment();
		//�Ӹ����ж�ȡ�ͻ���������Ϣ
		LinkedList<ByteBuffer> outq = echoClient.getOutputQueue();
		
		ByteBuffer bb = outq.getLast();
		try{
			//��ͻ��˷�����Ϣ
			int len = channel.write(bb);
			if(len == -1){
				disconnect(sk);
				return;
			}
			
			if(bb.remaining() == 0){
				
				outq.removeLast();
			}
		} catch (Exception e) {
			System.out.println("Failed to write to client");
			e.printStackTrace();
			disconnect(sk);
		}
		if(outq.size() == 0){
			sk.interestOps(SelectionKey.OP_READ);
		}
	}

	private void doRead(SelectionKey sk) {
		
		SocketChannel channel = (SocketChannel) sk.channel();
		//�������ֽ�Ϊ8k
		ByteBuffer bb = ByteBuffer.allocate(1024*8);
		int len;
		try{
			len = channel.read(bb);
			if(len < 0){//��ȡ�����������ѶϿ�
				disconnect(sk);
				return;
			}
		} catch (Exception e) {
			System.out.println("Failed to accept new client.");
			e.printStackTrace();
			disconnect(sk);
			return;
		}
		bb.flip();
		tp.execute(new HandleMsg(sk,bb));
	}

	private void disconnect(SelectionKey sk) {
		// TODO Auto-generated method stub
		SocketChannel channel = (SocketChannel) sk.channel();
		try {
			channel.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void doAccept(SelectionKey sk) {
		//���ͨ������
		ServerSocketChannel server = (ServerSocketChannel) sk.channel();
		SocketChannel clientChannel;
		try{
			clientChannel = server.accept();
			clientChannel.configureBlocking(false);
			//֪ͨѡ�����Զ���������Ȥ
			SelectionKey clientKey = clientChannel.register(selector, SelectionKey.OP_READ);
			//����һ���ͻ��˴���ʵ��
			EchoClient echoClient = new EchoClient();
			//����ʵ����Ϊ�������ӵ������ӵ�SelectionKey�������ڴ������оͿ��Թ���
			clientKey.attach(echoClient);
			//��ÿͻ��˵�ַ
			InetAddress clientAddress = clientChannel.socket().getInetAddress();
			
			System.out.println("accepted connection from "+clientAddress.getHostAddress()+".");
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Failed to accept new client.");
			e.printStackTrace();
		}
	}
	
	class HandleMsg implements Runnable{

		private SelectionKey sk;
		private ByteBuffer bb;
		
		public HandleMsg(SelectionKey sk, ByteBuffer bb) {
			this.sk = sk;
			this.bb = bb;
		}

		public void run() {
			EchoClient echoClient = (EchoClient)sk.attachment();
			echoClient.enquene(bb);
			sk.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
			//ǿ��selector��������
			selector.wakeup();
		}
	}
	//��װһ�����У���������Ҫ�ظ�������ͻ��˵�������Ϣ��
	public static class EchoClient{
		//�ͻ��˷������ݻ�����
		private LinkedList<ByteBuffer> outq;
		
		public EchoClient() {
			outq = new LinkedList<ByteBuffer>();
		}

		public LinkedList<ByteBuffer> getOutputQueue() {
			return outq;
		}

		public void enquene(ByteBuffer bb){
			outq.addFirst(bb);
		}
	}
}

