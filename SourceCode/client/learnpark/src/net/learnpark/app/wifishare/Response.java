package net.learnpark.app.wifishare;

import java.io.OutputStream;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.File;

/* 
 HTTP Response = Status-Line 
 *(( general-header | response-header | entity-header ) CRLF) 
 CRLF 
 [ message-body ] 
 Status-Line = HTTP-Version SP Status-Code SP Reason-Phrase CRLF 
 */

public class Response {

	private String WEB_ROOT;

	private static final int BUFFER_SIZE = 1024;
	Request request;
	OutputStream output;

	public Response(OutputStream output, String web_root) {
		this.output = output;
		this.WEB_ROOT = web_root;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	public void sendStaticResource() throws IOException {
		byte[] bytes = new byte[BUFFER_SIZE];
		FileInputStream fis = null;
		try {
			String header = "HTTP/1.1 200 OK\r\n"
					+ "Server: EyeTHS\r\n";
			if ("/index.html".equals(request.getUri())) {
				header = header + "Content-Type: text/html\r\n";
				String fileList = CreateFile.getFileList(WEB_ROOT);
				header = header + "Content-Length: " + fileList.length()
						+ "\r\n" + "\r\n";
				header = header + fileList;
				output.write(header.getBytes());
			} else {
				File file = new File(WEB_ROOT, request.getUri());
				if (file.exists()) {
					fis = new FileInputStream(file);
					/**
					 * HTTP/1.1 200 OK Server: EyeTHS Conten-Type: text/html
					 * ContenT-Length:
					 */
					if (request.getUri().indexOf(".html") != -1
							|| request.getUri().indexOf(".htm") != -1) {
						header = header + "Content-Type: text/html\r\n";
					} else {
						header = header
								+ "Content-Type: application/octet-stream\r\n";
					}
					header = header + "Content-Length: " + fis.available()
							+ "\r\n" + "\r\n";
					output.write(header.getBytes(), 0, header.length());
					int ch = fis.read(bytes, 0, BUFFER_SIZE);
					while (ch != -1) {
						output.write(bytes, 0, ch);
						ch = fis.read(bytes, 0, BUFFER_SIZE);
					}
				} else {
					// file not found
					String errorMessage = "HTTP/1.1 404 File Not Found\r\n"
							+ "Content-Type: text/html\r\n"
							+ "Content-Length: 20\r\n" + "\r\n"
							+ "<h1>Error 404 !</h1>";
					output.write(errorMessage.getBytes());
				}
			}
		} catch (Exception e) {

		} finally {
			if (fis != null)
				fis.close();
		}
	}
}