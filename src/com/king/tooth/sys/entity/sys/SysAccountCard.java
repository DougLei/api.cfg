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
import com.king.tooth.util.StrUtils;

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
	 * 卡号来源
	 * <p>默认值为0，0:用户输入，1:自动生成</p>
	 */
	private int cardNoFrom;
	/**
	 * 是否绑定用户
	 * <p>默认是0</p>
	 */
	private int isBind;
	
	//-------------------------------------------------------------------------
	
	public SysAccountCard() {
	}
	public SysAccountCard(String id, int status) {
		this.id = id;
		this.status = status;
	}
	
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
	public int getCardNoFrom() {
		return cardNoFrom;
	}
	public void setCardNoFrom(int cardNoFrom) {
		this.cardNoFrom = cardNoFrom;
	}
	public int getIsBind() {
		return isBind;
	}
	public void setIsBind(int isBind) {
		this.isBind = isBind;
	}
	
	@JSONField(serialize = false)
	public boolean getIsInputCardNo() {
		return cardNoFrom == 0;
	}
	
	
	@JSONField(serialize = false)
	public List<CfgColumn> getColumnList() {
		List<CfgColumn> columns = new ArrayList<CfgColumn>(4+7);
		
		CfgColumn cardNoColumn = new CfgColumn("card_no", DataTypeConstants.STRING, 64);
		cardNoColumn.setName("卡号");
		cardNoColumn.setComments("卡号");
		columns.add(cardNoColumn);
		
		CfgColumn statusColumn = new CfgColumn("status", DataTypeConstants.INTEGER, 1);
		statusColumn.setName("卡状态");
		statusColumn.setComments("1.启用、2.禁用，默认值是：1");
		statusColumn.setDefaultValue("1");
		columns.add(statusColumn);
		
		CfgColumn cardNoFromColumn = new CfgColumn("card_no_from", DataTypeConstants.INTEGER, 1);
		cardNoFromColumn.setName("卡号来源");
		cardNoFromColumn.setComments("默认值为0，0:用户输入，1:自动生成");
		cardNoFromColumn.setDefaultValue("0");
		columns.add(cardNoFromColumn);
		
		CfgColumn isBindColumn = new CfgColumn("is_bind", DataTypeConstants.INTEGER, 1);
		isBindColumn.setName("是否绑定用户");
		isBindColumn.setComments("默认是0");
		isBindColumn.setDefaultValue("0");
		columns.add(isBindColumn);
		
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
			return "状态值目前只支持1(启用)和2(禁用)";
		}
		if(cardNoFrom != 0 && cardNoFrom != 1){
			return "卡号来源值目前只支持0(用户输入，默认值)和1(自动生成)";
		}
		if(getIsInputCardNo() && StrUtils.isEmpty(cardNo)){
			return "卡号不能为空";
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
