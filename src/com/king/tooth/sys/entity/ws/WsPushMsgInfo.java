package com.king.tooth.sys.entity.ws;

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
public class WsPushMsgInfo extends BasicEntity implements ITable, IEntity{
	
	/**
	 * 消息类型
	 * <p>1:直接推送，即直接将消息原原本本推送给客户端</p>
	 */
	private Integer msgType;
	/**
	 * 推送者id
	 */
	private String pushUserId;
	/**
	 * 接受者id
	 * <p>多个用,分割</p>
	 */
	private String receiveUserId;
	/**
	 * 消息内容
	 */
	private String msg;
	/**
	 * 消息批次
	 * <p>标识消息发送是否属于同一次，如果一次推送的人员超过100人后，系统会分批次，每次100人推送，以减少服务器压力</p>
	 */
	private String msgBatch;
	/**
	 * 排序
	 * <p>用在同一批次时 ，标识推送的顺序</p>
	 * <p>默认值为1</p>
	 */
	private Integer orderCode;
	/**
	 * 是否推送成功
	 * <p>不能为空</p>
	 */
	private Integer isPushSuccess;
	/**
	 * 推送失败原因
	 * <p>比如用户不在线，后端异常等</p>
	 */
	private String pushUnsuccessReason;
	
	//----------------------------------------------------------------

	public Integer getMsgType() {
		return msgType;
	}
	public void setMsgType(Integer msgType) {
		this.msgType = msgType;
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
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getMsgBatch() {
		return msgBatch;
	}
	public void setMsgBatch(String msgBatch) {
		this.msgBatch = msgBatch;
	}
	public Integer getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(Integer orderCode) {
		this.orderCode = orderCode;
	}
	public Integer getIsPushSuccess() {
		return isPushSuccess;
	}
	public void setIsPushSuccess(Integer isPushSuccess) {
		this.isPushSuccess = isPushSuccess;
	}
	public String getPushUnsuccessReason() {
		return pushUnsuccessReason;
	}
	public void setPushUnsuccessReason(String pushUnsuccessReason) {
		this.pushUnsuccessReason = pushUnsuccessReason;
	}

	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata("WS_PUSH_MSG_INFO", 0);
		table.setName("推送消息信息表");
		table.setComments("推送消息信息表");
		table.setIsBuiltin(1);
		table.setIsNeedDeploy(1);
		table.setIsCreated(1);
		table.setBelongPlatformType(ISysResource.COMMON_PLATFORM);
		
		List<ComColumndata> columns = new ArrayList<ComColumndata>(15);
		
		ComColumndata msgTypeColumn = new ComColumndata("msg_type", BuiltinDataType.INTEGER, 1);
		msgTypeColumn.setName("消息类型");
		msgTypeColumn.setComments("1:直接推送，即直接将消息原原本本推送给客户端；");
		columns.add(msgTypeColumn);
		
		ComColumndata pushUserIdColumn = new ComColumndata("push_user_id", BuiltinDataType.STRING, 32);
		pushUserIdColumn.setName("推送者id");
		pushUserIdColumn.setComments("推送者id");
		columns.add(pushUserIdColumn);
		
		ComColumndata receiveUserIdColumn = new ComColumndata("receive_user_id", BuiltinDataType.STRING, 3300);
		receiveUserIdColumn.setName("接受者id");
		receiveUserIdColumn.setComments("多个用,分割");
		columns.add(receiveUserIdColumn);
		
		ComColumndata msgColumn = new ComColumndata("msg", BuiltinDataType.STRING, 3000);
		msgColumn.setName("消息内容");
		msgColumn.setComments("消息内容");
		columns.add(msgColumn);
		
		ComColumndata msgBatchColumn = new ComColumndata("msg_batch", BuiltinDataType.STRING, 32);
		msgBatchColumn.setName("消息批次");
		msgBatchColumn.setComments("标识消息发送是否属于同一次，如果一次推送的人员超过100人后，系统会分批次，每次100人推送，以减少服务器压力");
		columns.add(msgBatchColumn);
		
		ComColumndata orderCodeColumn = new ComColumndata("order_code", BuiltinDataType.INTEGER, 3);
		orderCodeColumn.setName("排序");
		orderCodeColumn.setComments("用在同一批次时 ，标识推送的顺序，默认值为1");
		orderCodeColumn.setDefaultValue("1");
		columns.add(orderCodeColumn);
		
		ComColumndata isPushSuccessColumn = new ComColumndata("is_push_success", BuiltinDataType.INTEGER, 1);
		isPushSuccessColumn.setName("是否推送成功");
		isPushSuccessColumn.setComments("不能为空");
		isPushSuccessColumn.setIsNullabled(0);
		columns.add(isPushSuccessColumn);
		
		ComColumndata pushUnsuccessReasonColumn = new ComColumndata("push_unsuccess_reason", BuiltinDataType.STRING, 1000);
		pushUnsuccessReasonColumn.setName("推送失败原因");
		pushUnsuccessReasonColumn.setComments("比如用户不在线，后端异常等");
		columns.add(pushUnsuccessReasonColumn);
		
		table.setColumns(columns);
		return table;
	}
	
	public String toDropTable() {
		return "WS_PUSH_MSG_INFO";
	}
	
	@JSONField(serialize = false)
	public String getEntityName() {
		return "WsPushMsgInfo";
	}
}
