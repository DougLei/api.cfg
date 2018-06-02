package com.king.tooth.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 关闭连接、流的工具类
 * @author DougLei
 */
public class CloseUtil {
	
	/**
	 * 关闭File-IO类
	 * @param ios
	 */
	public static void closeIO(Object... ios){
		if(ios == null || ios.length == 0){
			Log4jUtil.debug("[CloseUtil.close]要关闭的IO对象为null");
			return;
		}
		for (Object io : ios) {
			closeIO(io);
		}
	}
	
	/**
	 * 关闭数据库链接类
	 * @param dbconns
	 */
	public static void closeDBConn(Object... dbconns){
		if(dbconns == null || dbconns.length == 0){
			Log4jUtil.debug("[CloseUtil.closeDBConn]要关闭的dbconn对象为null");
			return;
		}
		for (Object dbconn : dbconns) {
			closeDBConn(dbconn);
		}
	}
	
	/**
	 * 关闭File-IO类
	 * @param io
	 */
	private static void closeIO(Object io){
		if(io == null){
			Log4jUtil.debug("[CloseUtil.close]要关闭的IO对象为null");
			return;
		}

		String ioClass = io.getClass().toString();
		Log4jUtil.debug("[CloseUtil.closeIO]要关闭的IO对象为:{}", ioClass);
		try {
			if(ioClass.contains("Writer")){
				Writer writer = (Writer) io;
				writer.flush();
				writer.close();
			}else if(ioClass.contains("Reader")){
				Reader reader = (Reader) io;
				reader.close();
			}else if(ioClass.contains("OutputStream")){
				OutputStream out = (OutputStream) io;
				out.flush();
				out.close();
			}else if(ioClass.contains("InputStream")){
				InputStream in = (InputStream) io;
				in.close();
			}else{
				Log4jUtil.debug("[CloseUtil.closeIO]没有匹配到要关闭的类型:{}", ioClass);
			}
		} catch (IOException e) {
			Log4jUtil.debug("[CloseUtil.closeIO]方法在关闭IO对象出现异常信息:{}", ExceptionUtil.getErrMsg(e));
		}finally{
			io = null;
		}
	}
	
	/**
	 * 关闭数据库连接
	 * @param dbconn
	 */
	private static void closeDBConn(Object dbconn){
		if(dbconn == null){
			Log4jUtil.debug("[CloseUtil.closeDBConn]要关闭的dbconn对象为null");
			return;
		}

		String dbconnClass = dbconn.getClass().toString();
		Log4jUtil.debug("[CloseUtil.closeDBConn]要关闭的dbconn对象为:{}", dbconnClass);
		try {
			if(dbconnClass.contains("Connection")){
				Connection conn = (Connection) dbconn;
				conn.close();
			}else if(dbconnClass.contains("Statement")){
				Statement st = (Statement) dbconn;
				st.close();
			}else if(dbconnClass.contains("ResultSet")){
				ResultSet rs = (ResultSet) dbconn;
				rs.close();
			}else{
				Log4jUtil.debug("[CloseUtil.closeDBConn]没有匹配到要关闭的类型:{}", dbconnClass);
			}
		} catch (SQLException e) {
			Log4jUtil.debug("[CloseUtil.closeDBConn]方法在关闭dbconn对象出现异常信息:{}", ExceptionUtil.getErrMsg(e));
		}finally{
			dbconn = null;
		}
	}
}
