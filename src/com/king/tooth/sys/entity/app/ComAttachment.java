package com.king.tooth.sys.entity.app;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
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
 * 附件资源对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ComAttachment extends BasicEntity implements ITable, IEntity{
	/**
	 * 关联的数据主键值
	 */
	private String refDataId;
	/**
	 * 附件的实际名
	 * <p>即上传时的文件名称</p>
	 */
	private String attachmentActName;
	/**
	 * 附件编码
	 * <p>在上传的时候，修改实际的文件名</p>
	 */
	private String attachmentCode;
	/**
	 * 附件大小
	 */
	private String attachmentSize;
	/**
	 * 附件后缀
	 */
	private String attachmentSuffix;
	/**
	 * 附件内容
	 */
	private String attachmentContent;
	/**
	 * 附件的密级
	 */
	private Integer secretLevel;
	
	// ---------------------------------------------------------------------------
	
	public String getRefDataId() {
		return refDataId;
	}
	public void setRefDataId(String refDataId) {
		this.refDataId = refDataId;
	}
	public String getAttachmentActName() {
		return attachmentActName;
	}
	public void setAttachmentActName(String attachmentActName) {
		this.attachmentActName = attachmentActName;
	}
	public String getAttachmentCode() {
		return attachmentCode;
	}
	public void setAttachmentCode(String attachmentCode) {
		this.attachmentCode = attachmentCode;
	}
	public String getAttachmentSize() {
		return attachmentSize;
	}
	public void setAttachmentSize(String attachmentSize) {
		this.attachmentSize = attachmentSize;
	}
	public String getAttachmentSuffix() {
		return attachmentSuffix;
	}
	public void setAttachmentSuffix(String attachmentSuffix) {
		this.attachmentSuffix = attachmentSuffix;
	}
	public String getAttachmentContent() {
		return attachmentContent;
	}
	public void setAttachmentContent(String attachmentContent) {
		this.attachmentContent = attachmentContent;
	}
	public Integer getSecretLevel() {
		return secretLevel;
	}
	public void setSecretLevel(Integer secretLevel) {
		this.secretLevel = secretLevel;
	}
	
	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata("COM_ATTACHMENT", 0);
		table.setName("附件资源对象表");
		table.setComments("附件资源对象表");
		table.setIsBuiltin(1);
		table.setIsNeedDeploy(1);
		table.setBelongPlatformType(ISysResource.APP_PLATFORM);
		
		List<ComColumndata> columns = new ArrayList<ComColumndata>(13);
		
		ComColumndata refDataIdColumn = new ComColumndata("ref_data_id", DataTypeConstants.STRING, 32);
		refDataIdColumn.setName("关联的数据主键值");
		refDataIdColumn.setComments("关联的数据主键值");
		refDataIdColumn.setOrderCode(1);
		columns.add(refDataIdColumn);
		
		ComColumndata attachmentActNameColumn = new ComColumndata("attachment_act_name", DataTypeConstants.STRING, 300);
		attachmentActNameColumn.setName("附件的实际名");
		attachmentActNameColumn.setComments("附件的实际名：即上传时的文件名称");
		attachmentActNameColumn.setOrderCode(2);
		columns.add(attachmentActNameColumn);
		
		ComColumndata attachmentCodeColumn = new ComColumndata("attachment_code", DataTypeConstants.STRING, 32);
		attachmentCodeColumn.setName("附件编码");
		attachmentCodeColumn.setComments("附件编码：在上传的时候，修改实际的文件名");
		attachmentCodeColumn.setOrderCode(3);
		columns.add(attachmentCodeColumn);
		
		ComColumndata attachmentSizeColumn = new ComColumndata("attachment_size", DataTypeConstants.STRING, 20);
		attachmentSizeColumn.setName("附件大小");
		attachmentSizeColumn.setComments("附件大小");
		attachmentSizeColumn.setOrderCode(4);
		columns.add(attachmentSizeColumn);
		
		ComColumndata attachmentSuffixColumn = new ComColumndata("attachment_suffix", DataTypeConstants.STRING, 10);
		attachmentSuffixColumn.setName("附件后缀");
		attachmentSuffixColumn.setComments("附件后缀");
		attachmentSuffixColumn.setOrderCode(5);
		columns.add(attachmentSuffixColumn);
		
		ComColumndata attachmentContentColumn = new ComColumndata("attachment_content", DataTypeConstants.BLOB, 0);
		attachmentContentColumn.setName("附件内容");
		attachmentContentColumn.setComments("附件内容");
		attachmentContentColumn.setOrderCode(6);
		columns.add(attachmentContentColumn);
		
		ComColumndata secretLevelColumn = new ComColumndata("secret_level", DataTypeConstants.INTEGER, 1);
		secretLevelColumn.setName("附件的密级");
		secretLevelColumn.setComments("附件的密级");
		secretLevelColumn.setOrderCode(7);
		columns.add(secretLevelColumn);
		
		table.setColumns(columns);
		return table;
	}

	public String toDropTable() {
		return "COM_ATTACHMENT";
	}
	
	public String getEntityName() {
		return "ComAttachment";
	}
	
	public JSONObject toEntityJson() {
		EntityJson entityJson = new EntityJson(JsonUtil.toJsonObject(this));
		super.processBasicEntityProps(entityJson);
		entityJson.put("secretLevel", secretLevel);
		return entityJson.getEntityJson();
	}
}
