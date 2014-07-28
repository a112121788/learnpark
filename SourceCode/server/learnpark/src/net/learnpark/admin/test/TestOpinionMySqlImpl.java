package net.learnpark.admin.test;

import java.util.List;

import net.learnpark.admin.dao.OpinionDao;
import net.learnpark.admin.dao.impl.OpinionDaoMySqlImpl;
import net.learnpark.admin.entity.Opinion;

import org.junit.Test;

public class TestOpinionMySqlImpl {
	@Test
	public void testselectAllOpinion() {
		OpinionDao opinionDao=new OpinionDaoMySqlImpl();
		List<Opinion> opinions=opinionDao.selectAllOpinion();
		System.out.println(opinions.size());
	}
	@Test
	public void testmodifyOpinion(){
		OpinionDao opinionDao=new OpinionDaoMySqlImpl();
		opinionDao.modifyOpinion("840964310", "hhh", "已收到");
	}
}
