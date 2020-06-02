package com.douglei.tools.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.zip.ZipFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 关闭连接、流的工具类
 * @author DougLei
 */
public class CloseUtil {
	private static final Logger logger = LoggerFactory.getLogger(CloseUtil.class);
	
	/**
	 * 关闭File-IO类
	 * @param ios
	 */
	public static void closeIO(Object... ios){
		if(ios.length == 0){
			logger.debug("要关闭的ios对象集合为空");
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
		if(dbconns.length == 0){
			logger.debug("要关闭的dbconns对象集合为空");
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
	public static void closeIO(Object io){
		if(io == null){
			logger.debug("要关闭的io对象为空");
			return;
		}

		try {
			if(io instanceof Writer){
				Writer writer = (Writer) io;
				writer.flush();
				writer.close();
			}else if(io instanceof Reader){
				Reader reader = (Reader) io;
				reader.close();
			}else if(io instanceof OutputStream){
				OutputStream out = (OutputStream) io;
				out.flush();
				out.close();
			}else if(io instanceof InputStream){
				InputStream in = (InputStream) io;
				in.close();
			}else if(io instanceof ZipFile) {
				ZipFile zf = (ZipFile) io;
				zf.close();
			}else{
				logger.debug("没有匹配到[{}]的io对象", io.getClass().getName());
			}
		} catch (IOException e) {
			throw new UtilException("关闭io对象["+io.getClass().getName()+"]时, 出现异常", e);
		}
	}
	
	/**
	 * 关闭数据库连接
	 * @param dbconn
	 */
	public static void closeDBConn(Object dbconn){
		if(dbconn == null){
			logger.debug("要关闭的dbconn对象为空");
			return;
		}
		
		try {
			if(dbconn instanceof Connection){
				Connection conn = (Connection) dbconn;
				conn.close();
			}else if(dbconn instanceof Statement){
				Statement st = (Statement) dbconn;
				st.close();
			}else if(dbconn instanceof ResultSet){
				ResultSet rs = (ResultSet) dbconn;
				rs.close();
			}else{
				logger.debug("没有匹配到[{}]的dbconn对象", dbconn.getClass().getName());
			}
		} catch (SQLException e) {
			throw new UtilException("关闭dbconn对象["+dbconn.getClass().getName()+"]时, 出现异常", e);
		}
	}
}
