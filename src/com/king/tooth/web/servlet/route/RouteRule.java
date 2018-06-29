package com.king.tooth.web.servlet.route;

import java.io.Serializable;

import com.king.tooth.util.StrUtils;

/**
 * 路由规则对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class RouteRule implements Serializable{
	/**
	 * 路由中包含的特殊标示符
	 * <p>Counter、Values</p>
	 * <p>为空，则证明没有特殊标示符</p>
	 */
	private String specialWord;
	/**
	 * 解析出来的路由，对应的属性的set方法名数组
	 * <p>根据上面两个属性，解析出路由中每个值对应的属性set方法。通过java反射机制，调用这些set方法</p>
	 * <p>要求：数组中属性set方法名的顺序，要和路由中的值顺序对应</p>
	 */
	private String[] propSetMethodNameArr;
	
	public RouteRule(String specialWord, String[] propSetMethodNameArr) {
		this.specialWord = specialWord;
		this.propSetMethodNameArr = propSetMethodNameArr;
	}
	public RouteRule() {
	}

	public String getSpecialWord() {
		return specialWord;
	}
	public void setSpecialWord(String specialWord) {
		this.specialWord = specialWord;
	}
	public String[] getPropSetMethodNameArr() {
		return propSetMethodNameArr;
	}
	public void setPropSetMethodNameArr(String[] propSetMethodNameArr) {
		this.propSetMethodNameArr = propSetMethodNameArr;
	}
	/**
	 * 路由规则的全局唯一标识
	 * <p>如：1_null、2_Counter等</p>
	 */
	public String getRouteRuleIdentity() {
		boolean includeSpecialWord = StrUtils.notEmpty(specialWord);
		return (includeSpecialWord?(propSetMethodNameArr.length+1):propSetMethodNameArr.length) + "_" + (includeSpecialWord?specialWord:"null");
	}
}
