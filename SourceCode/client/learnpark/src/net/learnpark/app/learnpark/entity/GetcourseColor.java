package net.learnpark.app.learnpark.entity;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;

public class GetcourseColor {

	private int[] color = { Color.parseColor("#F0FFFF"),
			Color.parseColor("#EED2EE"), Color.parseColor("#E0FFFF"),
			Color.parseColor("#BBFFFF"), Color.parseColor("#B4EEB4"),
			Color.parseColor("#AEEEEE"), Color.parseColor("#B0E0E6"),
			Color.parseColor("#8EE5EE"), Color.parseColor("#7FFFD4"),
			Color.parseColor("#E0EEE0"), Color.parseColor("#DDA0DD"),
			Color.parseColor("#D1EEEE"), Color.parseColor("#CDB79E"),
			Color.parseColor("#CD96CD"), Color.parseColor("#CDC1C5"),
			Color.parseColor("#CAE1FF"), Color.parseColor("#CAFF70"),
			Color.parseColor("#C6E2FF"), Color.parseColor("#C1FFC1"),
			Color.parseColor("#BBFFFF"), Color.parseColor("#B0E2FF"),
			Color.parseColor("#98F5FF"), Color.parseColor("#8FBC8F"),
			Color.parseColor("#8DEEEE"), Color.parseColor("#71C671"),
			Color.parseColor("#32CD32"), Color.parseColor("#00CDCD"),
			Color.parseColor("#43CD80") };
	private List<String> course = new ArrayList<String>();

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

	public List<String> addcourse(List<String> bcourse, String mcourse) {
		this.course = bcourse;
		course.add(mcourse);
		return course;
	}

	public List<String> initcourse(List<String> bcourse) {
		this.course = bcourse;
		for (int i = 0; i < course.size() - 1; i++) { // 循环遍历集体中的元素
			for (int j = course.size() - 1; j > i; j--) { // 这里非常巧妙，这里是倒序的是比较
				if (course.get(j).equals(course.get(i))) {
					course.remove(j);
				}
			}
		}
		return course;
	}

	public int getColor(List<String> course, String mString) {
		setCourse(course);
		int k = 0;
		for (String string : course) {
			if (string.equals(mString)) {
				break;
			}
			k++;
		}
		return color[k];
	}

}
