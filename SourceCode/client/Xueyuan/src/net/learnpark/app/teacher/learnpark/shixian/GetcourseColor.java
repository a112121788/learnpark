package net.learnpark.app.teacher.learnpark.shixian;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;

public class GetcourseColor {


	private int[] color = { Color.parseColor("#FFF8DC"), Color.parseColor("#FFD39B"),
			Color.parseColor("#FFF0F5"), Color.parseColor("#F0F8FF"),
			Color.parseColor("#EEE8CD"), Color.parseColor("#EEE8AA"),
			Color.parseColor("#E0FFFF"), Color.parseColor("#CDC673"),
			Color.parseColor("#CDB7B5"), Color.parseColor("#CD9B9B"),
			Color.parseColor("#C1CDCD"), Color.parseColor("#B0E2FF"),
			Color.parseColor("#8FBC8F"), Color.parseColor("#6CA6CD"),
			Color.parseColor("#6495ED") };
	private List<String> course=new ArrayList<String>();
	
	
	public List<String> getCourse() {
		return course;
	}
	public void setCourse(List<String> course) {
		this.course = course;
	}
	public int[] getColor() {
		return color;
	}
	public void setColor(int[] color) {
		this.color = color;
	}
	
	public List<String> addcourse(List<String> bcourse, String mcourse){
		this.course=bcourse;
		course.add(mcourse);
		return course;
	}
	
	public List<String> initcourse(List<String> bcourse){
		this.course=bcourse;
		for (int i = 0; i < course.size() - 1; i++) { // 循环遍历集体中的元素
			for (int j = course.size() - 1; j > i; j--) { // 这里非常巧妙，这里是倒序的是比较
				if (course.get(j).equals(course.get(i))) {
					course.remove(j);
				}
			}
		}
		return course;
	}
	
	public int getColor(List<String> course,String mString){
		setCourse(course);
		int k=0;
		for (String string:course) {
			if (string.equals(mString)) {
				break;
			}
			k++;
		}
		return color[k];
	}
	
}
