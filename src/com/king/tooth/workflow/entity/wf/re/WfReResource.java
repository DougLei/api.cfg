package com.king.tooth.workflow.entity.wf.re;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.annotation.Table;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.EncodingConstants;
import com.king.tooth.constants.ResourceInfoConstants;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.IEntityPropAnalysis;
import com.king.tooth.sys.entity.cfg.CfgColumn;
import com.king.tooth.sys.entity.cfg.CfgTable;
import com.king.tooth.util.ExceptionUtil;
import com.king.tooth.util.StrUtils;
import com.king.tooth.workflow.constants.WorkflowBasicInfoConstants;
import com.king.tooth.workflow.entity.WfBasicEntity;

/**
 * 流程资源表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class WfReResource extends WfBasicEntity implements IEntity, IEntityPropAnalysis{

	/**
	 * 关联的部署id
	 */
	private String refDeployId;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 资源文件类型
	 * <p>0:流程配置资源文件、1:流程图片布局配置资源文件、2:流程配置生成的图片文件</p>
	 */
	private String resourceFileType;
	/**
	 * 二进制内容
	 */
	private byte[] byteContent;
	
	//-------------------------------------------------------------------------
	
	/**
	 * 流程配置的文件内容
	 */
	@JSONField(serialize = false)
	private String processConfigFileContent;
	
	public String getRefDeployId() {
		return refDeployId;
	}
	public void setRefDeployId(String refDeployId) {
		this.refDeployId = refDeployId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getResourceFileType() {
		return resourceFileType;
	}
	public void setResourceFileType(String resourceFileType) {
		this.resourceFileType = resourceFileType;
	}
	public byte[] getByteContent() {
		return byteContent;
	}
	public void setByteContent(byte[] byteContent) {
		this.byteContent = byteContent;
	}
	
	@JSONField(serialize = false)
	public List<CfgColumn> getColumnList() {
		List<CfgColumn> columns = new ArrayList<CfgColumn>(3+7);
		
		CfgColumn refDeployIdColumn = new CfgColumn("ref_deploy_id", DataTypeConstants.STRING, 32);
		refDeployIdColumn.setName("关联的部署id");
		refDeployIdColumn.setComments("关联的部署id");
		columns.add(refDeployIdColumn);
		
		CfgColumn nameColumn = new CfgColumn("name", DataTypeConstants.STRING, 300);
		nameColumn.setName("名称");
		nameColumn.setComments("名称");
		columns.add(nameColumn);
		
		CfgColumn resourceFileTypeColumn = new CfgColumn("resource_file_type", DataTypeConstants.INTEGER, 1);
		resourceFileTypeColumn.setName("资源文件类型");
		resourceFileTypeColumn.setComments("0:流程配置资源文件、1:流程图片布局配置资源文件、2:流程配置生成的图片文件");
		columns.add(resourceFileTypeColumn);
		
		CfgColumn byteContentsColumn = new CfgColumn("byte_content", DataTypeConstants.BLOB, 0);
		byteContentsColumn.setName("二进制内容");
		byteContentsColumn.setComments("二进制内容");
		columns.add(byteContentsColumn);
		
		return columns;
	}
	
	public CfgTable toCreateTable() {
		CfgTable table = new CfgTable(toDropTable());
		table.setName("流程资源表");
		table.setRemark("流程资源表");
		table.setRequestMethod(ResourceInfoConstants.GET);
		
		table.setColumns(getColumnList());
		return table;
	}

	public String toDropTable() {
		return "WF_RE_RESOURCE";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "WfReResource";
	}
	
	public String validNotNullProps() {
		return null;
	}
	
	public String analysisResourceProp() {
		String result = validNotNullProps();
		if(result == null){
		}
		return result;
	}
	
	
	/**
	 * 获取流程配置的文件内容
	 * @return
	 */
	public String getProcessConfigFileContent(){
		if(isProcessConfigFile() && processConfigFileContent == null){
			processConfigFileContent = StrUtils.getStringByByteArray(byteContent);
		}
		return processConfigFileContent;
	}
	public void setProcessConfigFileContent(String processConfigFileContent) {
		if(StrUtils.notEmpty(processConfigFileContent)){
			this.processConfigFileContent = processConfigFileContent;
			try {
				this.byteContent = processConfigFileContent.getBytes(EncodingConstants.UTF_8);
			} catch (UnsupportedEncodingException e) {
				throw new IllegalArgumentException("在set流程配置的xml文件内容时，将字符串转换为utf8编码格式的byte数组时出现异常：" + ExceptionUtil.getErrMsg(e));
			}
		}
	}
	
	/**
	 * 是否是流程配置的文件
	 * @return
	 */
	@JSONField(serialize = false)
	public boolean isProcessConfigFile(){
		return name.toLowerCase().endsWith("xml");
	}
	/**
	 * 是否是流程配置生成的流程图片文件
	 * @return
	 */
	@JSONField(serialize = false)
	public boolean isProcessConfigImageFile(){
		return name.toLowerCase().endsWith(WorkflowBasicInfoConstants.PROCESS_CONFIG_IMAGE_FILE_SUFFIX);
	}
}
