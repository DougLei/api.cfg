package com.king.tooth.sys.service.common;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.cache.SysConfig;
import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.plugins.thread.CurrentThreadContext;
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
	 * 验证工号是否已经存在
	 * @param user
	 * @return 
	 */
	private String validWorkNoIsExists(ComUser user) {
		long count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourceNameConstants.ID+") from ComUser where workNo=? and projectId=?", user.getWorkNo(), CurrentThreadContext.getCurrentAccountOnlineStatus().getProjectId());
		if(count > 0){
			return "系统已经存在工号为["+user.getWorkNo()+"]的用户";
		}
		return null;
	}
	
	/**
	 * 验证email邮箱是否已经存在
	 * @param user
	 * @return 
	 */
	private String validEmailIsExists(ComUser user) {
		if(StrUtils.isEmpty(user.getEmail())){
			return null;
		}
		long count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourceNameConstants.ID+") from ComUser where email=? and projectId=?", user.getEmail(), CurrentThreadContext.getCurrentAccountOnlineStatus().getProjectId());
		if(count > 0){
			return "系统已经存在邮箱为["+user.getEmail()+"]的用户";
		}
		return null;
	}
	
	/**
	 * 验证tel手机号是否已经存在
	 * @param user
	 * @return 
	 */
	private String validTelIsExists(ComUser user) {
		if(StrUtils.isEmpty(user.getTel())){
			return null;
		}
		long count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourceNameConstants.ID+") from ComUser where tel=? and projectId=?", user.getTel(), CurrentThreadContext.getCurrentAccountOnlineStatus().getProjectId());
		if(count > 0){
			return "系统已经存在手机号为["+user.getTel()+"]的用户";
		}
		return null;
	}
	
	/**
	 * 添加用户
	 * @param user
	 * @return
	 */
	public Object saveUser(ComUser user){
		String result = validWorkNoIsExists(user);
		if(result == null){
			result = validEmailIsExists(user);
		}
		if(result == null){
			result = validTelIsExists(user);
		}
		if(result == null){
			String accountId = null;
			if(user.getIsCreateAccount() == 1){
				ComSysAccount account = new ComSysAccount();
				account.setLoginName(user.getWorkNo());
				account.setLoginPwdKey(ResourceHandlerUtil.getLoginPwdKey());
				account.setLoginPwd(CryptographyUtil.encodeMd5(SysConfig.getSystemConfig("account.default.pwd"), account.getLoginPwdKey()));
				account.setTel(user.getTel());
				account.setEmails(user.getEmail());
				accountId = HibernateUtil.saveObject(account, null).getString(ResourceNameConstants.ID);
			}
			user.setAccountId(accountId);
			JSONObject userJsonObject = HibernateUtil.saveObject(user, null);
			String useId = userJsonObject.getString(ResourceNameConstants.ID);
			
			// 保存部门
			if(StrUtils.notEmpty(user.getDeptId())){
				JSONObject udLink = ResourceHandlerUtil.getDataLinksObject(useId, user.getDeptId(), "1", null, null);
				udLink.put("isMain", "1");
				HibernateUtil.saveObject("ComUserComDeptLinks", udLink, null);
			}
			// 保存岗位
			if(StrUtils.notEmpty(user.getPositionId())){
				JSONObject upLink = ResourceHandlerUtil.getDataLinksObject(useId, user.getPositionId(), "1", null, null);
				upLink.put("isMain", "1");
				HibernateUtil.saveObject("ComUserComPositionLinks", upLink, null);
			}
			
			return userJsonObject;
		}
		return result;
	}
	
	/**
	 * 修改用户
	 * @param user
	 * @return
	 */
	public Object updateUser(ComUser user){
		ComUser oldUser = getObjectById(user.getId(), ComUser.class);
		String result = null;
		if(!oldUser.getWorkNo().equals(user.getWorkNo())){
			result = validWorkNoIsExists(user);
		}
		if(result == null && oldUser.getEmail() != null && !oldUser.getEmail().equals(user.getEmail())){
			result = validEmailIsExists(user);
		}
		if(result == null && oldUser.getTel() != null && !oldUser.getTel().equals(user.getTel())){
			result = validTelIsExists(user);
		}
		if(result == null){
			JSONObject userJsonObject = HibernateUtil.updateObjectByHql(user, null);
			
			// 可能修改部门
			
			// 可能修改岗位
			
			return userJsonObject;
		}
		return result;
	}
}
