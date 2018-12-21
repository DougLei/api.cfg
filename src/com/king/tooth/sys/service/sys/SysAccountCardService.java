package com.king.tooth.sys.service.sys;

import java.util.Date;

import com.king.tooth.annotation.Service;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.constants.SqlStatementTypeConstants;
import com.king.tooth.sys.builtin.data.BuiltinResourceInstance;
import com.king.tooth.sys.entity.sys.SysAccount;
import com.king.tooth.sys.entity.sys.SysAccountCard;
import com.king.tooth.sys.entity.sys.SysAccountOnlineStatus;
import com.king.tooth.sys.service.AService;
import com.king.tooth.thread.current.CurrentThreadContext;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.hibernate.HibernateUtil;

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
		if(accountCard == null || accountCard.getIsDelete() == 1){
			return new SysAccountOnlineStatus("不存在卡号为["+originAccountCard.getCardNo()+"]的信息");
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
		if(validCardNoIsExists(accountCard)){
			return "卡号["+accountCard.getCardNo()+"]已经存在";
		}
		return HibernateUtil.saveObject(accountCard, null);
	}

	public Object updateAccountCard(SysAccountCard accountCard) {
		SysAccountCard oldAccountCard = getObjectById(accountCard.getId(), SysAccountCard.class);
		if(!oldAccountCard.getCardNo().equals(accountCard.getCardNo()) && validCardNoIsExists(accountCard)){
			return "卡号["+accountCard.getCardNo()+"]已经存在";
		}
		return HibernateUtil.updateEntityObject(accountCard, null);
	}

	public Object deleteAccountCard(String accountCardId) {
		HibernateUtil.executeUpdateByHqlArr(SqlStatementTypeConstants.UPDATE, "update SysAccountCard set isDelete=1, lastUpdateDate=? where "+ResourcePropNameConstants.ID+" = ?", new Date(), accountCardId);
		return null;
	}
}
