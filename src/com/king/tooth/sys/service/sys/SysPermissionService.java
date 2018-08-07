package com.king.tooth.sys.service.sys;

import java.util.ArrayList;
import java.util.List;

import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.plugins.thread.CurrentThreadContext;
import com.king.tooth.sys.builtin.data.BuiltinInstance;
import com.king.tooth.sys.builtin.data.BuiltinPermissionType;
import com.king.tooth.sys.entity.sys.SysPermission;
import com.king.tooth.sys.entity.sys.SysPermissionPriority;
import com.king.tooth.sys.service.AbstractService;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 权限信息表service
 * @author DougLei
 */
public class SysPermissionService extends AbstractService{
	
	/**
	 * 得到系统权限的优先级集合
	 * @return
	 */
	private List<SysPermissionPriority> getPermissionPriorities(){
		String hql = "from SysPermissionPriority where projectId=? and customerId=? order by lv desc";
		List<SysPermissionPriority> permissionPriorities = HibernateUtil.extendExecuteListQueryByHqlArr(SysPermissionPriority.class, null, null, hql, CurrentThreadContext.getProjectId(), CurrentThreadContext.getCurrentAccountOnlineStatus().getCustomerId());
		if(permissionPriorities == null || permissionPriorities.size() == 0){
			permissionPriorities = BuiltinInstance.permissionPriorities;
		}
		return permissionPriorities;
	}
	
	// 第一次查询权限信息集合的hql语句
	private static final String queryPermissionHql = "from SysPermission where refDataType = ? and refDataId = ? and (refParentResourceId is null or refParentResourceId = '') and projectId = ? and customerId = ?";
	// 后续递归查询权限信息集合的hql语句
	private static final String recursiveQueryPermissionHql = "from SysPermission where refParentResourceId = ? and refParentResourceId = ? and projectId = ? and customerId = ?";
	// 按照orderCode asc，查询账户所属的角色所有的权限【orderCode越低的，优先级越高】
	private static final String queryAccountOfRolesHql = "select r."+ResourcePropNameConstants.ID+" from SysRole r, SysAccountRoleLinks l where r.isEnabled=1 and r."+ResourcePropNameConstants.ID+"=l.rightId and l.leftId = ? order by r.orderCode asc";
	// 按照orderCode asc，查询账户所属的部门所有的权限【orderCode越低的，优先级越高】
	private static final String queryAccountOfDeptsHql = "select d."+ResourcePropNameConstants.ID+" from SysDept d, SysUserDeptLinks l, SysUser u where d."+ResourcePropNameConstants.ID+"=l.rightId and u."+ResourcePropNameConstants.ID+"=l.leftId and u.accountId=? order by d.orderCode asc";
	// 按照orderCode asc，查询账户所属的职务所有的权限【orderCode越低的，优先级越高】
	private static final String queryAccountOfPositionsHql = "select p."+ResourcePropNameConstants.ID+" from SysPosition p, SysUserPositionLinks l, SysUser u where p."+ResourcePropNameConstants.ID+"=l.rightId and u."+ResourcePropNameConstants.ID+"=l.leftId and u.accountId=? order by p.orderCode asc";
	
