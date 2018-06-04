package com.king.tooth.sys.entity.cfg;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.ITable;

/**
 * [配置系统]客户资源对象
 * <p>当ComUser中userType=2时，这个表中会存储一条相应的数据信息，也和帐号主键关联</p>
 * @author DougLei
 */
@SuppressWarnings("serial")
public class CfgCustomer extends BasicEntity implements ITable{

	/**
	 * 客户名称
	 */
	private String name;
	/**
	 * 客户简称
	 */
	private String shortName;
	/**
	 * 客户地址
	 */
	private String addr;
	/**
	 * 客户税号
	 */
	private String taxNum;
	/**
	 * 电话(企业电话)
	 */
	private String tel;
	/**
	 * 开户行
	 */
	private String bank;
	/**
	 * 账号
	 */
	private String bankCardNo;
	/**
	 * 联系人姓名
	 */
	private String contactUserName;
	/**
	 * 联系人电话
	 */
	private String contactUserTel;
	/**
	 * 联系人邮箱
	 */
	private String contactUserEmail;
	/**
	 * 备注
	 */
	private String remark;
	
	
	//--------------------------------------------------
	
	public String getName() {
		return name;
	}
	public String getId() {
		return id;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}
	public String getCreateUserId() {
		return createUserId;
	}
	public String getLastUpdatedUserId() {
		return lastUpdatedUserId;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	public void setLastUpdatedUserId(String lastUpdatedUserId) {
		this.lastUpdatedUserId = lastUpdatedUserId;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getTaxNum() {
		return taxNum;
	}
	public void setTaxNum(String taxNum) {
		this.taxNum = taxNum;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public String getBankCardNo() {
		return bankCardNo;
	}
	public void setBankCardNo(String bankCardNo) {
		this.bankCardNo = bankCardNo;
	}
	public String getContactUserName() {
		return contactUserName;
	}
	public void setContactUserName(String contactUserName) {
		this.contactUserName = contactUserName;
	}
	public String getContactUserTel() {
		return contactUserTel;
	}
	public void setContactUserTel(String contactUserTel) {
		this.contactUserTel = contactUserTel;
	}
	public String getContactUserEmail() {
		return contactUserEmail;
	}
	public void setContactUserEmail(String contactUserEmail) {
		this.contactUserEmail = contactUserEmail;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public CfgTabledata toCreateTable(String dbType) {
		CfgTabledata table = new CfgTabledata(dbType, "CFG_CUSTOMER");
		table.setName("[配置系统]客户资源对象表");
		table.setComments("[配置系统]客户资源对象表：当ComUser中userType=2时，这个表中会存储一条相应的数据信息，也和帐号主键关联");
		
		List<CfgColumndata> columns = new ArrayList<CfgColumndata>(16);
		
		CfgColumndata nameColumn = new CfgColumndata("name");
		nameColumn.setName("客户名称");
		nameColumn.setComments("客户名称");
		nameColumn.setColumnType(DataTypeConstants.STRING);
		nameColumn.setLength(200);
		nameColumn.setOrderCode(1);
		columns.add(nameColumn);
		
		CfgColumndata shortNameColumn = new CfgColumndata("short_name");
		shortNameColumn.setName("客户简称");
		shortNameColumn.setComments("客户简称");
		shortNameColumn.setColumnType(DataTypeConstants.STRING);
		shortNameColumn.setLength(40);
		shortNameColumn.setOrderCode(2);
		columns.add(shortNameColumn);
		
		CfgColumndata addrColumn = new CfgColumndata("addr");
		addrColumn.setName("客户地址");
		addrColumn.setComments("客户地址");
		addrColumn.setColumnType(DataTypeConstants.STRING);
		addrColumn.setLength(200);
		addrColumn.setOrderCode(3);
		columns.add(addrColumn);
		
		CfgColumndata taxNumColumn = new CfgColumndata("tax_num");
		taxNumColumn.setName("客户税号");
		taxNumColumn.setComments("客户税号");
		taxNumColumn.setColumnType(DataTypeConstants.STRING);
		taxNumColumn.setLength(30);
		taxNumColumn.setOrderCode(4);
		columns.add(taxNumColumn);
		
		CfgColumndata telColumn = new CfgColumndata("tel");
		telColumn.setName("电话(企业电话)");
		telColumn.setComments("电话(企业电话)");
		telColumn.setColumnType(DataTypeConstants.STRING);
		telColumn.setLength(20);
		telColumn.setOrderCode(5);
		columns.add(telColumn);
		
		CfgColumndata bankColumn = new CfgColumndata("bank");
		bankColumn.setName("开户行");
		bankColumn.setComments("开户行");
		bankColumn.setColumnType(DataTypeConstants.STRING);
		bankColumn.setLength(300);
		bankColumn.setOrderCode(6);
		columns.add(bankColumn);
		
		CfgColumndata bankCardNoColumn = new CfgColumndata("bank_card_no");
		bankCardNoColumn.setName("账号");
		bankCardNoColumn.setComments("账号");
		bankCardNoColumn.setColumnType(DataTypeConstants.STRING);
		bankCardNoColumn.setLength(50);
		bankCardNoColumn.setOrderCode(7);
		columns.add(bankCardNoColumn);
		
		CfgColumndata contactUserNameColumn = new CfgColumndata("contact_user_name");
		contactUserNameColumn.setName("联系人姓名");
		contactUserNameColumn.setComments("联系人姓名");
		contactUserNameColumn.setColumnType(DataTypeConstants.STRING);
		contactUserNameColumn.setLength(100);
		contactUserNameColumn.setOrderCode(8);
		columns.add(contactUserNameColumn);
		
		CfgColumndata contactUserTelColumn = new CfgColumndata("contact_user_tel");
		contactUserTelColumn.setName("联系人电话");
		contactUserTelColumn.setComments("联系人电话");
		contactUserTelColumn.setColumnType(DataTypeConstants.STRING);
		contactUserTelColumn.setLength(20);
		contactUserTelColumn.setOrderCode(9);
		columns.add(contactUserTelColumn);
		
		CfgColumndata contactUserEmailColumn = new CfgColumndata("contact_user_email");
		contactUserEmailColumn.setName("联系人邮箱");
		contactUserEmailColumn.setComments("联系人邮箱");
		contactUserEmailColumn.setColumnType(DataTypeConstants.STRING);
		contactUserEmailColumn.setLength(80);
		contactUserEmailColumn.setOrderCode(10);
		columns.add(contactUserEmailColumn);
		
		CfgColumndata remarkColumn = new CfgColumndata("remark");
		remarkColumn.setName("备注");
		remarkColumn.setComments("备注");
		remarkColumn.setColumnType(DataTypeConstants.STRING);
		remarkColumn.setLength(200);
		remarkColumn.setOrderCode(11);
		columns.add(remarkColumn);
		
		table.setColumns(columns);
		return table;
	}
	
	public String toDropTable() {
		return "CFG_CUSTOMER";
	}
}
