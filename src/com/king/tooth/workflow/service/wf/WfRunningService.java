package com.king.tooth.workflow.service.wf;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;

import com.king.tooth.thread.current.CurrentThreadContext;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.hibernate.HibernateUtil;
import com.king.tooth.util.xml.Dom4jUtil;
import com.king.tooth.workflow.entity.wf.re.WfReProcdef;
import com.king.tooth.workflow.entity.wf.ru.WfRuExecutioninst;
import com.king.tooth.workflow.service.WfService;

/**
 * 流程运行Service
 * @author DougLei
 */
public class WfRunningService extends WfService{
	
	/**
	 * 根据流程定义的key启动一条流程
	 * <p>启动版本最新的流程</p>
	 * @param processDefKey
	 */
	public WfRuExecutioninst startProcessByDefKey(String processDefKey){
		WfRuExecutioninst processInstance = null;
		
		// 获取流程的定义对象
		List<WfReProcdef> wfReProcdefList = HibernateUtil.extendExecuteListQueryByHqlArr(WfReProcdef.class, "1", "1", "from WfReProcdef where pKey=? and isActivate=1 and projectId=? and customerId=? order by version desc", processDefKey, CurrentThreadContext.getProjectId(), CurrentThreadContext.getCustomerId());
		if(wfReProcdefList == null || wfReProcdefList.size() == 0){
			processInstance = new WfRuExecutioninst();
			processInstance.setExceptionMessage("系统中不存在任何已激活状态下，key值为["+processDefKey+"]的流程信息，请检查");
			return processInstance;
		}
		
		Object processDefinedContent = HibernateUtil.executeUniqueQueryByHqlArr("from WfReResource where refDeployId=? and resourceFileType=0", wfReProcdefList.remove(0).getRefDeployId());
		if(processDefinedContent == null){
			processInstance = new WfRuExecutioninst();
			processInstance.setExceptionMessage("系统中key值为["+processDefKey+"]的流程，没有查询到对应的配置资源信息，请检查");
			return processInstance;
		}
		String processDefined = StrUtils.getStringByByteArray((byte[])processDefinedContent);
		
		// TODO 解析xml
		
		
		return processInstance;
	}
	
	public static void main(String[] args) throws Exception {
		String str = "<processDesign><process id=\"\" name=\"\" remark=\"哈哈\"></process></processDesign>";
		Document doc = Dom4jUtil.getDocumentByString(str);
		
		Element root = doc.getRootElement();
		System.out.println(root.getName());
	 
		Element processElement = root.element("processd");
		System.out.println(processElement == null);
	}
	
	/**
	 * 根据流程的id启动一条流程
	 * @param processDefKey
	 */
	public void startProcessById(String processId){
		
	}
}