	/**
	 * 根据账户id，获取对应的的权限集合
	 * @param accountId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public SysPermission findAccountOfPermissions(String accountId){
		String projectId = CurrentThreadContext.getProjectId();
		String customerId = CurrentThreadContext.getCurrentAccountOnlineStatus().getCustomerId();
		
		List<SysPermission> permissions = new ArrayList<SysPermission>();
		List<SysPermission> newPermissions = new ArrayList<SysPermission>();
		List<SysPermission> tmpPermissions = null;
		
		List<SysPermissionPriority> permissionPriorities = getPermissionPriorities();
		for (SysPermissionPriority permissionPriority : permissionPriorities) {
			// 帐号
			if(BuiltinPermissionType.ACCOUNT.equals(permissionPriority.getPermissionType())){
				newPermissions = getRootPermissionsByData(BuiltinPermissionType.ACCOUNT, accountId, projectId, customerId);
				setSubPermissionsByData(newPermissions, projectId, customerId);
			}
			// 角色
			else if(BuiltinPermissionType.ROLE.equals(permissionPriority.getPermissionType())){
				List<Object> roleIds = HibernateUtil.executeListQueryByHqlArr(null, null, queryAccountOfRolesHql, accountId);
				if(roleIds != null && roleIds.size() > 0){
					roleIds = processSamePermissionTypeLevel(roleIds, permissionPriority.getSamePermissionTypeLv());
					
					for (Object roleId : roleIds) {
						tmpPermissions = getRootPermissionsByData(BuiltinPermissionType.ROLE, roleId, projectId, customerId);
						setSubPermissionsByData(tmpPermissions, projectId, customerId);
						mergePermissions(newPermissions, tmpPermissions);
					}
					roleIds.clear();
				}
			}
			// 部门
			else if(BuiltinPermissionType.DEPT.equals(permissionPriority.getPermissionType())){
				List<Object> deptIds = HibernateUtil.executeListQueryByHqlArr(null, null, queryAccountOfDeptsHql, accountId);
				if(deptIds != null && deptIds.size() > 0){
					deptIds = processSamePermissionTypeLevel(deptIds, permissionPriority.getSamePermissionTypeLv());		
					
					for (Object deptId : deptIds) {
						tmpPermissions = getRootPermissionsByData(BuiltinPermissionType.ROLE, deptId, projectId, customerId);
						setSubPermissionsByData(newPermissions, projectId, customerId);
						mergePermissions(newPermissions, tmpPermissions);
					}
					deptIds.clear();
				}
			}
			// 岗位
			else if(BuiltinPermissionType.POSITION.equals(permissionPriority.getPermissionType())){
				List<Object> positionIds = HibernateUtil.executeListQueryByHqlArr(null, null, queryAccountOfPositionsHql, accountId);
				if(positionIds != null && positionIds.size() > 0){
					positionIds = processSamePermissionTypeLevel(positionIds, permissionPriority.getSamePermissionTypeLv());	
					
					for (Object positionId : positionIds) {
						tmpPermissions = getRootPermissionsByData(BuiltinPermissionType.ROLE, positionId, projectId, customerId);
						setSubPermissionsByData(newPermissions, projectId, customerId);
						mergePermissions(newPermissions, tmpPermissions);
					}
					positionIds.clear();
				}
			}
			mergePermissions(permissions, newPermissions);
		}
		
		if(permissions.size() > 0){
			SysPermission permission = new SysPermission();
			permission.setRefResourceCode("ROOT");
			permission.setRefResourceId("ROOT");
			permission.setRefResourceType("ROOT");
			permission.setChildren(permissions);
			return permission;
		}
		return null;
	}
	
	/**
	 * 处理相同权限类型的优先级：这里可以按照等级顺序，存储多个id，用,分割，越前面的，优先级越高；第一个优先级最高
	 * @param permissionTypeIds
	 * @param samePermissionTypePriorityLevel
	 * @return
	 */
	private List<Object> processSamePermissionTypeLevel(List<Object> permissionTypeIds, String samePermissionTypePriorityLevel){
		if(StrUtils.isEmpty(samePermissionTypePriorityLevel)){
			return permissionTypeIds;
		}
		List<Object> tmpPermissionTypeIds = new ArrayList<Object>(permissionTypeIds.size());
		String[] samePermissionTypePriorityLevelArr = samePermissionTypePriorityLevel.split(",");
		
		for (String sptpl : samePermissionTypePriorityLevelArr) {
			if(permissionTypeIds.contains(sptpl)){
				tmpPermissionTypeIds.add(sptpl);
				permissionTypeIds.remove(sptpl);
			}
		}
		if(permissionTypeIds.size() > 0){
			tmpPermissionTypeIds.addAll(permissionTypeIds);
			permissionTypeIds.clear();
		}
		return tmpPermissionTypeIds;
	}
	
	/**
	 * 获取引用的数据权限根数据集合
	 * @param refDataType
	 * @param refDataId
	 * @param projectId
	 * @param customerId
	 * @return
	 */
	private List<SysPermission> getRootPermissionsByData(String refDataType, Object refDataId, String projectId, String customerId){
		return HibernateUtil.extendExecuteListQueryByHqlArr(SysPermission.class, null, null, queryPermissionHql, 
				refDataType, refDataId, projectId, customerId);
	}
	/**
	 * 获取引用的数据权限子数据集合
	 * @param permissions
	 * @param projectId
	 * @param customerId
	 */
	private void setSubPermissionsByData(List<SysPermission> permissions, String projectId, String customerId) {
		if(permissions == null || permissions.size() == 0){
			return;
		}
		for (SysPermission p : permissions) {
			p.setChildren(HibernateUtil.extendExecuteListQueryByHqlArr(SysPermission.class, null, null, recursiveQueryPermissionHql, 
					p.getRefResourceId(), p.getRefResourceCode(), projectId, customerId));
			setSubPermissionsByData(p.gainChildren(), projectId, customerId);
		}
	}

	
	
	
	/**
	 * 将新的权限和原有权限合并，取并集
	 * @param permissions
	 * @param newPermissions
	 * @return
	 */
	private void mergePermissions(List<SysPermission> permissions, List<SysPermission> newPermissions){
		if(newPermissions == null || newPermissions.size() == 0){
			return;
		}
		if(permissions.size() == 0){
			permissions.addAll(newPermissions);
			newPermissions.clear();
		}else{
			doMerge(permissions, newPermissions);
		}
	}
	/**
	 * 做合并操作
	 * @param permissions
	 * @param newPermissions
	 */
	private void doMerge(List<SysPermission> permissions, List<SysPermission> newPermissions){
		if(newPermissions == null || newPermissions.size() == 0){
			return;
		}
		try {
			if(permissions.size() == 0){
				permissions.addAll(newPermissions);
				return;
			}
			
			for (SysPermission np : newPermissions) {
				boolean unExists = true;
				for (SysPermission p : permissions) {
					if(np.getRefResourceId().equals(p.getRefResourceId()) || np.getRefResourceCode().equals(p.getRefResourceCode())){
						unExists = false;
						
						if(np.getIsVisibility()==1){
							p.setIsVisibility(1);
						}
						if(np.getIsOper()==1){
							p.setIsOper(1);
						}
						
						if(p.gainChildren() == null){
							p.setChildren(new ArrayList<SysPermission>());
						}
						doMerge(p.gainChildren(), np.gainChildren());
						break;
					}
				}
				if(unExists){
					permissions.add(np);
				}
			}
		} finally{
			newPermissions.clear();
		}
	}
}