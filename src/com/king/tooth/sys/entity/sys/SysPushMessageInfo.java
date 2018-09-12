package com.king.tooth.sys.entity.sys;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.annotation.Table;
import com.king.tooth.sys.builtin.data.BuiltinDataType;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.ISysResource;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.entity.cfg.ComTabledata;

/**
 * 推送消息信息表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class SysPushMessageInfo extends BasicEntity implements ITable, IEntity, Cloneable{
	
	/**
	 * 消息类型
	 * <p>不能为空</p>
	 * <p>0:直接推送，即直接将消息原原本本推送给客户端</p>
	 */
	private Integer msgType;
	/**
	 * 推送者账户id
	 */
	private String pushAccountId;
	/**
	 * 推送者id
	 */
	private String pushUserId;
	/**
	 * 接受者id
	 */
	private String receiveUserId;
	/**
	 * 源消息内容
	 */
	private String sourceMsg;
	/**
	 * 实际消息内容
	 */
	private String targetMsg;
	/**
	 * 消息批次排序
	 * <p>标识消息发送是否属于同一次，如果一次推送的人员超过100人后，系统会分批次，每次100人推送，以减少服务器压力；且这个字段还能用来排序，区分批次的顺序</p>
	 */
	private Integer msgBatchOrderCode;
	/**
	 * 消息排序
	 * <p>标识推送每条消息的顺序</p>
	 */
	private Integer msgOrderCode;
	
	//----------------------------------------------------------------

	public SysPushMessageInfo(String pushAccountId, String pushUserId) {
		this.pushAccountId = pushAccountId;
		this.pushUserId = pushUserId;
	}
	public SysPushMessageInfo() {
	}
	
	public Integer getMsgType() {
		return msgType;
	}
	public void setMsgType(Integer msgType) {
		this.msgType = msgType;
	}
	public String getPushAccountId() {
		return pushAccountId;
	}
	public void setPushAccountId(String pushAccountId) {
		this.pushAccountId = pushAccountId;
	}
	public String getPushUserId() {
		return pushUserId;
	}
	public void setPushUserId(String pushUserId) {
		this.pushUserId = pushUserId;
	}
	public String getReceiveUserId() {
		return receiveUserId;
	}
	public void setReceiveUserId(String receiveUserId) {
		this.receiveUserId = receiveUserId;
	}
	public String getSourceMsg() {
		return sourceMsg;
	}
	public void setSourceMsg(String sourceMsg) {
		this.sourceMsg = sourceMsg;
	}
	public String getTargetMsg() {
		return targetMsg;
	}
	public void setTargetMsg(String targetMsg) {
		this.targetMsg = targetMsg;
	}
	public Integer getMsgBatchOrderCode() {
		return msgBatchOrderCode;
	}
	public void setMsgBatchOrderCode(Integer msgBatchOrderCode) {
		this.msgBatchOrderCode = msgBatchOrderCode;
	}
	public Integer getMsgOrderCode() {
		return msgOrderCode;
	}
	public void setMsgOrderCode(Integer msgOrderCode) {
		this.msgOrderCode = msgOrderCode;
	}

	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata("SYS_PUSH_MESSAGE_INFO", 0);
		table.setName("推送消息信息表");
		table.setComments("推送消息信息表");
		table.setIsBuiltin(1);
		table.setIsNeedDeploy(1);
		table.setIsCreated(1);
		table.setBelongPlatformType(ISysResource.COMMON_PLATFORM);
		
		List<ComColumndata> columns = new ArrayList<ComColumndata>(15);
		
		ComColumndata msgTypeColumn = new ComColumndata("msg_type", BuiltinDataType.INTEGER, 1);
		msgTypeColumn.setName("消息类型");
		msgTypeColumn.setComments("不能为空；0:直接推送，即直接将消息原原本本推送给客户端；");
		msgTypeColumn.setIsNullabled(0);
		columns.add(msgTypeColumn);
		
		ComColumndata pushAccountIdColumn = new ComColumndata("push_account_id", BuiltinDataType.STRING, 32);
		pushAccountIdColumn.setName("推送者账户id");
		pushAccountIdColumn.setComments("推送者账户id");
		columns.add(pushAccountIdColumn);
		
		ComColumndata pushUserIdColumn = new ComColumndata("push_user_id", BuiltinDataType.STRING, 32);
		pushUserIdColumn.setName("推送者id");
		pushUserIdColumn.setComments("推送者id");
		columns.add(pushUserIdColumn);
		
		ComColumndata receiveUserIdColumn = new ComColumndata("receive_user_id", BuiltinDataType.STRING, 32);
		receiveUserIdColumn.setName("接受者id");
		receiveUserIdColumn.setComments("接受者id");
		columns.add(receiveUserIdColumn);
		
		ComColumndata sourceMsgColumn = new ComColumndata("source_msg", BuiltinDataType.CLOB, 0);
		sourceMsgColumn.setName("源消息内容");
		sourceMsgColumn.setComments("源消息内容");
		columns.add(sourceMsgColumn);
		
		ComColumndata targetMsgColumn = new ComColumndata("target_msg", BuiltinDataType.CLOB, 0);
		targetMsgColumn.setName("实际消息内容");
		targetMsgColumn.setComments("实际消息内容");
		columns.add(targetMsgColumn);
		
		ComColumndata msgBatchOrderCodeColumn = new ComColumndata("msg_batch_order_code", BuiltinDataType.INTEGER, 3);
		msgBatchOrderCodeColumn.setName("消息批次排序");
		msgBatchOrderCodeColumn.setComments("标识消息发送是否属于同一次，如果一次推送的人员超过100人后，系统会分批次，每次100人推送，以减少服务器压力；且这个字段还能用来排序，区分批次的顺序");
		columns.add(msgBatchOrderCodeColumn);
		
		ComColumndata msgOrderCodeColumn = new ComColumndata("msg_order_code", BuiltinDataType.INTEGER, 5);
		msgOrderCodeColumn.setName("消息排序");
		msgOrderCodeColumn.setComments("标识推送每条消息的顺序");
		columns.add(msgOrderCodeColumn);
		
		table.setColumns(columns);
		return table;
	}
	
	public String toDropTable() {
		return "SYS_PUSH_MESSAGE_INFO";
	}
	
	@JSONField(serialize = false)
	public String getEntityName() {
		return "SysPushMessageInfo";
	}
	
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	/**
	 * 解析实际要发送的消息
	 * @param toUserId
	 */
	public void analyzeActualSendMessage() {
		if(msgType == 1){
			targetMsg = sourceMsg;
		}
	}
}
