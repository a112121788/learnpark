package net.learnpark.app.teacher.learnpark.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;

public class ByteUtil {

	//这里可以用一个接口来写
	public static byte[] inputStream2Byte(HttpResponse response)
			throws IOException {
		InputStream is = response.getEntity().getContent();
		int len;
		byte[] buffer = new byte[1024];
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		while ((len = is.read(buffer)) > 0) {
			bos.write(buffer, 0, len);
		}
		return bos.toByteArray();
	}
	
	
}
