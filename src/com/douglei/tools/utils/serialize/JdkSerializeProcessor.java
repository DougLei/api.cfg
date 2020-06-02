package com.douglei.tools.utils.serialize;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.douglei.tools.utils.CloseUtil;
import com.douglei.tools.utils.serialize.exception.DeserializeException;
import com.douglei.tools.utils.serialize.exception.SerializableException;

/**
 * jdk提供的序列化处理器
 * @author DougLei
 */
public class JdkSerializeProcessor extends SerializeProcessor{

	// ---------------------------------------------------------------------------------------------------
	// 序列化到byte数组、从byte数组反序列化
	// ---------------------------------------------------------------------------------------------------
	/**
	 * 序列化成byte数组
	 * @param object
	 * @return
	 */
	public static byte[] serialize2ByteArray(Object object) {
		isSerializable(object);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			return baos.toByteArray();
		} catch (Exception e) {
			throw new SerializableException(object, e);
		} finally {
			CloseUtil.closeIO(oos, baos);
		}
	}

	/**
	 * 根据指定的byte数组, 反序列化
	 * @param targetClass 要转换的目标类
	 * @param _byte
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T deserializeFromByteArray(Class<T> targetClass, byte[] _byte) {
		serializationByteArrayIsNull(_byte);
		
		ByteArrayInputStream bais = new ByteArrayInputStream(_byte);
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(bais);
			return (T) ois.readObject();
		} catch (Exception e) {
			throw new DeserializeException(targetClass, _byte, e);
		} finally {
			CloseUtil.closeIO(ois, bais);
		}
	}
	
	// ---------------------------------------------------------------------------------------------------
	// 序列化到文件、从文件反序列化
	// ---------------------------------------------------------------------------------------------------
	/**
	 * 序列化到文件
	 * @param object
	 * @param targetFile 目标文件的路径
	 * @return
	 */
	public static boolean serialize2File(Object object, String targetFile) {
		isSerializable(object);
		File file = getFile(targetFile);
		
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try {
			fos = new FileOutputStream(file);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(object);
			return true;
		} catch (Exception e) {
			throw new SerializableException(object, e);
		} finally {
			CloseUtil.closeIO(oos, fos);
		}
	}

	/**
	 * 根据指定的文件路径, 反序列化
	 * @param targetClass 要转换的目标类
	 * @param serializationFile
	 * @return
	 */
	public static <T> T deserializeFromFile(Class<T> targetClass, String serializationFile) {
		return deserializeFromFile(targetClass, new File(serializationFile));
	}
	
	/**
	 * 根据指定的文件, 反序列化
	 * @param targetClass 要转换的目标类
	 * @param serializationFile
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T deserializeFromFile(Class<T> targetClass, File serializationFile) {
		serializationFileExists(serializationFile);
		
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			fis = new FileInputStream(serializationFile);
			ois = new ObjectInputStream(fis);
			return (T) ois.readObject();
		} catch (Exception e) {
			throw new DeserializeException(targetClass, serializationFile, e);
		} finally {
			CloseUtil.closeIO(ois, fis);
		}
	}
}
