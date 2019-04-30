package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

import com.api.util.CloseUtil;
import com.api.util.Log4jUtil;

public class Test {
	public static void main(String[] args) throws CloneNotSupportedException, UnsupportedEncodingException {
		final String key = "加强建筑施工企业劳保基金";
		
		new Thread(new Runnable() {
			public void run() {
				String s = null;
				for(int i=1;i<139;i++){
					s = doGetBasic("http://www.shaanxijs.gov.cn/zcfagui/list2030_"+i+".htm", null, null);
					System.out.print("DO");
					if(s.contains(key)){
						System.err.println("\n 公告公示：" + i + "\n");
					}
				}
			}
		}).start();
		
		new Thread(new Runnable() {
			public void run() {
				String s = null;
				for(int i=1;i<124;i++){
					s = doGetBasic("http://www.shaanxijs.gov.cn/zcfagui/list2031_"+i+".htm", null, null);
					System.out.print("DO");
					if(s.contains(key)){
						System.err.println("\n 省厅文件：" + i + "\n");
					}
				}
			}
		}).start();
	}
	
	
	/**
	 * 基础的get请求
	 * @param reqUrl
	 * @param params
	 * @param headers
	 * @return
	 */
	public static String doGetBasic(String reqUrl, Map<String, Object> urlParams, Map<String, String> headers){
		HttpClient httpClient = null;
		GetMethod getMethod = null;
		try {
			httpClient = new HttpClient();
			getMethod = new GetMethod(reqUrl);
			int status = httpClient.executeMethod(getMethod);// 执行
			Log4jUtil.debug("[HttpClientUtil.doGet]方法调用接口\"{}\"的结果为\"{}\"", reqUrl, status);
			return getResponseBodyAsString(getMethod);
		} catch (HttpException e) {
			Log4jUtil.debug("[HttpClientUtil.doGet]方法出现异常：{}", e.getMessage());
		} catch (IOException e) {
			Log4jUtil.debug("[HttpClientUtil.doGet]方法出现异常：{}", e.getMessage());
		}finally{
			if(getMethod != null){
				getMethod.releaseConnection();
			}
		}
		return null;
	}
	
	/**
	 * 获取响应体的string值
	 * @param method
	 * @return
	 * @throws IOException
	 */
	private static String getResponseBodyAsString(HttpMethod method) throws IOException{
		InputStream in = null;
		InputStreamReader inReader = null;
		BufferedReader reader = null;
		try {
			in = method.getResponseBodyAsStream();
			inReader = new InputStreamReader(in, "gb2312");
			reader = new BufferedReader(inReader);
			
			StringBuilder sb = new StringBuilder();
			String str = null;
			while((str=reader.readLine()) != null){
				sb.append(str);
			}
			return sb.toString();
		} finally{
			CloseUtil.closeIO(reader, inReader, in);
		}
	}
}
