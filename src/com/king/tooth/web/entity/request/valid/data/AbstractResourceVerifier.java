package com.king.tooth.web.entity.request.valid.data;

import java.util.List;
import com.king.tooth.sys.entity.tools.resource.metadatainfo.ResourceMetadataInfo;
import com.king.tooth.web.entity.request.RequestBody;

/**
 * 资源的数据校验父类
 * @author DougLei
 */
public abstract class AbstractResourceVerifier {

	protected RequestBody requestBody;
	protected String resourceName;
	protected String parentResourceName;
	
	public AbstractResourceVerifier(RequestBody requestBody) {
		this.requestBody = requestBody;
		this.resourceName = requestBody.getResourceName();
		this.parentResourceName = requestBody.getParentResourceName();
	}

	/**
	 * 资源的元数据信息集合
	 */
	protected List<ResourceMetadataInfo> resourceMetadataInfos;
	/**
	 * 父资源的元数据信息集合
	 */
	protected List<ResourceMetadataInfo> parentResourceMetadataInfos;
	
	public void clear(){
		if(!requestBody.isGetRequest()){
			if(resourceMetadataInfos != null && resourceMetadataInfos.size() > 0){
				resourceMetadataInfos.clear();
			}
			if(parentResourceMetadataInfos != null && parentResourceMetadataInfos.size() > 0){
				parentResourceMetadataInfos.clear();
			}
		}
	}

	// ------------------------------------------------------------------------------------------
	/**
	 * 验证
	 * @return
	 */
	public abstract String doValid();
}
