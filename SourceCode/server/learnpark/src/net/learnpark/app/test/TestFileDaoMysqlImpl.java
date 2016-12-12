package net.learnpark.app.test;

import net.learnpark.app.dao.FileDao;
import net.learnpark.app.dao.impl.FileDaoMySqlImpl;

import org.junit.Test;

public class TestFileDaoMysqlImpl {


	@Test
	public void testgetFilesCommon(){
		FileDao videoDao=new FileDaoMySqlImpl();
		videoDao.getFilesCommon();
		System.out.println(	videoDao.getFilesCommon().size());
	}
}
