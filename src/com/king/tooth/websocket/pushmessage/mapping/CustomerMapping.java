package com.king.tooth.websocket.pushmessage.mapping;

import java.util.HashMap;
import java.util.Map;

import com.king.tooth.util.CryptographyUtil;
import com.king.tooth.websocket.pushmessage.entity.Customer;

/**
 * 消息推送的客户信息的映射
 * @author DougLei
 */
public class CustomerMapping {
	private static final Map<String, Customer> customerMapping = new HashMap<String, Customer>(16);
	static{
		Customer customer = new Customer("48a3b6dfad19478592e936071d0582bb", "SmartOne", "1QaZ2wSx,.", "西安博道工业科技有限公司");
		customerMapping.put(customer.getCustomerToken(), customer);
	}
	
	/**
	 * 添加客户
	 * @param customer
	 * @return 
	 */
	public static Object addCustomer(Customer customer){
		if(customerMapping.containsKey(customer.getId())){
			return "已经存在["+customer.getUserName()+"]客户，添加失败";
		}
		customerMapping.put(customer.getCustomerToken(), customer);
		return customer;
	}
	
	/**
	 * 删除客户
	 * @param token
	 * @return 
	 */
	public static Object removeCustomer(String customerUserName, String customerPassword){
		String customerToken = CryptographyUtil.encodeMd5(customerUserName, customerPassword);
		if(!customerMapping.containsKey(customerToken)){
			return "该客户["+customerUserName+"]不存在，删除失败";
		}
		return customerMapping.remove(customerToken);
	}
	
	/**
	 * 根据token，获取对应的客户对象
	 * @param customerToken
	 * @return
	 */
	public static Customer getCustomer(String customerToken){
		return customerMapping.get(customerToken);
	}
	
	/**
	 * 根据token，判断是否存在对应的客户对象
	 * @param customerToken
	 * @return
	 */
	public static boolean customerIsExists(String customerToken){
		return customerMapping.containsKey(customerToken);
	}
}
