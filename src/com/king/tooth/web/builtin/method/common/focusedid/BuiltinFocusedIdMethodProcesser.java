package com.king.tooth.web.builtin.method.common.focusedid;

import java.util.ArrayList;
import java.util.List;

import com.king.tooth.util.Log4jUtil;
import com.king.tooth.web.builtin.method.BuiltinMethodProcesserType;
import com.king.tooth.web.builtin.method.common.AbstractBuiltinCommonMethod;

/**
 * 内置聚焦函数处理器
 * @author DougLei
 */
public class BuiltinFocusedIdMethodProcesser extends AbstractBuiltinCommonMethod{
	
	/**
	 * 添加数据-聚焦的数据id
	 */
	private List<Object> addFocusedIds;
	/**
	 * 修改数据-聚焦的数据id
	 */
	private List<Object> editFocusedIds;
	/**
	 * 所有聚焦的数据id
	 */
	private String[] focusedId;
	
	public BuiltinFocusedIdMethodProcesser() {
		Log4jUtil.debug("此次请求，没有使用到BuiltinFocusedIdMethodProcesser内置方法处理器");
	}
	public BuiltinFocusedIdMethodProcesser(String focusedId) {
		isUsed = true;
		String[] focusedIdArr = focusedId.split(",");
		StringBuilder focusedIdBuffer = new StringBuilder();
		
		String[] tmp;
		for (String fid : focusedIdArr) {
			tmp = fid.split("_");
			if(tmp.length == 1){
				throw new IllegalArgumentException("定位数据时，传入的focusedId值格式不正确，请联系系统开发人员："+fid);
			}
			if("add".equals(tmp[1])){
				if(addFocusedIds == null){
					addFocusedIds = new ArrayList<Object>(focusedIdArr.length);
				}
				addFocusedIds.add(tmp[0]);
			}else if("edit".equals(tmp[1])){
				if(editFocusedIds == null){
					editFocusedIds = new ArrayList<Object>(focusedIdArr.length);
				}
				editFocusedIds.add(tmp[0]);
			}else{
				throw new IllegalArgumentException("定位数据时，传入的focusedId值出现异常，不应存在非_add或_edit的数据，请联系系统开发人员："+fid);
			}
			focusedIdBuffer.append(tmp[0]).append(",");
		}
		focusedIdBuffer.setLength(focusedIdBuffer.length()-1);
		
		this.focusedId = focusedIdBuffer.toString().split(",");
		focusedIdBuffer.setLength(0);
	}

	
	public List<Object> getAddFocusedIds() {
		return addFocusedIds;
	}
	public List<Object> getEditFocusedIds() {
		return editFocusedIds;
	}
	public String[] getFocusedId() {
		return focusedId;
	}
	
	public int getProcesserType() {
		return BuiltinMethodProcesserType.FOCUSED_ID;
	}
	
	public void clearInvalidMemory() {
		if(addFocusedIds != null && addFocusedIds.size() > 0){
			addFocusedIds.clear();
		}
		if(editFocusedIds != null && editFocusedIds.size() > 0){
			editFocusedIds.clear();
		}
	}
}
