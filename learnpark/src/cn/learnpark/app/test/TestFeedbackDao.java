package cn.learnpark.app.test;

import org.junit.Test;

import cn.learnpark.app.dao.FeedbackDao;
import cn.learnpark.app.dao.impl.FeedbackDaoMySqlImpl;

public class TestFeedbackDao {
	@Test
	public void testsaveFeedBack() {
		FeedbackDao feedback= new FeedbackDaoMySqlImpl();
		if(feedback.saveFeedBack("wo", "我")){
			System.out.print("true");
		}else{
			System.out.print("false");
		}
	}
}
