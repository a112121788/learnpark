package net.learnpark.app.test;

import java.util.List;

import net.learnpark.app.dao.CourseDao;
import net.learnpark.app.dao.impl.CourseDaoMySqlImpl;
import net.learnpark.app.entity.Course;

import org.junit.Test;

public class TestCourseDao {
	@Test
	public void testGetCoursesList() {
		CourseDao courseDao = new CourseDaoMySqlImpl();
		List<Course> course=courseDao.getCoursesList("840964310@qq.com");
		for (Course course2 : course) {
			System.out.println(course2.getCite());
		}
	}
}
