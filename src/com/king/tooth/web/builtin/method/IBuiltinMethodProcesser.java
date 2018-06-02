package com.king.tooth.web.builtin.method;


/**
 * 内置函数处理器类
 * @author DougLei
 */
public interface IBuiltinMethodProcesser {
	
	/**
	 * 获取拼接的最终数据库脚本语句
	 * <p>主要针对表资源处理使用到的</p>
	 * @return
	 */
	public StringBuilder getDBScriptStatement();
	
	/**
	 * 获取处理器的类型
	 * @return
	 */
	public int getProcesserType();
	
	/**
	 * 清除无效的数据
	 */
	public void clearInvalidMemory();
}
