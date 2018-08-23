package com.king.tooth.util;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.sys.entity.cfg.CfgColumnCodeRule;

/**
 * 字段编码规则的工具类
 * @author DougLei
 */
public class ColumnCodeRuleUtil {
	
	/**
	 * 给对象的属性设置最终的编码值
	 * @param data
	 * @param index
	 * @param rules
	 */
	public static void setFinalCodeVal(JSONObject data, int index, List<CfgColumnCodeRule> rules) {
		if(rules != null && rules.size() > 0){
			for (CfgColumnCodeRule rule : rules) {
				data.put(rule.getRefPropName(), rule.getFinalCodeVal(index));
			}
		}
	}
}
