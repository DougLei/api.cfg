package com.king.tooth.sys.service.sys;

import com.king.tooth.annotation.Service;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.constants.SqlStatementTypeConstants;
import com.king.tooth.sys.builtin.data.BuiltinResourceInstance;
import com.king.tooth.sys.entity.sys.SysAccount;
import com.king.tooth.sys.entity.sys.SysAccountCard;
import com.king.tooth.sys.entity.sys.SysAccountOnlineStatus;
import com.king.tooth.sys.entity.sys.SysUser;
import com.king.tooth.sys.entity.sys.accountcard.AccountCardAndUserRelation;
import com.king.tooth.sys.service.AService;
import com.king.tooth.thread.current.CurrentThreadContext;
import com.king.tooth.util.JsonUtil;
import com.king.tooth.util.ResourceHandlerUtil;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.hibernate.HibernateUtil;
import com.king.tooth.util.prop.code.rule.PropCodeRuleUtil;

/**
 * 账户卡表Service
 * @author DougLei
 */
@Service
public class SysAccountCardService extends AService{

	/**
	 * 刷卡登陆
	 * @param string
	 * @param accountCard
	 * @return
	 */
	public SysAccountOnlineStatus loginByCard(String clientIp, SysAccountCard originAccountCard) {
		if(StrUtils.isEmpty(originAccountCard.getCardNo())){
			return new SysAccountOnlineStatus("卡号不能为空");
		}
		
		SysAccountCard accountCard = HibernateUtil.extendExecuteUniqueQueryByHqlArr(SysAccountCard.class, "from SysAccountCard where cardNo=? and customerId=?", originAccountCard.getCardNo(), CurrentThreadContext.getCustomerId());
		if(accountCard == null){
			return new SysAccountOnlineStatus("不存在卡号为["+originAccountCard.getCardNo()+"]的信息");
		}
		if(accountCard.getStatus() == 2){
			return new SysAccountOnlineStatus("卡["+originAccountCard.getCardNo()+"]已被禁用，请联系系统管理员");
		}
		
		SysAccount account = getObjectById(accountCard.getId(), SysAccount.class);
		return BuiltinResourceInstance.getInstance("SysAccountService", SysAccountService.class).loginByCard(clientIp, account.getLoginName());
	}
	
	// ---------------------------------------------------------------------------------
	/**验证卡号是否已经存在*/
	private boolean validCardNoIsExists(SysAccountCard accountCard) {
		Object obj = HibernateUtil.executeUniqueQueryByHqlArr("select "+ResourcePropNameConstants.ID+" from SysAccountCard where cardNo= ? and customerId=?", accountCard.getCardNo(), CurrentThreadContext.getCustomerId());
		if(obj == null){
			return false;
		}
		return true;
	}
	
	public Object saveAccountCard(SysAccountCard accountCard) {
		if(StrUtils.notEmpty(accountCard.getId())){
			SysUser user = HibernateUtil.extendExecuteUniqueQueryByHqlArr(SysUser.class, "from SysUser where " + ResourcePropNameConstants.ID +"=?", accountCard.getId());
			if(user != null){
				SysAccountCard oldAccountCard = HibernateUtil.extendExecuteUniqueQueryByHqlArr(SysAccountCard.class, "from SysAccountCard where " + ResourcePropNameConstants.ID +"=?", accountCard.getId());
				if(oldAccountCard != null){
					return "用户["+user.getName()+"]已经存在卡号["+oldAccountCard.getCardNo()+"]，无法重复添加";
				}
				accountCard.setIsBind(1);
			}
		}
		
		if(accountCard.getIsInputCardNo()){
			if(validCardNoIsExists(accountCard)){
				return "卡号["+accountCard.getCardNo()+"]已经存在";
			}
		}else{
			accountCard = PropCodeRuleUtil.processBuiltinTableResourceCodeRuleValue(accountCard).get(0).toJavaObject(SysAccountCard.class);
		}
		return HibernateUtil.saveObject(accountCard, null);
	}

	public Object updateAccountCard(SysAccountCard accountCard) {
		SysAccountCard oldAccountCard = getObjectById(accountCard.getId(), SysAccountCard.class);
		if(!oldAccountCard.getCardNo().equals(accountCard.getCardNo())){
			if(accountCard.getIsInputCardNo()){
				if(validCardNoIsExists(accountCard)){
					return "卡号["+accountCard.getCardNo()+"]已经存在";
				}
			}else{
				return "禁止修改由规则自动生成的卡号";
			}
		}
		return HibernateUtil.updateEntityObject(accountCard, null);
	}

	public Object deleteAccountCard(String accountCardId) {
		return deleteDataById("SysAccountCard", accountCardId);
	}

	public Object addCardAndUserRelation(AccountCardAndUserRelation acur) {
		if(acur.isSame()){
			return "增加账户卡和用户的关系时，传入的账户卡id和用户id值应该不一致";
		}
		SysUser user = HibernateUtil.extendExecuteUniqueQueryByHqlArr(SysUser.class, "from SysUser where " + ResourcePropNameConstants.ID +"=?", acur.getAccountCardId());
		if(user != null){
			Object cardNo = HibernateUtil.executeUniqueQueryByHqlArr("select cardNo from SysAccountCard where " + ResourcePropNameConstants.ID +"=?", acur.getAccountCardId());
			return "卡号为["+cardNo+"]的卡，已经和用户["+user.getName()+"]有关联，无法重复关联";
		}
		HibernateUtil.executeUpdateByHqlArr(SqlStatementTypeConstants.UPDATE, 
				"update SysAccountCard set isBind=1, "+ResourcePropNameConstants.ID+"=? where " + ResourcePropNameConstants.ID+"=?", 
				acur.getUserId(), acur.getAccountCardId());
		return JsonUtil.toJsonObject(acur);
	}

	public Object cancelCardAndUserRelation(AccountCardAndUserRelation acur) {
		if(!acur.isSame()){
			return "取消账户卡和用户的关系时，传入的账户卡id应该和用户id值一致";
		}
		HibernateUtil.executeUpdateByHqlArr(SqlStatementTypeConstants.UPDATE, 
				"update SysAccountCard set isBind=0, "+ResourcePropNameConstants.ID+"=? where " + ResourcePropNameConstants.ID+"=?", 
				ResourceHandlerUtil.getIdentity(), acur.getAccountCardId());
		return JsonUtil.toJsonObject(acur);
	}
}
