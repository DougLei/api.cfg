package com.douglei.tools.utils;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 主键工具类
 * @author DougLei
 */
public class IdentityUtil {
	
	/**
	 * 获取uuid(标准36长度)
	 * @return
	 */
	public static String getUUID() {
		return UUID.randomUUID().toString();
	}
	
	/**
	 * 获取uuid(32长度，没有-)
	 * @return
	 */
	public static String get32UUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	/**
	 * 获取随机数
	 * @param seed 种子
	 * @return
	 */
	public static int getRandom(int seed) {
		return ThreadLocalRandom.current().nextInt(seed);
	}
}
