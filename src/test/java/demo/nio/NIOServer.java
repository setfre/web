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
	//定义选择器
	private Selector selector;
	//定义线程池
	public ExecutorService tp = Executors.newCachedThreadPool();
	//定义客户端连接容器
	private static Map<Socket, Long> time_stat = new HashMap<Socket, Long>(10240);
	
	public static void main(String[] args) throws Exception{
		
		EchoClient ec = new EchoClient();
		NIOServer nioServer = new NIOServer();
		
		nioServer.startServer();

	}
	
	public void startServer() throws Exception{
		//选择器和可选择通道的服务提供者类。 
		selector = SelectorProvider.provider().openSelector();
		//表示服务器端实例
		ServerSocketChannel ssc = ServerSocketChannel.open();
		//设置非阻塞模式
		ssc.configureBlocking(false);
		//服务器监听端口
		InetSocketAddress isa = new InetSocketAddress(8080);
		//操作集位用于插座接受操作。 
		SelectionKey acceptKey = ssc.register(selector, SelectionKey.OP_ACCEPT);
		
		for(;;){//等待--分发网络消息
			selector.select();
			//获取准备好的Channel集合
			Set<SelectionKey> readyKeys = selector.selectedKeys();
			//获取迭代元素集合
			Iterator<SelectionKey> i = readyKeys.iterator();
			//服务器端处理一个客户端耗时
			long e = 0;
			//迭代连接通道
			while (i.hasNext()) {
				
				SelectionKey sk = (SelectionKey) i.next();
				//每次处理一个SelectionKey就将其从容器中移除，避免重复处理
				i.remove();
				
				if(sk.isAcceptable()){//判断当前Channel是否处于可接受状态
					//执行客户端接收
					doAccept(sk);
				}
				else if(sk.isValid() && sk.isReadable()){//可读
					if(!time_stat.containsKey(((SocketChannel)sk.channel()).socket())){
						time_stat.put(((SocketChannel)sk.channel()).socket(), 
								System.currentTimeMillis());
					}
					//从客户端读取
					doRead(sk);
				}
				else if(sk.isValid() && sk.isWritable()){//可写
					//向客户端写入
					doWrite(sk);
					//记录时间
					e = System.currentTimeMillis();
					//从容器中移除此连接
					long b = time_stat.remove(((SocketChannel)sk.channel()).socket());
					System.out.println("spend:"+(e-b)+"ms");
				}
			}
		}
	}

	private void doWrite(SelectionKey sk) {
		//获得通道引用
		SocketChannel channel = (SocketChannel) sk.channel();
		//获取此通道绑定的附件
		EchoClient echoClient = (EchoClient) sk.attachment();
		//从附件中读取客户端请求消息
		LinkedList<ByteBuffer> outq = echoClient.getOutputQueue();
		
		ByteBuffer bb = outq.getLast();
		try{
			//向客户端发送消息
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
		//缓冲区字节为8k
		ByteBuffer bb = ByteBuffer.allocate(1024*8);
		int len;
		try{
			len = channel.read(bb);
			if(len < 0){//读取结束，连接已断开
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
		//获得通道引用
		ServerSocketChannel server = (ServerSocketChannel) sk.channel();
		SocketChannel clientChannel;
		try{
			clientChannel = server.accept();
			clientChannel.configureBlocking(false);
			//通知选择器对读操作感兴趣
			SelectionKey clientKey = clientChannel.register(selector, SelectionKey.OP_READ);
			//创建一个客户端代理实例
			EchoClient echoClient = new EchoClient();
			//将此实例作为附件附加到此连接的SelectionKey，这样在此连接中就可以共享
			clientKey.attach(echoClient);
			//获得客户端地址
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
			//强迫selector立即返回
			selector.wakeup();
		}
	}
	//封装一个队列，保存在需要回复给这个客户端的所有信息。
	public static class EchoClient{
		//客户端发送数据缓冲区
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

