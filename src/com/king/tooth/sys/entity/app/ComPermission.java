package com.king.tooth.sys.entity.app;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.EntityJson;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.ISysResource;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.entity.cfg.ComTabledata;
import com.king.tooth.util.JsonUtil;

/**
 * 权限资源对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ComPermission extends BasicEntity implements ITable, IEntity{
	
	/**
	 * 关联的资源id
	 * <p>可以是模块id(菜单id)，也可以是模块下的tab id，还可以是模块下的操作功能id  等</p>
	 */
	private String refResourceId;
	/**
	 * 权限的类型
	 * <p>1:模块、2:tabl、3:功能  等</p>
	 */
	private Integer permissionType;
	/**
	 * 是否可见(是否可读)
	 */
	private Integer isVisibility;
	/**
	 * 是否可操作(是否可写)
	 */
	private Integer isOper;
	/**
	 * 密级
	 * <p>多个用,隔开</p>
	 */
	private String secretLevels;
	
	// ---------------------------------------------------------------------------

	public String getRefResourceId() {
		return refResourceId;
	}
	public void setRefResourceId(String refResourceId) {
		this.refResourceId = refResourceId;
	}
	public Integer getPermissionType() {
		return permissionType;
	}
	public void setPermissionType(Integer permissionType) {
		this.permissionType = permissionType;
	}
	public Integer getIsVisibility() {
		return isVisibility;
	}
	public void setIsVisibility(Integer isVisibility) {
		this.isVisibility = isVisibility;
	}
	public Integer getIsOper() {
		return isOper;
	}
	public void setIsOper(Integer isOper) {
		this.isOper = isOper;
	}
	public String getSecretLevels() {
		return secretLevels;
	}
	public void setSecretLevels(String secretLevels) {
		this.secretLevels = secretLevels;
	}
	
	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata("COM_PERMISSION", 0);
		table.setName("权限资源对象表");
		table.setComments("权限资源对象表");
		table.setIsBuiltin(1);
		table.setIsNeedDeploy(1);
		table.setBelongPlatformType(ISysResource.APP_PLATFORM);
		
		List<ComColumndata> columns = new ArrayList<ComColumndata>(11);
		
		ComColumndata refResourceIdColumn = new ComColumndata("org_id", DataTypeConstants.STRING, 32);
		refResourceIdColumn.setName("关联的资源id");
		refResourceIdColumn.setComments("关联的资源id：可以是模块id(菜单id)，也可以是模块下的tab id，还可以是模块下的操作功能id  等");
		refResourceIdColumn.setOrderCode(1);
		columns.add(refResourceIdColumn);
		
		ComColumndata permissionTypeColumn = new ComColumndata("permission_type", DataTypeConstants.INTEGER, 2);
		permissionTypeColumn.setName("权限的类型");
		permissionTypeColumn.setComments("权限的类型：1:模块、2:tab、3:功能  等");
		permissionTypeColumn.setOrderCode(2);
		columns.add(permissionTypeColumn);
		
		ComColumndata isVisibilityColumn = new ComColumndata("is_visibility", DataTypeConstants.INTEGER, 1);
		isVisibilityColumn.setName("是否可见(是否可读)");
		isVisibilityColumn.setComments("是否可见(是否可读)");
		isVisibilityColumn.setOrderCode(3);
		columns.add(isVisibilityColumn);
		
		ComColumndata isOperColumn = new ComColumndata("is_oper", DataTypeConstants.INTEGER, 1);
		isOperColumn.setName("是否可操作(是否可写)");
		isOperColumn.setComments("是否可操作(是否可写)");
		isOperColumn.setOrderCode(4);
		columns.add(isOperColumn);
		
		ComColumndata secretLevelsColumn = new ComColumndata("secret_levels", DataTypeConstants.STRING, 12);
		secretLevelsColumn.setName("密级");
		secretLevelsColumn.setComments("密级：多个用,隔开");
		secretLevelsColumn.setOrderCode(5);
		columns.add(secretLevelsColumn);
		
		table.setColumns(columns);
		return table;
	}

	public String toDropTable() {
		return "COM_PERMISSION";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "ComPermission";
	}
	
	public JSONObject toEntityJson() {
		EntityJson entityJson = new EntityJson(JsonUtil.toJsonObject(this));
		entityJson.put("permissionType", permissionType);
		entityJson.put("isVisibility", isVisibility);
		entityJson.put("isOper", isOper);
		super.processBasicEntityProps(entityJson);
		return entityJson.getEntityJson();
	}
}
