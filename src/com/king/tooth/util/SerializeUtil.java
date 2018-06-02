package com.king.tooth.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * 对象序列化工具类
 * @author DougLei
 */
public class SerializeUtil {
	
	/**
	 * 序列化对象为byte[]
	 * @param obj
	 * @return
	 */
	public static byte[] serializeObjectToByte(Object obj){
		if(obj instanceof Serializable){
			ByteArrayOutputStream out = null;
			ObjectOutputStream objectOut = null;
			try {
				out = new ByteArrayOutputStream();
				objectOut = new ObjectOutputStream(out);
				objectOut.writeObject(obj);
				
				byte[] bytes = out.toByteArray();
				return bytes;
			} catch (IOException e) {
				Log4jUtil.debug("[SerializeUtil.serializeObjectToByte]序列化对象出现异常信息:{}", ExceptionUtil.getErrMsg(e));
			}finally{
				CloseUtil.closeIO(objectOut);
				CloseUtil.closeIO(out);
			}
		}else{
			Log4jUtil.debug("[SerializeUtil.serializeObjectToByte]要序列化的对象没有实现Serializable接口");
		}
		return null;
	}
	
	
	/**
	 * 从byte[]反序列化对象，返回Object，需要强制转换
	 * @param bytes
	 * @return obj
	 */
	public static Object unserializeObjectFromByte(byte[] bytes){
		if(bytes == null || bytes.length == 0){
			Log4jUtil.debug("[SerializeUtil.unserializeObjectFromByte]要反序列化为对象的byte数据不存在");
			return null;
		}
		
		ByteArrayInputStream in = null;
		ObjectInputStream objectIn = null; 
		try {
			in = new ByteArrayInputStream(bytes);
			objectIn = new ObjectInputStream(in);
			return objectIn.readObject();
		} catch (IOException e) {
			Log4jUtil.debug("[SerializeUtil.unserializeObjectFromByte]反序列化对象出现异常信息:{}", ExceptionUtil.getErrMsg(e));
		} catch (ClassNotFoundException e) {
			Log4jUtil.debug("[SerializeUtil.unserializeObjectFromByte]反序列化对象出现异常信息:{}", ExceptionUtil.getErrMsg(e));
		}finally{
			CloseUtil.closeIO(objectIn);
			CloseUtil.closeIO(in);
		}
		return null;
	}
	/**
	 * 从byte[]反序列化对象，返回指定类型
	 * @param bytes
	 * @return T
	 */
	@SuppressWarnings("unchecked")
	public static <T> T unserializeObjectFromByte(byte[] bytes, Class<T> classType){
		return (T) unserializeObjectFromByte(bytes);
	}
	
	
	
	/**
	 * 序列化对象到文件
	 * @param obj
	 * @param path
	 */
	public static void serializeObjectToFile(Object obj, String path){
		if(obj instanceof Serializable){
			File file = new File(path);
			if(!file.exists()){
				file.mkdirs();
			}
			
			OutputStream out = null;
			ObjectOutputStream objectOut = null;
			try {
				out = new FileOutputStream(file);
				objectOut = new ObjectOutputStream(out);
				objectOut.writeObject(obj);
			} catch (FileNotFoundException e) {
				Log4jUtil.debug("[SerializeUtil.serializeObject]序列化的对象出现异常信息:{}", ExceptionUtil.getErrMsg(e));
			} catch (IOException e) {
				Log4jUtil.debug("[SerializeUtil.serializeObject]序列化的对象出现异常信息:{}", ExceptionUtil.getErrMsg(e));
			}finally{
				CloseUtil.closeIO(objectOut);
				CloseUtil.closeIO(out);
			}
		}else{
			Log4jUtil.debug("[SerializeUtil.serializeObjectToFile]要序列化的对象没有实现Serializable接口");
		}
	}

	
	/**
	 * 从文件反序列化对象，返回Object，需要强制转换
	 * @param path
	 * @return obj
	 */
	public static Object unserializeObjectFromFile(String path){
		File file = new File(path);
		if(file.exists()){
			Log4jUtil.debug("[SerializeUtil.unserializeObjectFromFile]要反序列化为对象的文件不存在");
			return null;
		}
		
		InputStream in = null;
		ObjectInputStream objectIn = null; 
		try {
			in = new FileInputStream(file);
			objectIn = new ObjectInputStream(in);
			return objectIn.readObject();
		} catch (FileNotFoundException e) {
			Log4jUtil.debug("[SerializeUtil.unserializeObjectFromFile]反序列化对象出现异常信息:{}", ExceptionUtil.getErrMsg(e));
		} catch (IOException e) {
			Log4jUtil.debug("[SerializeUtil.unserializeObjectFromFile]反序列化对象出现异常信息:{}", ExceptionUtil.getErrMsg(e));
		} catch (ClassNotFoundException e) {
			Log4jUtil.debug("[SerializeUtil.unserializeObjectFromFile]反序列化对象出现异常信息:{}", ExceptionUtil.getErrMsg(e));
		}finally{
			CloseUtil.closeIO(objectIn);
			CloseUtil.closeIO(in);
		}
		return null;
	}
	/**
	 * 从文件反序列化对象，返回指定类型
	 * @param path
	 * @return T
	 */
	@SuppressWarnings("unchecked")
	public static <T> T unserializeObjectFromFile(String path, Class<T> classType){
		return (T) unserializeObjectFromFile(path);
	}
}
