import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author qfdk Serveur.java 2015年10月9日
 */
public class Serveur {

	public static final String WEB_ROOT = System.getProperty("user.dir") + File.separator + "wwwroot";
	private int PORT;
	private boolean run = true;

	/**
	 * point entre
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			new Serveur(8080).run();
		} catch (IOException e) {
			System.out.println(e);
			System.exit(-1);
		}
	}

	/**
	 * Le constucteur de classe Serveur.java
	 * @param port
	 */
	public Serveur(int port)
	{
		this.PORT=port;
	}
	/**
	 * @throws IOException
	 */
	public void run() throws IOException {
		System.out.println("[+] Host: localhost");
		System.out.println("[+] Port: " + PORT);
		System.out.println("[+] Racine: " + WEB_ROOT);
		System.out.println("[+] Exp: http://localhost:" + PORT + "/index.html");
		// creation du socket
		ServerSocketChannel ssc = ServerSocketChannel.open();
		ssc.socket().bind(new InetSocketAddress(PORT));
		// attend le client
		while (run) {
			SocketChannel client = ssc.accept();
			ByteBuffer buffer = ByteBuffer.allocate(8192);
			// lire des info client
			client.read(buffer);
			buffer.flip();
			String request = new String(buffer.array(), 0, buffer.limit(), "UTF-8");
			String firstLineOfRequest = request.substring(0, request.indexOf("\r\n"));
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			System.out.println("[" + df.format(new Date()) + "] " + firstLineOfRequest);
			// System.out.println(request);
			File file = null;
			String fileName = url2File(firstLineOfRequest);

			if (fileName.equals("/")) {
				// page par default
				file = new File(WEB_ROOT, "index.html");
			} else {
				file = new File(WEB_ROOT, fileName);
			}

			try {
				String donnees = readFichier(file);
				StringBuffer sb = new StringBuffer("HTTP/1.1 200 OK\r\n");

				if(getFileExtension(fileName).equals("css"))
				{
					sb.append("Content-Type:text/css\r\n\r\n").append(donnees);
				}else{
					sb.append("Content-Type:text/html\r\n\r\n").append(donnees);
				}
				// envoie des donnees au client
				client.write(string2ByteBuffer(sb.toString()));
			} catch (FileNotFoundException e) {
				String errorMessage = "HTTP/1.1 404 File Not Found\r\n" + "ContentType: text/html\r\n"
						+ "ContentLength: 23\r\n\r\n" + "<html><h1>File Not Found</h1></html>";
				client.write(string2ByteBuffer(errorMessage));
			}
			// fermer la connextion
			client.close();
		}

	}

	/**
	 * la metode pour arrter le serveur
	 */
	public void stop() {
		this.run = false;
	}

	/**
	 * lire un fichier
	 *
	 * @param file un fichier
	 * @return le text
	 * @throws IOException
	 */
	public String readFichier(File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		byte data[] = new byte[fis.available()];
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		fis.read(data);
		out.write(data);
		fis.close();
		return out.toString();
	}

	/**
	 * converter GET /index.htm HTTP 1.1 to index.htm
	 *
	 * @param requestString
	 *            GET /index.htm HTTP 1.1
	 * @return nom du fichier
	 */
	public String url2File(String requestString) {
		int index1, index2;
		index1 = requestString.indexOf(' ');
		if (index1 != -1) {
			index2 = requestString.indexOf(' ', index1 + 1);
			if (index2 > index1) {
				return requestString.substring(index1 + 1, index2);
			}
		}
		return null;
	}

	/**
	 * converter le forma du donnees a envoyer
	 *
	 * @param str le text
	 * @return une donnee a envoyer
	 */
	public ByteBuffer string2ByteBuffer(String str) {
		Charset encoder = Charset.forName("UTF-8");
		return encoder.encode(CharBuffer.wrap(str));
	}

	/**
	 * obtenir l'extentnion du fichier
	 * @param name nom du fichier
	 * @return extentions du fichier
	 */
	public String getFileExtension(String name) {
		return name.substring(name.lastIndexOf(".") + 1);
	}

}
