package com.king.tooth.workflow.service;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;

import com.king.tooth.sys.service.AService;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.xml.Dom4jUtil;

/**
 * 流程服务处理器的抽象类
 * @author DougLei
 */
public class WfService extends AService{

	/**
	 * 根据节点名称，以及节点元素的id属性值，获取对应的节点元素对象
	 * @param xmlContent
	 * @param elementName
	 * @param elementIdAttrValue 
	 * @return
	 */
	protected Element getElementById(String xmlContent, String elementName, String elementIdAttrValue){
		return getElementById(Dom4jUtil.getDocumentByString(xmlContent), elementName, elementIdAttrValue);
	}
	
	/**
	 * 根据节点名称，以及节点元素的id属性值，获取对应的节点元素对象
	 * @param xmlContentByteArray
	 * @param elementName
	 * @param elementIdAttrValue 
	 * @return
	 */
	protected Element getElementById(byte[] xmlContentByteArray, String elementName, String elementIdAttrValue){
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
	private Element getElementById(Document document, String elementName, String elementIdAttrValue){
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
		
		Element processElement = rootElement.element("process");
		if(processElement == null){
			throw new NullPointerException("xml文档中，不存在[process]节点，请检查配置");
		}
		if(!processElement.hasContent()){
			throw new NullPointerException("xml文档中，节点元素["+processElement.getName()+"]下没有任何数据，请检查配置");
		}
		
		List<Element> elements = processElement.elements(elementName);
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
