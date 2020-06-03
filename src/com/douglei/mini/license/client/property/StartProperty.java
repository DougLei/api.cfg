package com.douglei.mini.license.client.property;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.douglei.mini.license.client.ValidationResult;

/**
 * 
 * @author DougLei
 */
public class StartProperty extends Property {
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	public StartProperty(String value) {
		super("start", value);
	}
	
	/**
	 * 验证有效期
	 * @return
	 */
	public ValidationResult verify() {
		if((getCurrentDate() - getStartDate().getTime()) < 0) {
			return new ValidationResult() {
				
				@Override
				public String getMessage() {
					return "授权文件未激活";
				}
				
				@Override
				public String getCode_() {
					return "file.not.active";
				}
			};
		}
		return null;
	}
	
	private Date startDate;
	private Date getStartDate() {
		if(startDate == null) {
			try {
				startDate = sdf.parse(value);
			} catch (ParseException e) {
				e.printStackTrace();
			}		
		}
		return startDate;
	}
	
	/**
	 * 获取当前日期
	 * @return
	 */
	private long getCurrentDate() {
		try {
			return sdf.parse(sdf.format(new Date())).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
