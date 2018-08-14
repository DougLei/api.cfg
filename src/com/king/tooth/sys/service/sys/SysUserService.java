package com.king.tooth.sys.service.sys;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.cache.SysConfig;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.sys.builtin.data.BuiltinDatabaseData;
import com.king.tooth.sys.builtin.data.BuiltinObjectInstance;
import com.king.tooth.sys.entity.sys.SysAccount;
import com.king.tooth.sys.entity.sys.SysUser;
import com.king.tooth.sys.service.AbstractService;
import com.king.tooth.thread.CurrentThreadContext;
import com.king.tooth.util.CryptographyUtil;
import com.king.tooth.util.ResourceHandlerUtil;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 人员信息表Service
 * @author DougLei
 */
public class SysUserService extends AbstractService{
	
	private static final String sysUserDeptLinks = "SysUserDeptLinks";
	private static final String sysUserPositionLinks = "SysUserPositionLinks";
	
	/**
	 * 修改用户关联的账户密码
	 * @param userId
	 * @param newLoginPwd
	 * @return
	 */
	public Object uploadUserLoginPwd(String userId, String newLoginPwd){
		SysUser user = getObjectById(userId, SysUser.class);
		if(StrUtils.isEmpty(user.getAccountId())){
			return "该用户不存在账户信息，无法修改密码，或先创建关联的账户信息";
		}
		return BuiltinObjectInstance.accountService.uploadAccounLoginPwd(user.getId(), user.getAccountId(), newLoginPwd);
	}
	
	/**
	 * 验证工号是否已经存在
	 * @param user
	 * @return 
	 */
	private String validWorkNoIsExists(SysUser user) {
		String workNo = user.getWorkNo();
		String currentCustomerId = CurrentThreadContext.getCurrentAccountOnlineStatus().getCustomerId();
		
		long count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourcePropNameConstants.ID+") from SysUser where workNo=? and customerId=?", workNo, currentCustomerId);
		if(count > 0){
			return "系统已经存在工号为["+workNo+"]的用户";
		}
		
