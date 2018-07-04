package com.king.tooth.sys.service.common;

import com.king.tooth.cache.SysConfig;
import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.sys.entity.common.ComSysAccount;
import com.king.tooth.sys.entity.common.ComUser;
import com.king.tooth.sys.service.AbstractService;
import com.king.tooth.util.CryptographyUtil;
import com.king.tooth.util.ResourceHandlerUtil;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 人员资源服务处理器
 * @author DougLei
 */
public class ComUserService extends AbstractService{
	
	private ComSysAccountService accountService = new ComSysAccountService();
	
	/**
	 * 修改用户关联的账户密码
	 * @param userId
	 * @param newLoginPwd
	 * @return
	 */
	public String uploadUserLoginPwd(String userId, String newLoginPwd){
		ComUser user = getObjectById(userId, ComUser.class);
		if(StrUtils.isEmpty(user.getAccountId())){
			return "该用户不存在账户信息，无法修改密码，或先创建关联的账户信息";
		}
		return accountService.uploadAccounLoginPwd(user.getAccountId(), newLoginPwd);
	}
	
	/**
	 * 添加用户
	 * @param user
	 * @return
	 */
	public String saveUser(ComUser user){
		String accountId = null;
		if(user.getIsCreateAccount() == 1){
			ComSysAccount account = new ComSysAccount();
			account.setLoginName(user.getWorkNo());
			account.setLoginPwdKey(ResourceHandlerUtil.getLoginPwdKey());
			account.setLoginPwd(CryptographyUtil.encodeMd5(SysConfig.getSystemConfig("account.default.pwd"), account.getLoginPwdKey()));
			account.setTel(user.getTel());
			account.setEmails(user.getUserEmail());
			accountId = HibernateUtil.saveObject(account, null).getString(ResourceNameConstants.ID);
		}
		user.setAccountId(accountId);
		HibernateUtil.saveObject(user, null);
		return null;
	}
}
