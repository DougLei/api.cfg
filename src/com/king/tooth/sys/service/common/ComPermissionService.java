package com.king.tooth.sys.service.common;

import java.util.ArrayList;
import java.util.List;

import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.plugins.thread.CurrentThreadContext;
import com.king.tooth.sys.builtin.data.BuiltinInstance;
import com.king.tooth.sys.builtin.data.BuiltinPermissionType;
import com.king.tooth.sys.entity.common.ComPermission;
import com.king.tooth.sys.entity.common.ComPermissionPriority;
import com.king.tooth.sys.service.AbstractService;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 权限资源服务处理器
 * @author DougLei
 */
public class ComPermissionService extends AbstractService{
	
	/**
	 * 得到系统权限的优先级集合
	 * @return
	 */
	private List<ComPermissionPriority> getPermissionPriorities(){
		String hql = "from ComPermissionPriority where projectId=? and customerId=? order by lv desc";
		List<ComPermissionPriority> permissionPriorities = HibernateUtil.extendExecuteListQueryByHqlArr(ComPermissionPriority.class, null, null, hql, CurrentThreadContext.getProjectId(), CurrentThreadContext.getCurrentAccountOnlineStatus().getCurrentCustomerId());
		if(permissionPriorities == null || permissionPriorities.size() == 0){
			permissionPriorities = BuiltinInstance.permissionPriorities;
		}
		return permissionPriorities;
	}
	
	// 第一次查询权限信息集合的hql语句
	private static final String queryPermissionHql = "from ComPermission where refDataType = ? and refDataId = ? and (refParentResourceId is null or refParentResourceId = '') and projectId = ? and customerId = ?";
	// 后续递归查询权限信息集合的hql语句
	private static final String recursiveQueryPermissionHql = "from ComPermission where refParentResourceId = ? and refParentResourceId = ? and projectId = ? and customerId = ?";
	// 按照orderCode asc，查询账户所属的角色所有的权限【orderCode越低的，优先级越高】
	private static final String queryAccountOfRolesHql = "select r."+ResourceNameConstants.ID+" from ComRole r, ComSysAccountComRoleLinks l where r.isEnabled=1 and r."+ResourceNameConstants.ID+"=l.rightId and l.leftId = ? order by r.orderCode asc";
	// 按照orderCode asc，查询账户所属的部门所有的权限【orderCode越低的，优先级越高】
	private static final String queryAccountOfDeptsHql = "select d."+ResourceNameConstants.ID+" from ComDept d, ComUserComDeptLinks l, ComUser u where d."+ResourceNameConstants.ID+"=l.rightId and u."+ResourceNameConstants.ID+"=l.leftId and u.accountId=? order by d.orderCode asc";
	// 按照orderCode asc，查询账户所属的职务所有的权限【orderCode越低的，优先级越高】
	private static final String queryAccountOfPositionsHql = "select p."+ResourceNameConstants.ID+" from ComPosition p, ComUserComPositionLinks l, ComUser u where p."+ResourceNameConstants.ID+"=l.rightId and u."+ResourceNameConstants.ID+"=l.leftId and u.accountId=? order by p.orderCode asc";
	
	/**
	 * 根据账户id，获取对应的的权限集合
	 * @param accountId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ComPermission findAccountOfPermissions(String accountId){
		String projectId = CurrentThreadContext.getProjectId();
		String customerId = CurrentThreadContext.getCurrentAccountOnlineStatus().getCurrentCustomerId();
		
		List<ComPermission> permissions = new ArrayList<ComPermission>();
		List<ComPermission> newPermissions = new ArrayList<ComPermission>();
		List<ComPermission> tmpPermissions = null;
		
		List<ComPermissionPriority> permissionPriorities = getPermissionPriorities();
		for (ComPermissionPriority permissionPriority : permissionPriorities) {
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
			ComPermission permission = new ComPermission();
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
	private List<ComPermission> getRootPermissionsByData(String refDataType, Object refDataId, String projectId, String customerId){
		return HibernateUtil.extendExecuteListQueryByHqlArr(ComPermission.class, null, null, queryPermissionHql, 
				refDataType, refDataId, projectId, customerId);
	}
	/**
	 * 获取引用的数据权限子数据集合
	 * @param permissions
	 * @param projectId
	 * @param customerId
	 */
	private void setSubPermissionsByData(List<ComPermission> permissions, String projectId, String customerId) {
		if(permissions == null || permissions.size() == 0){
			return;
		}
		for (ComPermission p : permissions) {
			p.setChildren(HibernateUtil.extendExecuteListQueryByHqlArr(ComPermission.class, null, null, recursiveQueryPermissionHql, 
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
	private void mergePermissions(List<ComPermission> permissions, List<ComPermission> newPermissions){
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
	private void doMerge(List<ComPermission> permissions, List<ComPermission> newPermissions){
		if(newPermissions == null || newPermissions.size() == 0){
			return;
		}
		try {
			if(permissions.size() == 0){
				permissions.addAll(newPermissions);
				return;
			}
			
			for (ComPermission np : newPermissions) {
				boolean unExists = true;
				for (ComPermission p : permissions) {
					if(np.getRefResourceId().equals(p.getRefResourceId()) || np.getRefResourceCode().equals(p.getRefResourceCode())){
						unExists = false;
						
						if(np.getIsVisibility()==1){
							p.setIsVisibility(1);
						}
						if(np.getIsOper()==1){
							p.setIsOper(1);
						}
						
						if(p.gainChildren() == null){
							p.setChildren(new ArrayList<ComPermission>());
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