package com.king.tooth.sys.entity.sys;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.annotation.Table;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.ResourceInfoConstants;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.IEntityPropAnalysis;
import com.king.tooth.sys.entity.cfg.CfgColumn;
import com.king.tooth.sys.entity.cfg.CfgTable;

/**
 * 账户卡表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class SysAccountCard extends BasicEntity implements IEntity, IEntityPropAnalysis{

	/**
	 * 卡号
	 */
	private String cardNo;
	/**
	 * 卡状态
	 * 		1.启用
	 * 		2.禁用
	 * <p>默认值是：1</p>
	 */
	private int status = 1;
	/**
	 * 是否被删除
	 * <p>逻辑删除，默认值为0</p>
	 */
	private int isDelete;
	
	//-------------------------------------------------------------------------
	
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}
	
	@JSONField(serialize = false)
	public List<CfgColumn> getColumnList() {
		List<CfgColumn> columns = new ArrayList<CfgColumn>(3+7);
		
		CfgColumn cardNoColumn = new CfgColumn("card_no", DataTypeConstants.STRING, 64);
		cardNoColumn.setName("卡号");
		cardNoColumn.setComments("卡号");
		columns.add(cardNoColumn);
		
		CfgColumn statusColumn = new CfgColumn("status", DataTypeConstants.INTEGER, 1);
		statusColumn.setName("卡状态");
		statusColumn.setComments("1.启用、2.禁用，默认值是：1");
		statusColumn.setDefaultValue("1");
		columns.add(statusColumn);
		
		CfgColumn isDeleteColumn = new CfgColumn("is_delete", DataTypeConstants.INTEGER, 1);
		isDeleteColumn.setName("是否被删除");
		isDeleteColumn.setComments("逻辑删除，默认值为0");
		isDeleteColumn.setDefaultValue("0");
		columns.add(isDeleteColumn);
		
		return columns;
	}
	
	public CfgTable toCreateTable() {
		CfgTable table = new CfgTable(toDropTable());
		table.setName("账户卡表");
		table.setRemark("账户卡表");
		table.setRequestMethod(ResourceInfoConstants.GET);
		
		table.setColumns(getColumnList());
		return table;
	}

	public String toDropTable() {
		return "SYS_ACCOUNT_CARD";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "SysAccountCard";
	}
	
	public String validNotNullProps() {
		if(status != 1 && status != 2){
			return "状态值目前只支持1和2";
		}
		return null;
	}
	
	public String analysisResourceProp() {
		String result = validNotNullProps();
		if(result == null){
		}
		return result;
	}
}
