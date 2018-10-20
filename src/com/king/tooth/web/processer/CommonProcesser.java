package com.king.tooth.web.processer;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;

import com.king.tooth.sys.builtin.data.BuiltinResourceInstance;
import com.king.tooth.sys.controller.sys.SysExcelController;
import com.king.tooth.web.builtin.method.common.export.file.create.BuiltinCreateExportFileMethodProcesser;
import com.king.tooth.web.entity.request.RequestBody;
import com.king.tooth.web.entity.resulttype.PageResultEntity;
import com.king.tooth.web.entity.resulttype.ResponseBody;

/**
 * 抽象的公用处理器
 * <p>目的在于提取公共的属性、方法</p>
 * <p>主要就是请求体和响应体</p>
 * <p>**任何新的处理器类型，都必须继承这个类**</p>
 * @author DougLei
 */
public abstract class CommonProcesser {

	/**
	 * 请求体
	 */
	protected RequestBody requestBody;
	
	/**
	 * 处理结果对象
	 */
	protected ResponseBody responseBody;
	
	/**
	 * 是否生成导出文件
	 * @param createExportFileProcesser
	 * @param pageResultEntity
	 * @param query
	 * @return
	 */
	protected final boolean isCreateExportFile(BuiltinCreateExportFileMethodProcesser createExportFileProcesser, PageResultEntity pageResultEntity, Query query){
		if(createExportFileProcesser.getIsUsed()){
			String resourceName = createExportFileProcesser.getResourceName();
			if(createExportFileProcesser.getParentResourceName() != null || resourceName.equals(createExportFileProcesser.getParentResourceName())){
				installResponseBodySimple("系统目前不支持生成主子资源导出的数据文件、以及递归资源导出数据文件", null);
			}else if(pageResultEntity == null){
				installResponseBodySimple("要生成导出文件，需要提供分页查询参数值：[_rows和_page] 或 [_limit和_start]，其中[_page或_start]的值传递为0即可", null);
			}else{
				Map<String, String> urlParams = requestBody.installAllUrlParams();
				Object obj = BuiltinResourceInstance.getInstance("SysExcelController", SysExcelController.class).createExportExcelFile(createExportFileProcesser.getResource(), createExportFileProcesser.getExportFileSuffix(), createExportFileProcesser.getExportTitle(), pageResultEntity, query, urlParams);
				if(urlParams != null && urlParams.size() > 0){
					urlParams.clear();
				}
				
				if(obj == null){
					installResponseBodySimple("[SysExcelController.createExportExcelFile]生成导出excel文件方法返回null，请联系后端系统开发人员", null);
				}else if(obj instanceof String){
					installResponseBodySimple(obj.toString(), null);
				}else{
					installResponseBodySimple(null, obj);
				}
			}
			return true;
		}
		return false;
	}
	
	/**
	 * 查询数据集合时，组装ResponseBody对象
	 * @param message
	 * @param dataList
	 * @param pageResultEntity
	 */
	protected final void installResponseBodyForQueryDataList(String message, List<Map<String, Object>> dataList, PageResultEntity pageResultEntity){
		Object data = dataList;
		if(pageResultEntity != null){
			// 分页查询，要先将结果集存储到pageResultEntity中，再把pageResultEntity存储到responseBody中
			pageResultEntity.setResultDatas(dataList);
			data = pageResultEntity;
		}
		installResponseBodySimple(message, data);
	}
	
	/**
	 * 简单组装ResponseBody对象
	 * @param message 
	 * @param data 
	 */
	protected final void installResponseBodySimple(String message, Object data){
		ResponseBody responseBody = new ResponseBody(message, data);;
		setResponseBody(responseBody);
	}
	
	/**
	 * 设置响应体
	 * <p>在处理器中使用到</p>
	 * <p>也可以在前、后处理器中使用到</p>
	 * @param data
	 */
	protected final void setResponseBody(ResponseBody responseBody){
		this.responseBody = responseBody;
	}
	
	/**
	 * 设置请求体对象
	 * @param requestBody
	 */
	public final void setRequestBody(RequestBody requestBody) {
		this.requestBody = requestBody;
	}
}
