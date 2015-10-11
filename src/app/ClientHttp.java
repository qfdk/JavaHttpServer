package app;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * @author qfdk
 * ClientHttp.java
 * 2015年10月9日
 */
public class ClientHttp {
	 /**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException { 
		 if(args.length<2)
		 {
			 System.out.println("Usage : java ClientHttp url");
			 System.exit(-1);
		 }
		 URL url=new URL(args[0]);
		 String path=url.getPath();
		 String host=url.getHost();
		 int port = url.getPort();
		 Charset charset=Charset.forName("UTF-8"); 
		 
		 InetSocketAddress address=new InetSocketAddress(host,port);
		 SocketChannel socketChannel=SocketChannel.open(address);

		 String request = "GET "
		 + path +"\r\n"
		 + "HTTP/1.1\r\n" 
		 + "User-Agent: HTTPGrab\r\n"
	     + "Accept: text/*\r\n" 
		 + "Connection: keep-alive\r\n" 
	     + "Host: " + host + "\r\n" + "\r\n";
		 
	     socketChannel.write(charset.encode(request));
		 ByteBuffer buffer = ByteBuffer.allocate(8192);
		 
		 while(socketChannel.read(buffer)!=-1)
		 {
			 buffer.flip();
			 System.out.print(new String(buffer.array(),0,buffer.limit(),"UTF-8"));
		 }
		 socketChannel.close();
	}
}
