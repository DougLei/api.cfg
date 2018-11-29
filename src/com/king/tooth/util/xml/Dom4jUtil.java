package com.king.tooth.util.xml;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import com.king.tooth.constants.EncodingConstants;
import com.king.tooth.util.CloseUtil;
import com.king.tooth.util.ExceptionUtil;
import com.king.tooth.util.StrUtils;

/**
 * dom4j操作工具类
 * @author DougLei
 */
public class Dom4jUtil {
	
	/**
	 * 根据xml的文件绝对路径，获取document对象
	 * @param xmlFilePath
	 * @return
	 */
	public static Document getDocumentByXmlFilePath(String xmlFilePath){
		File xmlFile = new File(xmlFilePath);
		return getDocument(xmlFile);
	}
	
	/**
	 * 根据xml的文件，获取document对象
	 * @param xmlFile
	 * @return
	 */
	public static Document getDocument(File xmlFile){
		if(!xmlFile.exists() || xmlFile.isDirectory()){
			throw new NullPointerException("路径["+xmlFile.getAbsolutePath()+"]下，不存在["+xmlFile.getName()+"]文件");
		}
		Document document = null;
		try {
			SAXReader reader = new SAXReader();
			document = reader.read(xmlFile);
		} catch (DocumentException e) {
			throw new IllegalArgumentException("在获取dom4j的Document对象出现异常：" + ExceptionUtil.getErrMsg(e));
		}
		return document;
	}
	
	/**
	 * 根据xml字符串，获取document对象
	 * @param xmlContent
	 * @return
	 */
	public static Document getDocumentByString(String xmlContent){
		if(StrUtils.isEmpty(xmlContent)){
			throw new NullPointerException("根据xml字符串，获取document对象时，传入的xml内容字符串为空");
		}
		Document document = null;
		try {
			document = getDocument(xmlContent.getBytes(EncodingConstants.UTF_8));
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException("在获取dom4j的Document对象出现异常：" + ExceptionUtil.getErrMsg(e));
		}
		return document;
	}
	
	/**
	 * 根据byte数组，获取document对象
	 * @param xmlContentByteArray
	 * @return
	 */
	public static Document getDocument(byte[] xmlContentByteArray){
		if(xmlContentByteArray == null || xmlContentByteArray.length == 0){
			throw new NullPointerException("根据byte数组，获取document对象时，传入的byte数组为空");
		}
		Document document = null;
		InputStream in = null;
		try {
			in = new ByteArrayInputStream(xmlContentByteArray);
			SAXReader reader = new SAXReader();
			document = reader.read(in);
		} catch (DocumentException e) {
			throw new IllegalArgumentException("在获取dom4j的Document对象出现异常：" + ExceptionUtil.getErrMsg(e));
		} finally{
			CloseUtil.closeIO(in);
		}
		return document;
	}
}
