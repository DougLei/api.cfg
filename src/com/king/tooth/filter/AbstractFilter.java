package com.king.tooth.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.ServletResponse;

import com.king.tooth.util.CloseUtil;
import com.king.tooth.web.entity.resulttype.ResponseBody;

/**
 * 抽象filter类
 * @author DougLei
 */
public abstract class AbstractFilter implements Filter{
	
	/**
	 * 打印结果
	 * @param message
	 * @param resp
	 * @throws IOException 
	 */
	protected void printResult(String message, ServletResponse resp) throws IOException{
		ResponseBody responseBody = new ResponseBody(message, null);
		PrintWriter out = resp.getWriter();
		out.write(responseBody.toStrings());
		CloseUtil.closeIO(out);
	}
}
