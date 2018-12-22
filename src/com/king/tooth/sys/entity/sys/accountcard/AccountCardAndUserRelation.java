package com.king.tooth.sys.entity.sys.accountcard;

import com.king.tooth.sys.entity.IEntityPropAnalysis;
import com.king.tooth.util.StrUtils;

/**
 * 账户卡和用户的关系
 * @author DougLei
 */
public class AccountCardAndUserRelation implements IEntityPropAnalysis{
	private String accountCardId;
	private String userId;
	public String getAccountCardId() {
		return accountCardId;
	}
	public void setAccountCardId(String accountCardId) {
		this.accountCardId = accountCardId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	
	public String validNotNullProps() {
		if(StrUtils.isEmpty(accountCardId)){
			return "账户卡id不能为空";
		}
		if(StrUtils.isEmpty(userId)){
			return "用户id不能为空";
		}
		return null;
	}
	public String analysisResourceProp() {
		return validNotNullProps();
	}
	public String getId() {
		return null;
	}
	public String getEntityName() {
		return null;
	}
}
