package com.api.web.entity.request.valid.data.impl;

import java.util.Set;

import com.api.plugins.ijson.IJson;
import com.api.sys.builtin.data.BuiltinParameterKeys;
import com.api.util.StrUtils;
import com.api.web.entity.request.RequestBody;
import com.api.web.entity.request.valid.data.AbstractResourceVerifier;
import com.api.web.entity.request.valid.data.util.TableResourceValidUtil;

/**
 * 表资源的数据校验类
 * @author DougLei
 */
public class TableResourceVerifier extends AbstractResourceVerifier{
	
	public TableResourceVerifier(RequestBody requestBody) {
		super(requestBody);
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
		resourceMetadataInfos = TableResourceValidUtil.getTableResourceMetadataInfos(requestBody.getResourceInfo().getReqResource());
		
		if(requestBody.isParentSubResourceQuery()){
			if(requestBody.isRecursiveQuery()){
				parentResourceMetadataInfos = resourceMetadataInfos;
			}else{
				parentResourceMetadataInfos = TableResourceValidUtil.getTableResourceMetadataInfos(requestBody.getResourceInfo().getReqParentResource());
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
			if(TableResourceValidUtil.validPropUnExists(true, propName, resourceMetadataInfos)){
				return "操作表资源["+resourceName+"]时，不存在名为["+propName+"]的属性";
			}
		}
		
		if(requestBody.isParentSubResourceQuery()){
			requestResourcePropNames = requestBody.getRequestParentResourceParams().keySet();
			for (String propName : requestResourcePropNames) {
				if(TableResourceValidUtil.validPropUnExists(true, propName, parentResourceMetadataInfos)){
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
		return TableResourceValidUtil.validTableResourceMetadata("", resourceName, resourceMetadataInfos, ijson, isUpdate, true);
//		return TableResourceValidUtil.validTableResourceMetadata("操作表资源["+resourceName+"]时，", resourceName, resourceMetadataInfos, ijson, isUpdate, true);// TODO 暂时注释，使用上面一行code
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
