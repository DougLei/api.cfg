package org.opcfoundation.webservices.xmlda._1_0;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 3.0.4
 * 2015-03-20T16:07:36.745+08:00
 * Generated source version: 3.0.4
 * 
 */
@WebService(targetNamespace = "http://opcfoundation.org/webservices/XMLDA/1.0/", name = "OPC_XML_DA_WrapperServiceSoap")
@XmlSeeAlso({org.opcfoundation.webservices.xmlda._1.ObjectFactory.class})
public interface OPCXMLDAWrapperServiceSoap {

    @RequestWrapper(localName = "Read", targetNamespace = "http://opcfoundation.org/webservices/XMLDA/1.0/", className = "org.opcfoundation.webservices.xmlda._1.Read")
    @WebMethod(operationName = "Read", action = "http://opcfoundation.org/webservices/XMLDA/1.0/Read")
    @ResponseWrapper(localName = "ReadResponse", targetNamespace = "http://opcfoundation.org/webservices/XMLDA/1.0/", className = "org.opcfoundation.webservices.xmlda._1.ReadResponse")
    public void read(
        @WebParam(name = "Options", targetNamespace = "http://opcfoundation.org/webservices/XMLDA/1.0/")
        org.opcfoundation.webservices.xmlda._1.RequestOptions options,
        @WebParam(name = "ItemList", targetNamespace = "http://opcfoundation.org/webservices/XMLDA/1.0/")
        org.opcfoundation.webservices.xmlda._1.ReadRequestItemList itemList,
        @WebParam(mode = WebParam.Mode.OUT, name = "ReadResult", targetNamespace = "http://opcfoundation.org/webservices/XMLDA/1.0/")
        javax.xml.ws.Holder<org.opcfoundation.webservices.xmlda._1.ReplyBase> readResult,
        @WebParam(mode = WebParam.Mode.OUT, name = "RItemList", targetNamespace = "http://opcfoundation.org/webservices/XMLDA/1.0/")
        javax.xml.ws.Holder<org.opcfoundation.webservices.xmlda._1.ReplyItemList> rItemList,
        @WebParam(mode = WebParam.Mode.OUT, name = "Errors", targetNamespace = "http://opcfoundation.org/webservices/XMLDA/1.0/")
        javax.xml.ws.Holder<java.util.List<org.opcfoundation.webservices.xmlda._1.OPCError>> errors
    );

    @SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
    @WebResult(name = "WriteResponse", targetNamespace = "http://opcfoundation.org/webservices/XMLDA/1.0/", partName = "parameters")
    @WebMethod(operationName = "Write", action = "http://opcfoundation.org/webservices/XMLDA/1.0/Write")
    public org.opcfoundation.webservices.xmlda._1.WriteResponse write(
        @WebParam(partName = "parameters", name = "Write", targetNamespace = "http://opcfoundation.org/webservices/XMLDA/1.0/")
        org.opcfoundation.webservices.xmlda._1.Write parameters
    );

    @SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
    @WebResult(name = "SubscribeResponse", targetNamespace = "http://opcfoundation.org/webservices/XMLDA/1.0/", partName = "parameters")
    @WebMethod(operationName = "Subscribe", action = "http://opcfoundation.org/webservices/XMLDA/1.0/Subscribe")
    public org.opcfoundation.webservices.xmlda._1.SubscribeResponse subscribe(
        @WebParam(partName = "parameters", name = "Subscribe", targetNamespace = "http://opcfoundation.org/webservices/XMLDA/1.0/")
        org.opcfoundation.webservices.xmlda._1.Subscribe parameters
    );

    @SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
    @WebResult(name = "BrowseResponse", targetNamespace = "http://opcfoundation.org/webservices/XMLDA/1.0/", partName = "parameters")
    @WebMethod(operationName = "Browse", action = "http://opcfoundation.org/webservices/XMLDA/1.0/Browse")
    public org.opcfoundation.webservices.xmlda._1.BrowseResponse browse(
        @WebParam(partName = "parameters", name = "Browse", targetNamespace = "http://opcfoundation.org/webservices/XMLDA/1.0/")
        org.opcfoundation.webservices.xmlda._1.Browse parameters
    );

    @SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
    @WebResult(name = "SubscriptionCancelResponse", targetNamespace = "http://opcfoundation.org/webservices/XMLDA/1.0/", partName = "parameters")
    @WebMethod(operationName = "SubscriptionCancel", action = "http://opcfoundation.org/webservices/XMLDA/1.0/SubscriptionCancel")
    public org.opcfoundation.webservices.xmlda._1.SubscriptionCancelResponse subscriptionCancel(
        @WebParam(partName = "parameters", name = "SubscriptionCancel", targetNamespace = "http://opcfoundation.org/webservices/XMLDA/1.0/")
        org.opcfoundation.webservices.xmlda._1.SubscriptionCancel parameters
    );

    @SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
    @WebResult(name = "SubscriptionPolledRefreshResponse", targetNamespace = "http://opcfoundation.org/webservices/XMLDA/1.0/", partName = "parameters")
    @WebMethod(operationName = "SubscriptionPolledRefresh", action = "http://opcfoundation.org/webservices/XMLDA/1.0/SubscriptionPolledRefresh")
    public org.opcfoundation.webservices.xmlda._1.SubscriptionPolledRefreshResponse subscriptionPolledRefresh(
        @WebParam(partName = "parameters", name = "SubscriptionPolledRefresh", targetNamespace = "http://opcfoundation.org/webservices/XMLDA/1.0/")
        org.opcfoundation.webservices.xmlda._1.SubscriptionPolledRefresh parameters
    );

    @SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
    @WebResult(name = "GetStatusResponse", targetNamespace = "http://opcfoundation.org/webservices/XMLDA/1.0/", partName = "parameters")
    @WebMethod(operationName = "GetStatus", action = "http://opcfoundation.org/webservices/XMLDA/1.0/GetStatus")
    public org.opcfoundation.webservices.xmlda._1.GetStatusResponse getStatus(
        @WebParam(partName = "parameters", name = "GetStatus", targetNamespace = "http://opcfoundation.org/webservices/XMLDA/1.0/")
        org.opcfoundation.webservices.xmlda._1.GetStatus parameters
    );

    @SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
    @WebResult(name = "GetPropertiesResponse", targetNamespace = "http://opcfoundation.org/webservices/XMLDA/1.0/", partName = "parameters")
    @WebMethod(operationName = "GetProperties", action = "http://opcfoundation.org/webservices/XMLDA/1.0/GetProperties")
    public org.opcfoundation.webservices.xmlda._1.GetPropertiesResponse getProperties(
        @WebParam(partName = "parameters", name = "GetProperties", targetNamespace = "http://opcfoundation.org/webservices/XMLDA/1.0/")
        org.opcfoundation.webservices.xmlda._1.GetProperties parameters
    );
}