package net.learnpark.app.learnpark.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件工具类 包括文件的遍历等
 * 
 * @author peng
 */
public class FileUtil {
	private ArrayList<String> filenames = new ArrayList<String>();

	/**
	 * 根据传入的文件目录和格式返回文件的绝对路径
	 * 
	 * @param path
	 *            文件目录
	 * @param postfix
	 *            文件后缀，用于简单的判断文件的类型，格式 mp4（不是 .mp4）
	 * @return 返回一个对应个格式文件的list
	 */
	public static List<String> findFilesBYType(String path, String postfix) {
		List<String> list = new ArrayList<String>();

		File home = new File(path);
		if (!home.exists()) {
			return null;
		}
		if (home.listFiles(new FileNameFilterImp(postfix)).length > 0) {
			for (File file : home.listFiles(new FileNameFilterImp(postfix))) {
				list.add(file.getAbsolutePath());
			}
		}
		return list;
	}

	/**
	 * 根据传入的文件目录返回文件的绝对路径
	 * 
	 * @param path
	 *            文件目录
	 * @return 返回一个对应个格式文件的list
	 */
	public static List<String> findFiles(String path) {
		List<String> list = new ArrayList<String>();

		File home = new File(path);
		if (!home.exists()) {
			home.mkdirs();
			return null;
		}
		if (home.listFiles() != null) {
			for (File file : home.listFiles()) {
				list.add(file.getAbsolutePath());
			}
		}
		return list;
	}

	public ArrayList<String> getFilenames() {
		return filenames;
	}

	public List<String> findAllFilesBYType(String path, String postfix)
			throws Exception {

		File root = new File(path);
		if (root != null) {
			showAllFiles(root, postfix);
		}
		return filenames;
	}

	private void showAllFiles(File dir, String postfix) throws Exception {
		File[] fs = dir.listFiles();
		for (int i = 0; i < fs.length; i++) {
			if (fs[i].isFile()) {
				if (fileEndWith(fs[i].toString(), postfix)) {
					System.out.println(fs[i].getAbsolutePath());
					filenames.add(fs[i].getAbsolutePath());
				}
			}
			if (fs[i].isDirectory()) {
				try {
					showAllFiles(fs[i], postfix);
				} catch (Exception e) {
				}
			}
		}
	}

	private boolean fileEndWith(String filename, String postfix) {
		return filename.endsWith(postfix);
	}

	/**
	 * 把一个字符串保存到一个txt文件
	 * 
	 * @param str
	 *            demo "aaa"
	 * @param path
	 *            demo "/1.txt"
	 */
	public static void str2File(String str, String path) {

		try {
			File f = new File(path);
			if (!f.exists()) {
				f.createNewFile();
			} else {
				f.delete();
				f.createNewFile();
			}
			FileWriter fw = new FileWriter(f);
			PrintWriter pw = new PrintWriter(fw);
			pw.append(str);

			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String file2Stt(String path) {
		BufferedReader in;
		String str = "";
		try {
			in = new BufferedReader(new FileReader(path));

			String s = in.readLine();
			while (s != null) {
				str = str + s;
				s = in.readLine();

			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return str;
	}
}

class FileNameFilterImp implements FilenameFilter {
	String postfix;

	public FileNameFilterImp(String postfix) {
		this.postfix = postfix;
	}

	@Override
	public boolean accept(File dir, String filename) {
		// return (filename.endsWith("." + postfix)||filename.endsWith("." +
		// "3gp"));
		return (filename.endsWith("." + postfix));
	}
}