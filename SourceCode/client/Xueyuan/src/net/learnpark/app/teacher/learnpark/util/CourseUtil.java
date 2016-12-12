package net.learnpark.app.teacher.learnpark.util;

import java.util.List;

import android.content.Context;

import net.learnpark.app.teacher.learnpark.shixian.Course;
import net.tsz.afinal.FinalDb;

public class CourseUtil {

	public static List<Course> getCoursesList(Context context) {
		FinalDb courseDb = FinalDb.create(context);
		List<Course> course_all = courseDb.findAll(Course.class);
		return course_all;
	}

	public static String getCourseString(Context context, String i, String j) {
		FinalDb courseDb = FinalDb.create(context);
		List<Course> course_list = courseDb.findAllByWhere(Course.class, "day="
				+ i + " and time=" + j);
		Course course = course_list.get(0);
		String mytoString = course.getCoursename() + "\n@" + course.getCite()
				+ "\n" + course.getClassname();
		return mytoString;
	}

	public static int insertCoursesForResult(Context context, Course course1) {
		FinalDb courseDb = FinalDb.create(context);
		List<Course> courses_all = courseDb.findAll(Course.class);
		for (int i = 0; i < courses_all.size(); i++) {
			Course course2 = courses_all.get(i);
			if (course2.getDay() == course1.getDay()
					&& course2.getTime() == course1.getTime()) {
				if (!(course2.getClassname().equals("") && course2
						.getClassname().length() == 0)) {
					courseDb.update(course1, "day=" + course1.getDay()
							+ " and time=" + course1.getTime());
					return 1;
				} else {
					courseDb.update(course1, "day=" + course1.getDay()
							+ " and time=" + course1.getTime());
					return 2;
				}
			}
		}
		return 0;
	}

	public static void insertCourses(Context context, Course course1) {
		FinalDb courseDb = FinalDb.create(context);
		courseDb.update(course1, "day=" + course1.getDay() + " and time="
				+ course1.getTime());
	}

	public static void deleteCourses(Context context, Course course) {
		FinalDb courseDb = FinalDb.create(context);
		courseDb.update(course, "day=" + course.getDay() + " and time="
				+ course.getTime());
	}

	public static List<Course> getCoursesListByDay(Context context, int day) {
		FinalDb courseDb = FinalDb.create(context);
		List<Course> course_list = courseDb.findAllByWhere(Course.class, "day="
				+ day);
		return course_list;
	}

	public static void createCourses(Context context) {
		FinalDb courseDb = FinalDb.create(context);
		Course course = new Course();
		course.setCite("");
		course.setClassname("");
		course.setCoursename("");
		course.setTimebegin("");
		course.setTimeend("");
		for (int i = 1; i < 8; i++) {
			for (int j = 1; j < 9; j++) {
				course.setDay(i);
				course.setTime(j);
				courseDb.save(course);
			}
		}
	}
}
