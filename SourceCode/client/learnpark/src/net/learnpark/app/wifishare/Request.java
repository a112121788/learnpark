package net.learnpark.app.wifishare;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;

public class Request {
	private InputStream input;
	private String uri;
	private String DEFAULT_WEB = "/index.html";
	private String WEB_ROOT;
	
	private static final int BUFFERED = 2048;
	
	private boolean isPost ;

	public Request(InputStream input,String web_root) {
		this.input = input;
		this.WEB_ROOT = web_root;
		this.isPost = false;
	}

	public void parse() {
		// Read a set of characters from the socket
		StringBuilder request = new StringBuilder(BUFFERED);
		int i;
		byte[] buffer = new byte[BUFFERED];
		try {
			i = input.read(buffer);
		} catch (IOException e) {
			i = -1;
		}
		for (int j = 0; j < i; j++) {
			request.append((char) buffer[j]);
		}
		uri = parseUri(request.toString());
		if(isPost){
			FileUpload(request.toString(),buffer);
		}
	}

	private String parseUri(String requestString) {
		int index1, index2;
		index1 = requestString.indexOf(' ');
		if("POST".equals(requestString.substring(0, index1))){
			isPost = true;
		}
		if (index1 != -1) {
			index2 = requestString.indexOf(' ', index1 + 1);
			if (index2 > index1){
				String t_uri = requestString.substring(index1 + 1, index2);
				int loc = t_uri.indexOf('?');
				if(loc != -1){
					t_uri = t_uri.substring(0, loc);
				}
				if("/".equals(t_uri)){
					t_uri = DEFAULT_WEB;
				}
				return t_uri;
			}
		}
		return null;
	}

	public String getUri() {
		return uri;
	}
	
	public String getHeader(String requestString,String option){
		int pos_s = requestString.indexOf(option);
		int pos_e = requestString.indexOf("\r\n", pos_s);
		String strOpt = requestString.substring(pos_s, pos_e + 2);
		return strOpt;
	}
	public boolean FileUpload(String requestString,byte[] requestBytes){
		String opt = this.getHeader(requestString.toString(), "Content-Type");
		//sure upload
		if(opt.indexOf("multipart/form-data") != -1){
			int index1 = opt.indexOf("boundary=");
			int index2 = opt.indexOf("\r\n"); 
			String boundary_s = "--" + opt.substring(index1 + "boundary=".length(), index2);
			String boundary_e = "\r\n" + boundary_s + "--";
			try {
				boundary_e = new String(boundary_e.getBytes(),"US-ASCII");
			} catch (Exception e1) {}
			//start to boundary
			index1 = requestString.toString().indexOf(boundary_s);
			index1 = requestString.toString().indexOf("filename=\"", index1);
			index2 = requestString.toString().indexOf("\"\r\n", index1);
			if(index1 == -1){
				return false;
			}
			//china encode have problem
			//String fileName = requestString.toString().substring(index1 + "filename=\"".length(), index2);
			int len_s = index1 + "filename=\"".length();
			int len = index2 - len_s;
			byte[] fileNameByte = new byte[len];
			for(int i=0 ; i<len ; i++){
				fileNameByte[i] = requestBytes[i + len_s];
			}
			byte[] buffer = new byte[BUFFERED];
			buffer = requestBytes;
			index1 = requestString.indexOf("\r\n\r\n", index2);
			try {
				File fout = new File(WEB_ROOT + File.separator + new String(fileNameByte));
				FileOutputStream fos = new FileOutputStream(fout);
				BufferedOutputStream bos = new BufferedOutputStream(fos);
				
				index2 = requestString.indexOf(boundary_e);
				if(index2 != -1){
					bos.write(buffer, index1+4,  index2 - index1 - 4);
					bos.close();
					return true;
				}
				
				bos.write(buffer, index1+4,  BUFFERED - index1 - 4);
				String tempB = null;
				int i = input.read(buffer);
				while(i != -1){
					tempB = new String(buffer,"US-ASCII");
					index1 = tempB.indexOf(boundary_e);
					//index1 = tempB.indexOf("\r\n--");
					if(index1 != -1){
						//read to the end
						bos.write(buffer, 0, index1);
						break;
					}else{
						bos.write(buffer, 0,  i);
					}
					i = input.read(buffer);
				}
				
				bos.close();
			} catch (Exception e) {	}
		}
		return true;
	}
}