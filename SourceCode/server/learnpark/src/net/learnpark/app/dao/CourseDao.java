package net.learnpark.app.dao;

import java.util.List;

import net.learnpark.app.entity.Course;


public interface CourseDao {

	/**
	 * 将学生上传上来的课程表信息存入数据库中
	 * 
	 * @param teacher_id
	 * @param courses
	 *            课程表的字符串信息
	 * @return
	 */
	public Boolean putCourseByGson(String username, String course);

	/**
	 * 根据提供的老师的id获得老师的课程表信息
	 * 
	 * @param teacher_id
	 * @return 返回老师的课程表信息
	 */
	public List<Course> getCoursesList(String username);

}
