package test;

import java.util.HashMap;
import java.util.Map;

import com.king.tooth.util.httpclient.HttpClientUtil;

public class TestApi {
	public static void main(String[] args) {
		HttpClientUtil.doPostBasic("http://localhost:8081/api.cfg/monitoring/data/publish", 
				getUrlParams("headerProjectId", "publishDataType", "publishType"), 
				null, 
				getHeaders("40621e47b80446fe853845eb782901bb"), 
				HttpClientUtil.getHttpStringRequestEntity("的发放的萨芬发送发送的dfadsfa范德萨", "text/json"));
	}
	
	/**
	 * 获取调用加载/卸载数据的api的参数map集合
	 * @param projectId 发布数据所属的projectId
	 * @param publishDataType 发布数据类型：[publishDataType = db、project、module、oper、table、sql]
	 * @param publishType 发布类型：[publishType = 1：发布/-1：取消发布]
	 * @return
	 */
	private static Map<String, Object> getUrlParams(String projectId, String publishDataType, String publishType){
		Map<String, Object> urlParams = new HashMap<String, Object>(4);
		urlParams.put("projectId", projectId);
		urlParams.put("publishDataType", publishDataType);
		urlParams.put("publishType", publishType);
//		urlParams.put("_token", "1");
		return urlParams;
	}
	
//	/**
//	 * 获取实际传递值的值
//	 * @param publishDataId 发布的数据主键
//	 * @return
//	 */
//	private static Map<String, String> getFormParams(String publishDataId) {
//		Map<String, String> formParams = new HashMap<String, String>(1);
//		formParams.put("publishDataId", publishDataId);
//		return formParams;
//	}
	
	/**
	 * 获取调用加载/卸载数据的api的headerMap集合
	 * <p>主要给header中设置_projId的值  @see PrepareFilter</p>
	 * @param projectId 
	 * @return
	 */
	private static Map<String, String> getHeaders(String projectId) {
		Map<String, String> header = new HashMap<String, String>(1);
		header.put("_projId", projectId);
		return header;
	}
}
