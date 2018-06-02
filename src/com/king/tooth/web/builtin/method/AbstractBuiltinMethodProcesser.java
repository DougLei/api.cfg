package com.king.tooth.web.builtin.method;

/**
 * 内置函数处理器类的抽象类，抽取具体实现类的共同属性和方法
 * @author DougLei
 */
public abstract class AbstractBuiltinMethodProcesser implements IBuiltinMethodProcesser {

	/**
	 * 是否使用到该处理器
	 * <p>用来判断当前对象是否可用</p>
	 */
	protected boolean isUsed;
	
	/**
	 * 是否解析过
	 */
	protected boolean isAnalysis;
	
	/**
	 * 别名
	 */
	protected String alias;
	
	/**
	 * 获取isUsed属性的值
	 * <p>判断当前对象是否可用</p>
	 * @return
	 */
	public boolean getIsUsed(){
		return isUsed;
	}
	
	/**
	 * 请求的资源名
	 */
	protected String resourceName;
	
	/**
	 * 请求的父资源名
	 */
	protected String parentResourceName;
	
	/**
	 * 请求的资源Id
	 */
	protected String resourceId;
	
	/**
	 * 请求的父资源Id
	 */
	protected String parentResourceId;
	
	/**
	 * 对外，设置请求的资源名
	 * @param resourceName
	 */
	public void setResourceName(String resourceName){
		this.resourceName = resourceName;
	}
	/**
	 * 对外，设置请求的父资源名
	 * @param parentResourceName
	 */
	public void setParentResourceName(String parentResourceName) {
		this.parentResourceName = parentResourceName;
	}
	/**
	 * 对外，设置请求的资源Id
	 * @param resourceId
	 */
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	/**
	 * 对外，设置请求的父资源Id
	 * @param parentResourceId
	 */
	public void setParentResourceId(String parentResourceId) {
		this.parentResourceId = parentResourceId;
	}
	
	
	/**
	 * 执行解析操作
	 * <p>判断内置函数对象是否可用，还要判断是否解析过参数</p>
	 * <p>这样是为了减少不必要的内存消耗[不在每个子处理器的构造函数中直接解析，而是通过调用该方法后再解析参数]</p>
	 */
	protected void execAnalysisParams() {
		if(isUsed && !isAnalysis){
			isAnalysis = true;
			execAnalysisParam();
		}
	}
	
	/**
	 * 执行解析操作
	 * <p>对execAnalysisParams()方法的再次封装，减少相同代码的开发，这个由子类实现</p>
	 * <p>该方法不对外开放</p>
	 */
	protected abstract void execAnalysisParam();
}
