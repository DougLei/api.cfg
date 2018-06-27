package com.king.tooth.sys.service.common;

import com.king.tooth.sys.entity.common.ComCode;
import com.king.tooth.sys.service.AbstractService;

/**
 * 代码资源服务处理器
 * @author DougLei
 */
public class ComCodeService extends AbstractService{
	
	/**
	 * 根据id，获取代码资源对象
	 * @param codeId
	 * @return
	 */
	public ComCode findCodeResourceById(String codeId){
		ComCode code = getObjectById(codeId, ComCode.class);
		if(code == null){
			throw new NullPointerException("不存在请求的代码资源，请联系管理员");
		}
		if(code.getIsEnabled() == 0){
			throw new IllegalArgumentException("请求的代码资源被禁用，请联系管理员");
		}
		return code;
	}
}
