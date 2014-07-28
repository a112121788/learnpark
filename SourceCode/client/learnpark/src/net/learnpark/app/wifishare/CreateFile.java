package net.learnpark.app.wifishare;

import java.io.File;
import java.io.UnsupportedEncodingException;

//import java.io.IOException;

public class CreateFile {

	/**
	 * 
	 * @param destDirName
	 *            文件路径与目录名称
	 * @return true 创建目录成功 false 创建目录失败
	 */
	public static boolean createDir(String destDirName) {
		File dir = new File(destDirName);
		if (dir.exists()) {
			return true;
		}
		if (!destDirName.endsWith(File.separator))
			destDirName = destDirName + File.separator;
		if (dir.mkdirs()) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * public static boolean createIndex(String destFileName) { File file = new
	 * File(destFileName); if (file.exists()) { return true; } // 目标不能是目录 if
	 * (destFileName.endsWith(File.separator)) { return false; } // 目标文件所在路径不存在
	 * if (!file.getParentFile().exists()) { return false; }
	 * 
	 * // 创建目标文件 try { if (file.createNewFile()) { FileWriter fw = new
	 * FileWriter(file);//以字符流的方式写入文件，不带缓冲区 BufferedWriter bw = new
	 * BufferedWriter(fw); bw.write("<html><title>Mobile Server</title>" +
	 * "<body><h1>Mobile Http Server Run Well!</h1></body>" + "</html>");
	 * bw.close(); return true; } else { return false; } } catch (IOException e)
	 * { return false; } }
	 */
	public static String getFileList(String dirName) {
		StringBuilder sb = new StringBuilder();
		sb.append("<html>");
		sb.append("<title>Share File List</title>");
		sb.append("<body>");
		File fileList = new File(dirName);
		if (fileList.isDirectory()) {
			String[] fl = fileList.list();
			if (fl != null) {
				sb.append("<h1>File List</h1><br/>");
				sb.append("<ul>");
				for (String tf : fl) {
					try {
						byte[] bt;
						bt = tf.getBytes("UTF-8");
						sb.append("<li><a href=\"" + new String(bt, "UTF-8")
								+ "\">" + new String(bt, "UTF-8") + "</a></li>");
					} catch (UnsupportedEncodingException e) {
					}
				}
				sb.append("</ul>");
			} else {
				sb.append("<h1>No File!</h1>");
			}
			sb.append("<br /><form method=\"post\" action=\"index.html\" enctype=\"multipart/form-data\">"
					+ "<input type=\"file\" name=\"fn\"/>"
					+ "<input type=\"submit\" value=\"Upload\"/>" + "</form>");
		} else {
			sb.append("<h1>Web Root Directory Is Not Create!</h1>");
		}

		sb.append("</body>");
		sb.append("</html>");
		return sb.toString();
	}
}
