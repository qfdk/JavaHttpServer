import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class ClientHttp {
	public static void main(String[] args) throws IOException {
		if(args.length<1)
		{
			System.out.println("Usage : java ClientHttp url");
		}else
		{
			URL url=new URL(args[0]);
			String path=url.getPath();
			String host=url.getHost();
			int port =url.getPort();

			InetSocketAddress address=new InetSocketAddress(host,port);
			SocketChannel socketChannel=SocketChannel.open(address);

			String request = "GET "
			+ path
			+ "HTTP/1.1\r\n"
			+ "User-Agent: Safari/601.1.56\r\n"
			+ "Accept: text/*\r\n"
			+ "Connection: keep-alive\r\n"
			+ "Host: " + host + "\r\n" + "\r\n";

			Charset charset=Charset.forName("UTF-8");
			socketChannel.write(charset.encode(request));

			ByteBuffer buffer = ByteBuffer.allocate(8192);

			while(socketChannel.read(buffer)!=-1)
			{
				buffer.flip();
				System.out.println(new String(buffer.array(),0,buffer.limit(),"UTF-8"));
			}
			socketChannel.close();
		}
	}
}
