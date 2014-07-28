package net.learnpark.admin.dao;

import java.util.List;

import net.learnpark.admin.entity.Opinion;

public interface OpinionDao {
	/**
	 * 查询所有的用户意见
	 * @return
	 */
	public List<Opinion> selectAllOpinion();
	
	/**
	 * 根据练习方式和意见内容 修改意见回复
	 * @param lianxi
	 * @param yijian
	 * @param reply
	 * @return
	 */
	public boolean modifyOpinion(String lianxi,String yijian,String reply);
	
	
}
