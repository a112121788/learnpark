package net.learnpark.admin.test;

import net.learnpark.app.dao.FeedbackDao;
import net.learnpark.app.dao.impl.FeedbackDaoMySqlImpl;

import org.junit.Test;

public class TestFeedbackDao {
	@Test
	public void testsaveFeedBack() {
		FeedbackDao feedback= new FeedbackDaoMySqlImpl();
		if(feedback.saveFeedBack("wo","我")){
			System.out.print("true");
		}else{
			System.out.print("false");
		}
	}
}
