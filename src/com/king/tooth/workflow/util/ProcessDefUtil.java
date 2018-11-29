package com.king.tooth.workflow.util;

import java.util.List;

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
	 * 根据节点名称，以及节点元素的id属性值，获取对应的节点元素对象
	 * @param xmlContent
	 * @param elementName
	 * @param elementIdAttrValue 
	 * @return
	 */
	public static Element getElementById(String xmlContent, String elementName, String elementIdAttrValue){
		return getElementById(Dom4jUtil.getDocumentByString(xmlContent), elementName, elementIdAttrValue);
	}
	
	/**
	 * 根据节点名称，以及节点元素的id属性值，获取对应的节点元素对象
	 * @param xmlContentByteArray
	 * @param elementName
	 * @param elementIdAttrValue 
	 * @return
	 */
	public static Element getElementById(byte[] xmlContentByteArray, String elementName, String elementIdAttrValue){
		return getElementById(Dom4jUtil.getDocument(xmlContentByteArray), elementName, elementIdAttrValue);
	}
	
	/**
	 * 根据节点名称，以及节点元素的id属性值，获取对应的节点元素对象
	 * @param document
	 * @param elementName
	 * @param elementIdAttrValue
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static Element getElementById(Document document, String elementName, String elementIdAttrValue){
		if(StrUtils.isEmpty(elementName)){
			throw new NullPointerException("在根据节点名称，以及节点元素的id属性值，获取对应的节点元素对象时，传入的elementName值不能为空");
		}
		if(StrUtils.isEmpty(elementIdAttrValue)){
			throw new NullPointerException("在根据节点名称，以及节点元素的id属性值，获取对应的节点元素对象时，传入的elementIdAttrValue值不能为空");
		}
		
		Element rootElement = document.getRootElement();
		if(!rootElement.hasContent()){
			throw new NullPointerException("xml文档中，根节点元素["+rootElement.getName()+"]下没有任何数据，请检查配置");
		}
		List<Element> elements = rootElement.elements(elementName);
		if(elements == null || elements.size() == 0){
			throw new NullPointerException("xml文档中，不存在节点名为["+elementName+"]的数据，请检查配置");
		}
		
		Element targetElement = null;
		for (Element element : elements) {
			if(elementIdAttrValue.equals(element.attributeValue("id"))){
				targetElement = element;
				break;
			}
		}
		elements.clear();
		
		if(targetElement == null){
			throw new NullPointerException("xml文档中，不存在id属性值为["+elementIdAttrValue+"]的数据，请检查配置");
		}
		return targetElement;
	}
}
