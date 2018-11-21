package com.king.tooth.sys.controller.cfg;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.king.tooth.annotation.Controller;
import com.king.tooth.annotation.RequestMapping;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.plugins.jdbc.table.DBTableHandler;
import com.king.tooth.sys.builtin.data.BuiltinParameterKeys;
import com.king.tooth.sys.builtin.data.BuiltinResourceInstance;
import com.king.tooth.sys.controller.AController;
import com.king.tooth.sys.entity.cfg.CfgColumn;
import com.king.tooth.sys.entity.cfg.CfgTable;
import com.king.tooth.sys.service.cfg.CfgColumnService;
import com.king.tooth.thread.current.CurrentThreadContext;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 字段信息表Controller
 * @author DougLei
 */
@Controller
public class CfgColumnController extends AController{
	
	private CfgTable getTableById(String tableId){
		CfgTable table = HibernateUtil.extendExecuteUniqueQueryByHqlArr(CfgTable.class, "from CfgTable where " + ResourcePropNameConstants.ID +"=?", tableId);
		if(table == null){
			throw new NullPointerException("不存在id值为'"+tableId+"'的CfgTable数据对象");
		}
		return table;
	}
	
	/**
	 * 添加列
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping
	public Object add(HttpServletRequest request, IJson ijson){
		List<CfgColumn> columns = getDataInstanceList(ijson, CfgColumn.class, true);
		analysisResourceProp(columns, false);
		if(analysisResult == null){
			CfgTable table = getTableById(columns.get(0).getTableId());
			
			DBTableHandler dbTableHandler = null;
			List<CfgColumn> addColumns = null;
			if(table.getIsCreated() == 1){// 表已经建模
				dbTableHandler = new DBTableHandler(CurrentThreadContext.getDatabaseInstance());
				addColumns = new ArrayList<CfgColumn>(columns.size());
			}
			
			for (CfgColumn column : columns) {
				resultObject = BuiltinResourceInstance.getInstance("CfgColumnService", CfgColumnService.class).saveColumn(table, column, addColumns, dbTableHandler);
				if(resultObject instanceof String){
					index++;
					resultObject = "第"+index+"个CfgColumn对象，" + resultObject;
					break;
				}
				resultJsonArray.add(resultObject);
			}
		}
		return getResultObject(columns, null);
	}
	
	/**
	 * 修改列
	 * <p>请求方式：PUT</p>
	 * @return
	 */
	@RequestMapping
	public Object update(HttpServletRequest request, IJson ijson){
		List<CfgColumn> columns = getDataInstanceList(ijson, CfgColumn.class, true);
		analysisResourceProp(columns, true);
		if(analysisResult == null){
			CfgTable table = getTableById(columns.get(0).getTableId());
			
			DBTableHandler dbTableHandler = null;
			List<CfgColumn> updateColumns = null;
			if(table.getIsCreated() == 1){// 表已经建模
				dbTableHandler = new DBTableHandler(CurrentThreadContext.getDatabaseInstance());
				updateColumns = new ArrayList<CfgColumn>(columns.size());
			}
			
			for (CfgColumn column : columns) {
				resultObject = BuiltinResourceInstance.getInstance("CfgColumnService", CfgColumnService.class).updateColumn(table, column, updateColumns, dbTableHandler);
				if(resultObject instanceof String){
					index++;
					resultObject = "第"+index+"个CfgColumn对象，" + resultObject;
					break;
				}
				resultJsonArray.add(resultObject);
			}
		}
		return getResultObject(columns, null);
	}
	
	/**
	 * 删除列
	 * <p>请求方式：DELETE</p>
	 * @return
	 */
	@RequestMapping
	public Object delete(HttpServletRequest request, IJson ijson){
		String columnIds = request.getParameter(BuiltinParameterKeys._IDS);
		if(StrUtils.isEmpty(columnIds)){
			return "要删除的列id不能为空";
		}
		resultObject = BuiltinResourceInstance.getInstance("CfgColumnService", CfgColumnService.class).deleteColumn(columnIds);
		processResultObject(BuiltinParameterKeys._IDS, columnIds);
		return getResultObject(null, null);
	}
}
