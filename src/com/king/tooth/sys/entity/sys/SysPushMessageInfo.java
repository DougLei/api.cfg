package com.king.tooth.sys.entity.sys;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.annotation.Table;
import com.king.tooth.sys.builtin.data.BuiltinDataType;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.IEntity;
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
	 * <p>1:待办消息、2.通知消息、...</p>
	 */
	private Integer msgType;
	/**
	 * 推送类型
	 * <p>不能为空</p>
	 * <p>0:直接推送，即直接将消息原原本本推送给客户端</p>
	 */
	private Integer sendType;
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
	 * 消息推送的批次编号
	 * <p>区分每次的消息推送</p>
	 */
	private String batchNum;
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
	/**
	 * 是否已读
	 * <p>默认值为0</p>
	 */
	private Integer isReaded;
	/**
	 * 推送是否成功
	 */
	private Integer isSuccess;
	/**
	 * 推送的结果编码
	 */
	private Integer pushResultCode;
	
	//----------------------------------------------------------------

	public SysPushMessageInfo(String pushAccountId, String pushUserId, String projectId, String customerId, String batchNum) {
		this.pushUserId = pushUserId;
		this.batchNum = batchNum;
		
		this.customerId = customerId;
		this.projectId = projectId;
		this.createUserId = pushAccountId;
		this.lastUpdateUserId = pushAccountId;
		this.createDate = new Date();
		this.lastUpdateDate = new Date();
	}
	public SysPushMessageInfo() {
	}
	
	public Integer getMsgType() {
		return msgType;
	}
	public void setMsgType(Integer msgType) {
		this.msgType = msgType;
	}
	public Integer getSendType() {
		return sendType;
	}
	public void setSendType(Integer sendType) {
		this.sendType = sendType;
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
	public String getBatchNum() {
		return batchNum;
	}
	public void setBatchNum(String batchNum) {
		this.batchNum = batchNum;
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
	public Integer getIsReaded() {
		return isReaded;
	}
	public void setIsReaded(Integer isReaded) {
		this.isReaded = isReaded;
	}
	public Integer getIsSuccess() {
		return isSuccess;
	}
	public void setIsSuccess(Integer isSuccess) {
		this.isSuccess = isSuccess;
	}
	public Integer getPushResultCode() {
		return pushResultCode;
	}
	public void setPushResultCode(Integer pushResultCode) {
		this.pushResultCode = pushResultCode;
	}
	
	@JSONField(serialize = false)
	public List<ComColumndata> getColumnList() {
		List<ComColumndata> columns = new ArrayList<ComColumndata>(19);
		
		ComColumndata msgTypeColumn = new ComColumndata("msg_type", BuiltinDataType.INTEGER, 1);
		msgTypeColumn.setName("消息类型");
		msgTypeColumn.setComments("不能为空；1:待办消息、2.通知消息、...");
		msgTypeColumn.setIsNullabled(0);
		columns.add(msgTypeColumn);
		
		ComColumndata sendTypeColumn = new ComColumndata("send_type", BuiltinDataType.INTEGER, 1);
		sendTypeColumn.setName("推送类型");
		sendTypeColumn.setComments("不能为空；0:直接推送，即直接将消息原原本本推送给客户端；");
		sendTypeColumn.setIsNullabled(0);
		columns.add(sendTypeColumn);
		
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
		
		ComColumndata batchNumCodeColumn = new ComColumndata("batch_num", BuiltinDataType.STRING, 32);
		batchNumCodeColumn.setName("消息推送的批次编号");
		batchNumCodeColumn.setComments("区分每次的消息推送");
		columns.add(batchNumCodeColumn);
		
		ComColumndata msgBatchOrderCodeColumn = new ComColumndata("msg_batch_order_code", BuiltinDataType.INTEGER, 3);
		msgBatchOrderCodeColumn.setName("消息批次排序");
		msgBatchOrderCodeColumn.setComments("标识消息发送是否属于同一次，如果一次推送的人员超过100人后，系统会分批次，每次100人推送，以减少服务器压力；且这个字段还能用来排序，区分批次的顺序");
		columns.add(msgBatchOrderCodeColumn);
		
		ComColumndata msgOrderCodeColumn = new ComColumndata("msg_order_code", BuiltinDataType.INTEGER, 5);
		msgOrderCodeColumn.setName("消息排序");
		msgOrderCodeColumn.setComments("标识推送每条消息的顺序");
		columns.add(msgOrderCodeColumn);
		
		ComColumndata isReadedColumn = new ComColumndata("is_readed", BuiltinDataType.INTEGER, 1);
		isReadedColumn.setName("是否已读");
		isReadedColumn.setComments("默认值为0");
		isReadedColumn.setDefaultValue("0");
		columns.add(isReadedColumn);
		
		ComColumndata isSuccessColumn = new ComColumndata("is_success", BuiltinDataType.INTEGER, 1);
		isSuccessColumn.setName("推送是否成功");
		isSuccessColumn.setComments("推送是否成功");
		columns.add(isSuccessColumn);
		
		ComColumndata pushResultCodeColumn = new ComColumndata("push_result_code", BuiltinDataType.INTEGER, 5);
		pushResultCodeColumn.setName("推送的结果编码");
		pushResultCodeColumn.setComments("推送的结果编码");
		columns.add(pushResultCodeColumn);
		
		return columns;
	}
	
	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata(toGetTableName());
		table.setName("推送消息信息表");
		table.setComments("推送消息信息表");
		
		
		table.setColumns(getColumnList());
		return table;
	}
	
	public String toGetTableName() {
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
		if(sendType == DIRECT_SEND){
			targetMsg = sourceMsg;
		}
	}
	
	/**
	 * 记录推送结果编码
	 * <p>并记录是否推送成功</p>
	 * @param pushResultCode
	 */
	public void recordPushResultCode(Integer pushResultCode) {
		this.pushResultCode = pushResultCode;
		this.isSuccess = (pushResultCode==1)?1:0;
	}
	
	// -----------------------------------------------------------
	/**
	 * 0:直接推送，即直接将消息原原本本推送给客户端
	 * <p>推送类型</p>
	 */
	public static final Integer DIRECT_SEND = 0;
}