		// 如果同时创建账户，则要去账户表中去判断，是否有重名的loginName
		if(user.getIsCreateAccount() == 1){
			count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourcePropNameConstants.ID+") from SysAccount where loginName=? and customerId=?", workNo, currentCustomerId);
			if(count > 0){
				return "系统已经存在登录名为["+workNo+"]的账户";
			}
		}
		return null;
	}
	
	/**
	 * 验证email邮箱是否已经存在
	 * @param user
	 * @return 
	 */
	private String validEmailIsExists(SysUser user) {
		String email = user.getEmail();
		if(StrUtils.isEmpty(email)){
			return null;
		}
		String currentCustomerId = CurrentThreadContext.getCurrentAccountOnlineStatus().getCustomerId();
		
		long count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourcePropNameConstants.ID+") from SysUser where email=? and customerId=?", email, currentCustomerId);
		if(count > 0){
			return "系统已经存在邮箱为["+email+"]的用户";
		}
		
		// 如果同时创建账户，则要去账户表中去判断，是否有重名的email
		if(user.getIsCreateAccount() == 1){
			count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourcePropNameConstants.ID+") from SysAccount where email=? and customerId=?", email, currentCustomerId);
			if(count > 0){
				return "系统已经存在邮箱为["+email+"]的账户";
			}
		}
		return null;
	}
	
	/**
	 * 验证tel手机号是否已经存在
	 * @param user
	 * @return 
	 */
	private String validTelIsExists(SysUser user) {
		String tel = user.getTel();
		if(StrUtils.isEmpty(tel)){
			return null;
		}
		String currentCustomerId = CurrentThreadContext.getCurrentAccountOnlineStatus().getCustomerId();
		
		long count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourcePropNameConstants.ID+") from SysUser where tel=? and customerId=?", tel, currentCustomerId);
		if(count > 0){
			return "系统已经存在手机号为["+tel+"]的用户";
		}
		
		// 如果同时创建账户，则要去账户表中去判断，是否有重名的tel
		if(user.getIsCreateAccount() == 1){
			count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourcePropNameConstants.ID+") from SysAccount where tel=? and customerId=?", tel, currentCustomerId);
			if(count > 0){
				return "系统已经存在手机号为["+tel+"]的账户";
			}
		}
		return null;
	}
	
	/**
	 * 添加用户
	 * @param user
	 * @return
	 */
	public Object saveUser(SysUser user){
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
				SysAccount account = new SysAccount();
				account.setLoginName(user.getWorkNo());
				account.setLoginPwdKey(ResourceHandlerUtil.getLoginPwdKey());
				account.setLoginPwd(CryptographyUtil.encodeMd5(SysConfig.getSystemConfig("account.default.pwd"), account.getLoginPwdKey()));
				account.setTel(user.getTel());
				account.setEmail(user.getEmail());
				accountId = HibernateUtil.saveObject(account, null).getString(ResourcePropNameConstants.ID);
			}
			user.setAccountId(accountId);
			JSONObject userJsonObject = HibernateUtil.saveObject(user, null);
			String userId = userJsonObject.getString(ResourcePropNameConstants.ID);
			
			// 保存部门
			if(StrUtils.notEmpty(user.getDeptId())){
				JSONObject udLink = ResourceHandlerUtil.getDataLinksObject(userId, user.getDeptId(), "1", null, null);
				udLink.put("isMain", "1");
				HibernateUtil.saveObject(sysUserDeptLinks, udLink, null);
			}
			// 保存岗位
			if(StrUtils.notEmpty(user.getPositionId())){
				JSONObject upLink = ResourceHandlerUtil.getDataLinksObject(userId, user.getPositionId(), "1", null, null);
				upLink.put("isMain", "1");
				HibernateUtil.saveObject(sysUserPositionLinks, upLink, null);
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
	public Object updateUser(SysUser user){
		SysUser oldUser = getObjectById(user.getId(), SysUser.class);
		
		String accountId = oldUser.getAccountId();
		boolean modifyAccountInfo = false;// 标识是否修改账户信息
		SysAccount account = null;
		if(StrUtils.notEmpty(accountId)){
			account = getObjectById(oldUser.getAccountId(), SysAccount.class);
		}else if(user.getIsCreateAccount() == 1){
			account = new SysAccount();
		}
		
		String result = null;
		if(!oldUser.getWorkNo().equals(user.getWorkNo())){
			modifyAccountInfo = true;
			result = validWorkNoIsExists(user);
			if(result == null && user.getIsCreateAccount() == 1){
				account.setLoginName(user.getWorkNo());
			}
		}
		if(result == null && (StrUtils.notEmpty(oldUser.getEmail()) && !oldUser.getEmail().equals(user.getEmail())) || (StrUtils.isEmpty(oldUser.getEmail()) && StrUtils.notEmpty(user.getEmail()))){
			modifyAccountInfo = true;
			result = validEmailIsExists(user);
			if(result == null && user.getIsCreateAccount() == 1){
				account.setEmail(user.getEmail());
			}
		}
		if(result == null && (StrUtils.notEmpty(oldUser.getTel()) && !oldUser.getTel().equals(user.getTel())) || (StrUtils.isEmpty(oldUser.getTel()) && StrUtils.notEmpty(user.getTel()))){
			modifyAccountInfo = true;
			result = validTelIsExists(user);
			if(result == null && user.getIsCreateAccount() == 1){
				account.setTel(user.getTel());
			}
		}
		if(result == null){
			if(account != null && modifyAccountInfo){
				if(StrUtils.notEmpty(oldUser.getAccountId())){
					HibernateUtil.updateObject(account, null);
				}else{
					account.setLoginPwdKey(ResourceHandlerUtil.getLoginPwdKey());
					account.setLoginPwd(CryptographyUtil.encodeMd5(SysConfig.getSystemConfig("account.default.pwd"), account.getLoginPwdKey()));
					accountId = HibernateUtil.saveObject(account, null).getString(ResourcePropNameConstants.ID);
					user.setAccountId(accountId);
				}
			}
			JSONObject userJsonObject = HibernateUtil.updateObject(user, null);
			String userId = oldUser.getId();
			
			// 可能修改部门
			if((StrUtils.notEmpty(oldUser.getDeptId()) && !oldUser.getDeptId().equals(user.getDeptId())) || (StrUtils.isEmpty(oldUser.getDeptId()) && StrUtils.notEmpty(user.getDeptId()))){
				if(StrUtils.notEmpty(oldUser.getDeptId())){
					HibernateUtil.deleteDataLinks(sysUserDeptLinks, userId, oldUser.getDeptId());
				}
				
				if(StrUtils.notEmpty(user.getDeptId())){
					JSONObject upLink = ResourceHandlerUtil.getDataLinksObject(userId, user.getPositionId(), "1", null, null);
					upLink.put("isMain", "1");
					HibernateUtil.saveObject(sysUserDeptLinks, upLink, null);
				}
			}
			
			// 可能修改岗位
			if((StrUtils.notEmpty(oldUser.getPositionId()) && !oldUser.getPositionId().equals(user.getPositionId())) || (StrUtils.isEmpty(oldUser.getPositionId()) && StrUtils.notEmpty(user.getPositionId()))){
				if(StrUtils.notEmpty(oldUser.getPositionId())){
					HibernateUtil.deleteDataLinks(sysUserPositionLinks, userId, oldUser.getPositionId());
				}
				
				if(StrUtils.notEmpty(user.getPositionId())){
					JSONObject upLink = ResourceHandlerUtil.getDataLinksObject(userId, user.getPositionId(), "1", null, null);
					upLink.put("isMain", "1");
					HibernateUtil.saveObject(sysUserPositionLinks, upLink, null);
				}
			}
			return userJsonObject;
		}
		return result;
	}

	/**
	 * 开通账户
	 * @param user
	 * @return
	 */
	public Object openAccount(SysUser user) {
		user = getObjectById(user.getId(), SysUser.class);
		
		SysAccount account = new SysAccount();
		account.setLoginName(user.getWorkNo());
		account.setLoginPwdKey(ResourceHandlerUtil.getLoginPwdKey());
		account.setLoginPwd(CryptographyUtil.encodeMd5(SysConfig.getSystemConfig("account.default.pwd"), account.getLoginPwdKey()));
		account.setTel(user.getTel());
		account.setEmail(user.getEmail());
		String accountId = HibernateUtil.saveObject(account, null).getString(ResourcePropNameConstants.ID);
		
		HibernateUtil.executeUpdateByHqlArr(BuiltinDatabaseData.UPDATE, "update SysUser set accountId=? where "+ResourcePropNameConstants.ID+"=?", accountId, user.getId());
		
		JSONObject jsonObject = new JSONObject(1);
		jsonObject.put(ResourcePropNameConstants.ID, user.getId());
		return jsonObject;
	}
	
	/**
	 * 删除用户
	 * @param userId
	 * @return
	 */
	public String deleteUser(String userId) {
		SysUser oldUser = getObjectById(userId, SysUser.class);
		HibernateUtil.executeUpdateByHqlArr(BuiltinDatabaseData.DELETE, "delete SysUser where "+ResourcePropNameConstants.ID+" = '"+oldUser.getId()+"'");
		
		if(StrUtils.notEmpty(oldUser.getAccountId())){
			HibernateUtil.executeUpdateByHqlArr(BuiltinDatabaseData.DELETE, "delete SysAccount where "+ResourcePropNameConstants.ID+" = '"+oldUser.getAccountId()+"'");
		}
		
		if(StrUtils.notEmpty(oldUser.getDeptId())){
			HibernateUtil.deleteDataLinks(sysUserDeptLinks, oldUser.getId(), oldUser.getDeptId());
		}
		
		if(StrUtils.notEmpty(oldUser.getPositionId())){
			HibernateUtil.deleteDataLinks(sysUserPositionLinks, oldUser.getId(), oldUser.getPositionId());
		}
		
		return null;
	}
}
