package com.api.soap;

/**
 * 用于视图的数据结构
 * 
 * @author houzh renlq
 * @since 2019-03-15
 */
public class OPCData {

	/** OPC名称 */
	private String OPCName = "";

	/** OPC值 */
	private String OPCValue = "";

	/** OPC 值更新时间 */
	private String OPCTime = "";

	
	public OPCData() {
	}
	public OPCData(String oPCName) {
		OPCName = oPCName;
	}
	
	
	/**
	 * @return OPC名称
	 */
	public String getOPCName() {
		return OPCName;
	}
	
	/**
	 * @param oPCName the oPCName to set
	 */
	public void setOPCName(String oPCName) {
		OPCName = oPCName;
	}

	/**
	 * @return OPC值
	 */
	public String getOPCValue() {
		return OPCValue;
	}

	/**
	 * @param oPCValue the oPCValue to set
	 */
	public void setOPCValue(String oPCValue) {
		OPCValue = oPCValue; 
	}

	/**
	 * @return OPC 值更新时间
	 */
	public String getOPCTime() {
		return OPCTime;
	}

	/**
	 * @param oPCTime the oPCTime to set
	 */
	public void setOPCTime(String oPCTime) {
		OPCTime = oPCTime;
	}
}
