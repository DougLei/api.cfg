package com.api.util.prop.code.rule;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;

/**
 * 属性编码规则锁映射
 * @author DougLei
 */
public class PropCodeRuleLockMapping {
	/**
	 * key：为编码规则的id
	 * value：为对应的锁对象
	 */
	transient static final Map<String, Lock> propCodeRuleLockMapping = new HashMap<String, Lock>(60);
}
