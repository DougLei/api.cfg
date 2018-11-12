package com.king.tooth.sys.entity.cfg.busi.model.resource.data;

import java.io.Serializable;

import com.king.tooth.plugins.alibaba.json.extend.string.IJson;

/**
 * 业务模型的资源数据
 * <p>和每个业务模型资源关系(CfgBusiModelResRelations)对应</p>
 * <p>@see CfgBusiModelResRelations</p>
 * @author DougLei
 */
@SuppressWarnings("serial")
public class BusiModelResourceData implements Serializable{
	
	/**
	 * 数据的父级id
	 * <p>如果是根，则该值为null</p>
	 */
	private String dataParentId;
	/**
	 * 实际的数据
	 */
	private IJson data;

	// -----------------------------------------------------------
	public BusiModelResourceData() {
	}
	public BusiModelResourceData(String dataParentId, IJson ijsonData) {
		this.dataParentId = dataParentId;
		this.data = ijsonData;
	}
	
	// -----------------------------------------------------------
	public String getDataParentId() {
		return dataParentId;
	}
	public void setDataParentId(String dataParentId) {
		this.dataParentId = dataParentId;
	}
	public IJson getData() {
		return data;
	}
	public void setData(IJson data) {
		this.data = data;
	}
}
