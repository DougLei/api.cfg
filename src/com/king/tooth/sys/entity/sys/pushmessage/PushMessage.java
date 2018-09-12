package com.king.tooth.sys.entity.sys.pushmessage;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.sys.entity.IEntityPropAnalysis;
import com.king.tooth.thread.CurrentThreadContext;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 要推送消息的实体
 * @author DougLei
 */
public class PushMessage implements IEntityPropAnalysis{
	
	/**
	 * 接收的用户id
	 * <p>多个用,隔开</p>
	 */
	private String toUserId;
	/**
	 * 推送的消息类型
	 */
	private Integer pushMessageType;
	/**
	 * 推送的消息内容
	 */
	private String message;

	// ---------------------------------------------------------------------------------------------------
	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}
	public String getToUserId() {
		return toUserId;
	}
	public Integer getPushMessageType() {
		return pushMessageType;
	}
	public void setPushMessageType(Integer pushMessageType) {
		this.pushMessageType = pushMessageType;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	// ---------------------------------------------------------------------------------------------------
	/**
	 * 是否给所有用户推送
	 */
	private boolean isToAllUser;
	/**
	 * 接收的用户id数组
	 */
	private String[] toUserIdArr;
	/**
	 * 接收的用户id数组的长度
	 */
	private int toUserIdArrLength;
	/**
	 * 循环次数
	 */
	private int loopCount;
	/**
	 * 实际已经循环次数
	 */
	private int actLoopCount;
	/**
	 * 最后一次循环时，actualToUserIdArr数组的长度
	 */
	private int lastLength;
	/**
	 * 每次循环时，取toUserIdArr数组值的下标
	 */
	private int index;
	
	/**
	 * 实际要接收的用户id数组
	 * <p>如果一次处理的数量过大，则要分批次梳理，这个用来记录每次分批次推送时，实际的用户id数组</p>
	 */
	@JSONField(serialize = false)
	private String[] actualToUserIdArr;
	
	// ---------------------------------------------------------------------------------------------------
	public String validNotNullProps() {
		if(StrUtils.isEmpty(toUserId)){
			return "要接收推送消息的用户id不能为空！";
		}
		if(pushMessageType == null){
			return "推送消息的类型值不能为空！";
		}
		if(pushMessageType != 0){
			return "推送消息的pushMessageType值不合法，目前仅支持=0！";
		}
		if(StrUtils.isEmpty(message)){
			return "推送消息的内容不能为空！";
		}
		return null;
	}
	
	public String analysisResourceProp() {
		String result = validNotNullProps();
		if(result == null){
			isToAllUser = "all".equalsIgnoreCase(toUserId);
			if(!isToAllUser){
				toUserIdArr = toUserId.split(",");
				toUserIdArrLength = toUserIdArr.length;
				loopCount = toUserIdArrLength/size +1;
				lastLength = toUserIdArrLength%size;
				actLoopCount = 1;
			}
		}
		return result;
	}

	// ---------------------------------------------------------------------------------------------------
	/** 第几次 */
	private int count = 0;
	/** 每次多少条数据 */
	private static final int size = 100;
	/** 查询用户id的hql */
	private static final String queryUserIdHql = "select "+ResourcePropNameConstants.ID+" from SysUser where customerId=?";
	
	/**
	 * 是否还有userId
	 * @param pageNo 如果toUserId传的是all，则要分页查询用户表
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean hasMoreToUserId() {
		if(isToAllUser){
			count++;
			List<Object> toUserIdList = HibernateUtil.executeListQueryByHqlArr(size+"", count+"", queryUserIdHql, CurrentThreadContext.getCustomerId());
			if(toUserIdList == null || toUserIdList.size() == 0){
				return false;
			}
			actualToUserIdArr = new String[toUserIdList.size()];
			toUserIdList.toArray(actualToUserIdArr);
			toUserIdList.clear();
			return true;
		}else{
			int loopLimitSize = 0;
			if(actLoopCount > loopCount){
				return false;
			}else if(actLoopCount < loopCount){
				loopLimitSize = size;
				actualToUserIdArr = new String[size];
			}else{
				loopLimitSize = lastLength;
				actualToUserIdArr = new String[lastLength];
			}
			
			int actIndex = 0;
			for(int i=0;i<loopLimitSize;i++){
				actualToUserIdArr[actIndex++] = toUserIdArr[index++];
			}
			actLoopCount++;
			return true;
		}
	}

	/**
	 * 获取要推送的用户id数据
	 * @return
	 */
	public String[] getActualToUserIdArr() {
		return actualToUserIdArr;
	}
	
	/**
	 * 清除多于的数据
	 */
	public void clear() {
	}
}
