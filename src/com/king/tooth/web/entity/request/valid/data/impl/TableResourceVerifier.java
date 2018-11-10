package com.king.tooth.web.entity.request.valid.data.impl;

import java.util.Set;

import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.sys.builtin.data.BuiltinParameterKeys;
import com.king.tooth.util.StrUtils;
import com.king.tooth.web.entity.request.RequestBody;
import com.king.tooth.web.entity.request.valid.data.AbstractResourceVerifier;
import com.king.tooth.web.entity.request.valid.data.util.TableResourceUtil;

/**
 * 表资源的数据校验类
 * @author DougLei
 */
public class TableResourceVerifier extends AbstractResourceVerifier{
	
	public TableResourceVerifier(RequestBody requestBody, String resourceName, String parentResourceName) {
		super(requestBody, resourceName, parentResourceName);
	}

	public String doValid(){
		return doValidTableResourceMetadata();
	}
	
	/**
	 * 验证表资源的元数据
	 * @return
	 */
	private String doValidTableResourceMetadata() {
		initTableResourceMetadataInfos();
		if(requestBody.isGetRequest()){
			return validGetTableResourceMetadata();
		}else if(requestBody.isPostRequest()){
			return validPostTableResourceMetadata(false);
		}else if(requestBody.isPutRequest()){
			return validPutTableResourceMetadata();
		}else if(requestBody.isDeleteRequest()){
			return validDeleteTableResourceMetadata();
		}
		return "table资源，系统只支持[get、post、put、delete]四种请求方式";
	}
	
	/**
	 * 初始化表资源元数据信息集合
	 * @return
	 */
	private void initTableResourceMetadataInfos() {
		resourceMetadataInfos = TableResourceUtil.getTableResourceMetadataInfos(requestBody.getResourceInfo().getReqResource());
		
		if(requestBody.isParentSubResourceQuery()){
			if(requestBody.isRecursiveQuery()){
				parentResourceMetadataInfos = resourceMetadataInfos;
			}else{
				parentResourceMetadataInfos = TableResourceUtil.getTableResourceMetadataInfos(requestBody.getResourceInfo().getReqParentResource());
			}
		}
	}
	
	/**
	 * 验证get请求的表资源数据
	 * @return
	 */
	private String validGetTableResourceMetadata() {
		Set<String> requestResourcePropNames = requestBody.getRequestResourceParams().keySet();
		for (String propName : requestResourcePropNames) {
			if(TableResourceUtil.validPropUnExists(true, propName, resourceMetadataInfos)){
				return "操作表资源["+resourceName+"]时，不存在名为["+propName+"]的属性";
			}
		}
		
		if(requestBody.isParentSubResourceQuery()){
			requestResourcePropNames = requestBody.getRequestParentResourceParams().keySet();
			for (String propName : requestResourcePropNames) {
				if(TableResourceUtil.validPropUnExists(true, propName, parentResourceMetadataInfos)){
					return "操作父表资源["+parentResourceName+"]时，不存在名为["+propName+"]的属性";
				}
			}
		}
		
		// 记录请求的查询资源的元数据信息集合
		requestBody.setQueryResourceMetadataInfos(resourceMetadataInfos);
		requestBody.setQueryParentResourceMetadataInfos(parentResourceMetadataInfos);
		return null;
	}
	
	/**
	 * 验证post请求的表资源数据
	 * @param isUpdate 是否是修改，如果是修改，则要验证id属性为空
	 * @return
	 */
	private String validPostTableResourceMetadata(boolean isUpdate) {
		IJson ijson = requestBody.getFormData();
		return TableResourceUtil.validTableResourceMetadata("操作表资源["+resourceName+"]时，", resourceName, resourceMetadataInfos, ijson, isUpdate, true);
	}
	
	/**
	 * 验证put请求的表资源数据
	 * @return
	 */
	private String validPutTableResourceMetadata() {
		return validPostTableResourceMetadata(true);
	}
	
	/**
	 * 验证delete请求的表资源数据
	 * @return
	 */
	private String validDeleteTableResourceMetadata() {
		if(StrUtils.isEmpty(requestBody.getRequestResourceParams().get(BuiltinParameterKeys._IDS))){
			return "要删除["+resourceName+"]资源时，"+BuiltinParameterKeys._IDS+"参数值不能为空";
		}
		return null;
	}
}
