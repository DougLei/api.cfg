package com.king.tooth.workflow.service.wf;

import java.util.List;

import com.king.tooth.thread.current.CurrentThreadContext;
import com.king.tooth.util.hibernate.HibernateUtil;
import com.king.tooth.workflow.entity.wf.re.WfReProcdef;
import com.king.tooth.workflow.entity.wf.ru.WfRuExecutioninst;
import com.king.tooth.workflow.service.WfService;
import com.king.tooth.workflow.util.ProcessDefUtil;

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

		ProcessDefUtil.getElementById((byte[])processDefinedContent, "", "");
		
		return processInstance;
	}
	
	/**
	 * 根据流程的id启动一条流程
	 * @param processDefKey
	 */
	public void startProcessById(String processId){
		
	}
}
