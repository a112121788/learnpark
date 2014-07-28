package net.learnpark.app.teacher.learnpark.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import net.learnpark.app.teacher.learnpark.shixian.Course;

import android.os.Environment;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class XmlUtil {

	private static XStream xstream;
	
	//对xml的结构进行设置
	static {
		xstream=new XStream(new DomDriver());
		xstream.alias("courses", Course.class);
		xstream.aliasAttribute(Course.class, "id", "id");		
	}
	
	//导出课程表用到的函数
	public Boolean ExportCourse(List<Course> course){
		List<Course> mcourse;
		mcourse=course;
		String str=Environment.getExternalStorageDirectory().getPath()+"/learnpark/download/courses/Courses_01.xml";
		File dir=new File(Environment.getExternalStorageDirectory().getPath()+"/learnpark/download/courses");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		try {
			PrintWriter pw=new PrintWriter(str, "UTF-8");
			xstream.toXML(mcourse, pw);
		} catch (FileNotFoundException e) {	
			e.printStackTrace();
			return false;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	//导入课程表用到的函数
	@SuppressWarnings("unchecked")
	public List<Course> ImportCourse(String filepath){
		List<Course> mcourse=new ArrayList<Course>();
		try {
			InputStream in=new FileInputStream(filepath);
			mcourse=(List<Course>) xstream.fromXML(in);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		//Log.d("TAG",mcourse.size()+"   "+88);
		return mcourse;
	}
	
	
}
