package com.api.soap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;

import org.opcfoundation.webservices.xmlda._1.ItemValue;
import org.opcfoundation.webservices.xmlda._1.ReadRequestItem;
import org.opcfoundation.webservices.xmlda._1.ReadRequestItemList;
import org.opcfoundation.webservices.xmlda._1.ReadResponse;
import org.w3c.dom.NodeList;

import com.alibaba.fastjson.JSONObject;
import com.api.cache.SysContext;

/**
 * 
 * @author panda fang
 * @since 2015-03-15
 */
public class SOAPClient {
	private SOAPClient(){}
	private static final SOAPClient client = new SOAPClient();
	
	private static String endpoint = SysContext.getSystemConfig("soap.url"); 
	private static String namespace = "http://opcfoundation.org/webservices/XMLDA/1.0/";
	
	public static SOAPClient instance(){
		return client;
	}
	
	public String read(List<OPCData> opcDataList) throws SOAPException, IOException, JAXBException {
		if (opcDataList == null) {
			throw new NullPointerException("opcDataList can not be null");
		}

		// 创建消息对象
		// ===========================================
		SOAPMessage message = MessageFactory.newInstance().createMessage();
		SOAPHeader header = message.getSOAPHeader();
		header.detachNode();

		MimeHeaders mh = message.getMimeHeaders();
		mh.addHeader("SOAPAction", "http://opcfoundation.org/webservices/XMLDA/1.0/Read");

		// 获取soap的信封
		SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
		envelope.setPrefix("soap");

		// 获取消息的body
		SOAPBody body = message.getSOAPBody();
		body.setPrefix("soap");
		QName bodyName = new QName(namespace, "Read");
		SOAPBodyElement bodyElement = body.addBodyElement(bodyName);

		SOAPElement se = bodyElement.addChildElement("Options");
		se.setAttribute("LocaleID", "en-US");
		se.setAttribute("ClientRequestHandle", "");
		se.setAttribute("ReturnItemTime", "true");
		se.setAttribute("ReturnItemName", "true");

		se = bodyElement.addChildElement("ItemList");

		ReadRequestItemList reqItemList = new ReadRequestItemList();
		int len = opcDataList.size();
		List<ReadRequestItem> items = reqItemList.getItems();
		List<ReadRequestItem> items2 = new ArrayList<ReadRequestItem>(len);
		items.addAll(items2);

		SOAPElement itemsElem = null;
		ReadRequestItem item = null;
		for (int i = 0; i < len; i++) {
			item = new ReadRequestItem();
			items.add(i, item);
			item.setClientItemHandle(String.valueOf(i));
			item.setItemName(opcDataList.get(i).getOPCName());

			itemsElem = se.addChildElement("Items");
			itemsElem.setAttribute("ItemName", item.getItemName());
			itemsElem.setAttribute("ClientItemHandle", item.getClientItemHandle());
		}

		SOAPConnection connection = SOAPConnectionFactory.newInstance().createConnection();
		SOAPMessage response = connection.call(message, endpoint);
		connection.close();

		SOAPBody responseBody = response.getSOAPBody();
		NodeList list = responseBody.getChildNodes();
		JAXBContext jaxbContext = JAXBContext.newInstance(ReadResponse.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		ReadResponse readResponse = (ReadResponse) jaxbUnmarshaller.unmarshal(list.item(1));
		List<ItemValue> replyItems = readResponse.getRItemList().getItems();

		if (replyItems.size() > 0) {
			int n = 0;
			OPCData opcdata = null;
			for (ItemValue v : replyItems) {
				n = Integer.parseInt(v.getClientItemHandle());
				opcdata = opcDataList.get(n);
				opcdata.setOPCValue(v.getValue().toString());
				opcdata.setOPCTime(v.getTimestamp().toString().replace('T', ' '));
			}
		}

		try {
			return JSONObject.toJSONString(opcDataList);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
