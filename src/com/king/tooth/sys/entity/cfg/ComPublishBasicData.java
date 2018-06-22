package com.king.tooth.sys.entity.cfg;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.EntityJson;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.ISysResource;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.util.JsonUtil;

/**
 * 要发布的基础数据资源对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ComPublishBasicData extends BasicEntity implements ITable, IEntity{
	
	/**
	 * 基础数据所对应的资源名
	 * <p>例如：数据字典对应的资源名为ComDataDictionary</p>
	 */
	private String basicDataResourceName;
	/**
	 * 基础数据的json字符串内容
	 */
	private String basicDataJsonStr;
	/**
	 * 资源所属的平台类型
	 * <p>1：配置平台、2：运行平台、3：通用</p>
	 * <p>后期开发的功能，如果是每个项目都需要的(基础功能)，则用这个字段控制是否要发布</p>
	 * <p>和isBuiltin有类似的作用，开放给前端开发使用，但还是不开放给用户</p>
	 * <p>isBuiltin控制的是系统内置的资源，belongPlatformType控制的是系统外置的资源</p>
	 * <p>@see ISysResource</p>
	 * <p>默认值：2</p>
	 */
	private Integer belongPlatformType;
	
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
	public Integer getBelongPlatformType() {
		return belongPlatformType;
	}
	public void setBelongPlatformType(Integer belongPlatformType) {
		this.belongPlatformType = belongPlatformType;
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
		JSONObject json = new JSONObject(tmpJson.size()+1);
		Set<String> keys = tmpJson.keySet();
		for (String key : keys) {
			if(key.endsWith("Date")){
				json.put(key, tmpJson.getDate(key));
			}else{
				json.put(key, tmpJson.getString(key));
			}
		}
		tmpJson.clear();
		json.put("projectId", projectId);
		return json;
	}
	
	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata("COM_PUBLISH_BASIC_DATA", 0);
		table.setName("要发布的基础数据资源对象表");
		table.setComments("要发布的基础数据资源对象表");
		table.setIsBuiltin(1);
		table.setIsNeedDeploy(0);
		table.setIsCreated(1);
		table.setBelongPlatformType(ISysResource.CONFIG_PLATFORM);
		
		List<ComColumndata> columns = new ArrayList<ComColumndata>(9);
		
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
		
		ComColumndata belongPlatformTypeColumn = new ComColumndata("belong_platform_type", DataTypeConstants.INTEGER, 1);
		belongPlatformTypeColumn.setName("资源所属的平台类型");
		belongPlatformTypeColumn.setComments("资源所属的平台类型:1：配置平台、2：运行平台、3：通用(这个类型由开发者控制)；后期开发的功能，如果是每个项目都需要的(基础功能)，则用这个字段控制是否要发布；和isBuiltin有类似的作用，开放给前端开发使用，但还是不开放给用户；isBuiltin控制的是系统内置的资源，belongPlatformType控制的是系统外置的资源");
		belongPlatformTypeColumn.setDefaultValue("2");
		belongPlatformTypeColumn.setOrderCode(3);
		columns.add(belongPlatformTypeColumn);
		
		table.setColumns(columns);
		return table;
	}
	
	public String toDropTable() {
		return "COM_PUBLISH_BASIC_DATA";
	}
	
	@JSONField(serialize = false)
	public String getEntityName() {
		return "ComPublishBasicData";
	}

	public JSONObject toEntityJson() {
		EntityJson entityJson = new EntityJson(JsonUtil.toJsonObject(this));
		super.processBasicEntityProps(entityJson);
		entityJson.put("belongPlatformType", belongPlatformType);
		return entityJson.getEntityJson();
	}
}
