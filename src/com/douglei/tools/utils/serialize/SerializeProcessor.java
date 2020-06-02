package com.douglei.tools.utils.serialize;

import java.io.File;
import java.io.Serializable;

import com.douglei.tools.utils.serialize.exception.NotSerializableException;
import com.douglei.tools.utils.serialize.exception.SerializeByteArrayIsNullException;
import com.douglei.tools.utils.serialize.exception.SerializeFileNotFoundException;


/**
 * 序列化处理器
 * @author DougLei
 */
abstract class SerializeProcessor {
	
	/**
	 * 是否可序列化
	 * @param object
	 */
	protected static void isSerializable(Object object) {
		if(!(object instanceof Serializable)) {
			throw new NotSerializableException(object);
		}
	}
	
	/**
	 * 是否存在序列化文件
	 * @param serializationFile
	 */
	protected static void serializationFileExists(File serializationFile) {
		if(!serializationFile.exists()) {
			throw new SerializeFileNotFoundException(serializationFile);
		}
	}
	
	/**
	 * byte数组是否为空
	 * @param _byte
	 */
	protected static void serializationByteArrayIsNull(byte[] _byte) {
		if(_byte == null || _byte.length == 0) {
			throw new SerializeByteArrayIsNullException();
		}
	}
	
	/**
	 * 获取<b>文件</b>对象, 如果文件上层的文件夹不存在则创建
	 * @param filePath 文件全路径  <b>包括文件名称</b>
	 * @return 
	 */
	protected static File getFile(String filePath) {
		File file = new File(filePath);
		
		File fileFolder = file.getParentFile();
		if(!fileFolder.exists()) {
			fileFolder.mkdirs();
		}
		return file;
	}
}
