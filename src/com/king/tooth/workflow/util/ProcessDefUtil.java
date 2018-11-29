package com.king.tooth.workflow.util;

import org.dom4j.Document;
import org.dom4j.Element;

import com.king.tooth.util.StrUtils;
import com.king.tooth.util.xml.Dom4jUtil;

/**
 * 流程定义的工具类
 * @author DougLei
 */
public class ProcessDefUtil {
	
	/**
	 * 验证流程的xml配置信息
	 * @param processXmlConfig
	 * @return 如果返回null，则证明验证通过，否则返回验证失败的原因
	 */
	public static String validProcessXmlConfig(String processXmlConfig){
		// TODO
		return null;
	}
	
	/**
	 * 验证(流程)页面布局的xml配置信息
	 * @param pageLayoutXmlConfig
	 * @return 如果返回null，则证明验证通过，否则返回验证失败的原因
	 */
	public static String validPageLayoutXmlConfig(String pageLayoutXmlConfig){
		// TODO
		return null;
	}
	
	// ------------------------------------------------------------------------------------------------------
	
	/**
	 * 根据元素的名称，或其id属性值，获取对应的Element对象
	 * <p>如果elementName和elementIdAttrValue都为null，则返回根元素对象</p>
	 * @param xmlContent
	 * @param elementName 
	 * @param elementIdAttrValue 
	 * @return
	 */
	public static Element getElement(String xmlContent, String elementName, String elementIdAttrValue){
		return getElement(Dom4jUtil.getDocumentByString(xmlContent), elementName, elementIdAttrValue);
	}
	
	/**
	 * 根据元素的名称，或其id属性值，获取对应的Element对象
	 * <p>如果elementName和elementIdAttrValue都为null，则返回根元素对象</p>
	 * @param xmlContent
	 * @param elementName 
	 * @param elementIdAttrValue 
	 * @return
	 */
	public static Element getElement(byte[] buf, String elementName, String elementIdAttrValue){
		return getElement(Dom4jUtil.getDocument(buf), elementName, elementIdAttrValue);
	}
	
	/**
	 * 根据元素的名称，或其id属性值，获取对应的Element对象
	 * <p>如果elementName和elementIdAttrValue都为null，则返回根元素对象</p>
	 * @param xmlContent
	 * @param elementName 
	 * @param elementIdAttrValue 
	 * @return
	 */
	public static Element getElement(Document document, String elementName, String elementIdAttrValue){
		return getElement(document.getRootElement(), elementName, elementIdAttrValue);
	}
	
	/**
	 * 根据元素的名称，或其id属性值，获取对应的Element对象
	 * <p>如果elementName和elementIdAttrValue都为null，则返回根元素对象</p>
	 * @param basicElement
	 * @param elementName 
	 * @param elementIdAttrValue 
	 * @return
	 */
	public static Element getElement(Element basicElement, String elementName, String elementIdAttrValue){
		if(StrUtils.isEmpty(elementName) && StrUtils.isEmpty(elementIdAttrValue)){
			return basicElement;
		}
		
		Element element = null;
		
		
		if(element == null){
			throw new NullPointerException("xml文档中，不存在id属性值为["+elementIdAttrValue+"]的元素，请检查配置");
		}
		return element;
	}
}
