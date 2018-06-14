package com.king.tooth.sys.entity.cfg;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.sys.entity.AbstractSysResource;
import com.king.tooth.sys.entity.EntityJson;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.common.ComSysResource;
import com.king.tooth.util.JsonUtil;
import com.king.tooth.util.ResourceHandlerUtil;

/**
 * 要发布的基础数据资源对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ComPublishBasicData extends AbstractSysResource implements ITable, IEntity{
	
	/**
	 * 基础数据所对应的资源名
	 * <p>例如：数据字典对应的资源名为ComDataDictionary</p>
	 */
	private String basicDataResourceName;
	/**
	 * 基础数据的json字符串内容
	 */
	private String basicDataJsonStr;
	
	//----------------------------------------------------------------

	public String getBasicDataResourceName() {
		return basicDataResourceName;
	}
	public void setBasicDataResourceName(String basicDataResourceName) {
		this.basicDataResourceName = basicDataResourceName;
	}
	public String getBasicDataJsonStr() {
		return basicDataJsonStr;
	}
	public void setBasicDataJsonStr(String basicDataJsonStr) {
		this.basicDataJsonStr = basicDataJsonStr;
	}
	/**
	 * 将basicDataJsonStr转换为对应的basicDataClassPath的jsonObject
	 * 将其远程保存到运行系统数据库的表中
	 * @param projectId
	 * @return
	 */
	@JSONField(serialize = false)
	public JSONObject getBasicDataJsonObject(String projectId) {
		JSONObject tmpJson = JSONObject.parseObject(basicDataJsonStr);
		JSONObject json = new JSONObject(tmpJson.size()+2);
		Set<String> keys = tmpJson.keySet();
		for (String key : keys) {
			if(key.endsWith("Date")){
				json.put(key, tmpJson.getDate(key));
			}else{
				json.put(key, tmpJson.getString(key));
			}
		}
		tmpJson.clear();
		json.put(ResourceNameConstants.ID, ResourceHandlerUtil.getIdentity());
		json.put("projectId", projectId);
		return json;
	}
	
	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata("COM_PUBLISH_BASIC_DATA", 0);
		table.setName("要发布的基础数据资源对象表");
		table.setComments("要发布的基础数据资源对象表");
		table.setIsResource(1);
		table.setIsBuiltin(1);
		table.setIsNeedDeploy(0);
		table.setIsCreated(1);
		table.setBelongPlatformType(CONFIG_PLATFORM);
		
		List<ComColumndata> columns = new ArrayList<ComColumndata>(15);
		
		ComColumndata basicDataResourceNameColumn = new ComColumndata("basic_data_resource_name", DataTypeConstants.STRING, 60);
		basicDataResourceNameColumn.setName("基础数据所对应的资源名");
		basicDataResourceNameColumn.setComments("基础数据所对应的资源名：例如：数据字典对应的资源名为ComDataDictionary");
		basicDataResourceNameColumn.setOrderCode(1);
		columns.add(basicDataResourceNameColumn);
		
		ComColumndata basicDataJsonStrColumn = new ComColumndata("basic_data_json_str", DataTypeConstants.STRING, 1500);
		basicDataJsonStrColumn.setName("基础数据的json字符串内容");
		basicDataJsonStrColumn.setComments("基础数据的json字符串内容");
		basicDataJsonStrColumn.setOrderCode(2);
		columns.add(basicDataJsonStrColumn);
		
		table.setColumns(columns);
		return table;
	}
	
	public String toDropTable() {
		return "COM_PUBLISH_BASIC_DATA";
	}
	
	public String getEntityName() {
		return "ComPublishBasicData";
	}

	public JSONObject toEntityJson() {
		EntityJson entityJson = new EntityJson(JsonUtil.toJsonObject(this));
		super.processSysResourceProps(entityJson);
		return entityJson.getEntityJson();
	}

	public Integer getResourceType() {
		return BASIC_DATA;
	}
	
	public ComSysResource turnToPublishResource(String projectId, String refResourceId) {
		throw new IllegalArgumentException("该资源目前不支持turnToPublishResource功能");
	}

	public ComSysResource turnToResource() {
		throw new IllegalArgumentException("该资源目前不支持turnToResource功能");
	}

	public ComPublishInfo turnToPublish() {
		throw new IllegalArgumentException("该资源目前不支持turnToPublish功能");
	}
}
