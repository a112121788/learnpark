package net.learnpark.app.dao;

import java.util.List;

import net.learnpark.app.entity.File;

public interface FileDao {
	/**
	 * 根据课程的上传者 冲数据库中查询名称。
	 * 
	 * @param teacherName
	 * @return
	 */
	public List<File> getFilesByTeacher(String teacherName);

	/**
	 * 根据传入的数选择要查询的视频的条数
	 * 
	 * @param num
	 * @return
	 */
	public List<File> getFiles(int num);

	public List<File> getFilesCommon();
}
