package net.learnpark.app.wifishare;

import java.net.Socket;
import java.net.ServerSocket;
import java.net.InetAddress;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

public class HttpServer {

	/**
	 * WEB_ROOT is the directory where our HTML and other files reside. For this
	 * package, WEB_ROOT is the "webroot" directory under the working directory.
	 * The working directory is the location in the file system from where the
	 * java command was invoked.
	 */
	
	public ServerSocket serverSocket = null;
	
	private String WEB_ROOT;
	private String HOST_ADDR;
	
	private boolean shutdown = false;

	public HttpServer(String web_root,String host_addr) {
		this.WEB_ROOT = web_root;
		this.HOST_ADDR = host_addr;
	}
	public void await() {
		
		//default port
		int port = 3693;
		
		try {
			serverSocket = new ServerSocket(port, 1,InetAddress.getByName(HOST_ADDR));
		} catch (IOException e) {
		}
		// Loop waiting for a request
		while (!shutdown) {
			try {
				Socket socket = null;
				socket = serverSocket.accept();
				new Thread(new ThreadHandler(socket)).start();
			} catch (IOException e) {
				shutdown = true;
			}
		}
	}
	public class ThreadHandler implements Runnable{
		
		private Socket socket;
		
		public ThreadHandler(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			InputStream input = null;
			OutputStream output = null;

			try {
				input = socket.getInputStream();
				output = socket.getOutputStream();
				// create Request object and parse
				Request request = new Request(input,WEB_ROOT);
				request.parse();

				// create Response object
				Response response = new Response(output,WEB_ROOT);
				response.setRequest(request);
				response.sendStaticResource();

				// Close the socket
				socket.close();
				
			} catch (Exception e) {	}
			
		}
		
	}
}
