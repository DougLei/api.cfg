package com.king.tooth.sys.entity.cfg.prop.code.rule;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.sys.entity.cfg.CfgPropCodeRuleDetail;
import com.king.tooth.sys.entity.cfg.CfgSeqInfo;
import com.king.tooth.thread.current.CurrentThreadContext;
import com.king.tooth.util.DateUtil;
import com.king.tooth.util.ResourceHandlerUtil;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.datatype.DataTypeTurnUtil;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 当前段的编码值处理者
 * @author DougLei
 */
@SuppressWarnings("serial")
public class CurrentStageCodeProcesser implements Serializable{
	
	private String id;
	private int ruleType;
	private String defValue;
	private String dateFormate;
	private Integer seqReinitTime;
	private Integer seqStartVal;
	private Integer seqSkipVal;
	private String recSeqTableId;
	private String recSeqCodeColumnId;
	private String recSeqParentColumnId;
	private String recSeqLinkSymbol;
	private int serialNumLength;
	private int serialNumIsAutoFillnull;
	private int randomSeedVal;
	private int columnValFrom;
	private int codeDataDictionaryValFrom;
	private String codeDataDictionaryId;
	private String refTableId;
	private String refColumnId;
	private String queryCondColumnId;
	private String queryCondValPropId;
	private int queryCondValPropFrom;
	private String orderByColumnId;
	private String orderByMethod;
	private int valSubStartIndex;
	private int valSubEndIndex;
	private String valSubRegex;
	private int valSubMatchNum;
	
	public CurrentStageCodeProcesser(CfgPropCodeRuleDetail propCodeRuleDetail) {
		id = propCodeRuleDetail.getId();                        
		ruleType = propCodeRuleDetail.getRuleType();
		defValue = propCodeRuleDetail.getDefValue();
		dateFormate = propCodeRuleDetail.getDateFormate();
		seqReinitTime = propCodeRuleDetail.getSeqReinitTime();
		seqStartVal = propCodeRuleDetail.getSeqStartVal();
		seqSkipVal = propCodeRuleDetail.getSeqSkipVal();
		recSeqTableId = propCodeRuleDetail.getRecSeqTableId();
		recSeqCodeColumnId = propCodeRuleDetail.getRecSeqCodeColumnId();
		recSeqParentColumnId = propCodeRuleDetail.getRecSeqParentColumnId();
		recSeqLinkSymbol = propCodeRuleDetail.getRecSeqLinkSymbol();
		serialNumLength = propCodeRuleDetail.getSerialNumLength();
		serialNumIsAutoFillnull = propCodeRuleDetail.getSerialNumIsAutoFillnull();
		randomSeedVal = propCodeRuleDetail.getRandomSeedVal();
		columnValFrom = propCodeRuleDetail.getColumnValFrom();
		codeDataDictionaryValFrom = propCodeRuleDetail.getCodeDataDictionaryValFrom();
		codeDataDictionaryId = propCodeRuleDetail.getCodeDataDictionaryId();
		refTableId = propCodeRuleDetail.getRefTableId();
		refColumnId = propCodeRuleDetail.getRefColumnId();
		queryCondColumnId = propCodeRuleDetail.getQueryCondColumnId();
		queryCondValPropId = propCodeRuleDetail.getQueryCondValPropId();
		queryCondValPropFrom = propCodeRuleDetail.getQueryCondValPropFrom();
		orderByColumnId = propCodeRuleDetail.getOrderByColumnId();
		orderByMethod = propCodeRuleDetail.getOrderByMethod();
		valSubStartIndex = propCodeRuleDetail.getValSubStartIndex();
		valSubEndIndex = propCodeRuleDetail.getValSubEndIndex();
		valSubRegex = propCodeRuleDetail.getValSubRegex();
		valSubMatchNum = propCodeRuleDetail.getValSubMatchNum();
	}
	
	/**
	 * 获取编码值
	 * @param resourceName 当前对象资源名
	 * @param currentJsonObject 当前对象的json对象，获取当前段的值的时候，可能会用到当前对象的数据
	 * @return
	 */
	public String getCodeVal(String resourceName, JSONObject currentJsonObject) {
		Object value = null;
		switch(ruleType){
			case 1: // 1:date(日期)
				value = getDateVal(resourceName, currentJsonObject);
				break;
			case 2: // 2:seq(序列)
				value = getSeqVal(resourceName, currentJsonObject, null);
				break;
			case 3: // 3:recursive_seq(递归序列)
				value = getRecursiveSeqVal(resourceName, currentJsonObject);
				break;
			case 4: // 4:serialNumber(流水号)
				value = getSerialNumberVal(resourceName, currentJsonObject);
				break;
			case 5: // 5:random(随机数)
				value = getRandomVal(resourceName, currentJsonObject);
				break;
			case 6: // 6:column(其他列值)
				value = getColumnVal(resourceName, currentJsonObject, columnValFrom);
				break;
			case 7: // 7:code_data_dictionary(编码数据字典值)
				value = getCodeDataDictionaryVal(resourceName, currentJsonObject);
				break;
			case 8: // 8:weekCalendar(周日历，即当前日期是今年第几周)
				value = getWeekCalendarVal(resourceName, currentJsonObject);
				break;
			case 9: // 9:seasonCalendar(季度日历，即当前日期是今年第几季度)
				value = getSeasonCalendarVal(resourceName, currentJsonObject);
				break;
			default: // 默认值为0，0:default(默认固定值)
				value = getDefaultVal(resourceName, currentJsonObject);
				break;
		}
		
		if(value == null){
			return "";
		}
		String valueStr = value.toString();
		if(ruleType == 6 && valueStr.length() > 0){
			if(valSubEndIndex > 0 && valSubEndIndex <= valueStr.length()){
				valueStr = valueStr.substring(valSubStartIndex-1, valSubEndIndex);
			}else if(StrUtils.notEmpty(valSubRegex)){
				int matchNum = 1;
				Matcher matcher = Pattern.compile(valSubRegex, Pattern.MULTILINE).matcher(valueStr);
				
				while(matcher.find()){
					if(matchNum == valSubMatchNum){
						valueStr = matcher.group();
						break;
					}
					matchNum++;
				}
			}
		}
		return valueStr;
	}
	
	// ------------------------------------------------------------------------------------------
	/**
	 * 获取【0:default(默认固定值)】
	 * @param resourceName
	 * @param currentJsonObject
	 * @return
	 */
	private Object getDefaultVal(String resourceName, JSONObject currentJsonObject) {
		return defValue;
	}
	
	// ------------------------------------------------------------------------------------------
	/**
	 * 获取【1:date(日期)】
	 * @param resourceName
	 * @param currentJsonObject
	 * @return
	 */
	private synchronized Object getDateVal(String resourceName, JSONObject currentJsonObject) {
		if(dateValFormat == null){
			dateValFormat = new SimpleDateFormat(dateFormate);
		}
		return dateValFormat.format(new Date());
	}
	private SimpleDateFormat dateValFormat;
	
	// ------------------------------------------------------------------------------------------
	private CfgSeqInfo seq;// 当前序列信息
	/**
	 * 获取【2:seq(序列)】
	 * @param resourceName
	 * @param currentJsonObject
	 * @param parentSeqValue 父序列值，实现递归序列，该字段存值例如：1.1，那么该序列的值就为1.1.1、1.1.2等
	 * @return
	 */
	private Object getSeqVal(String resourceName, JSONObject currentJsonObject, String parentSeqValue) {
		if(seq == null){
			if(StrUtils.isEmpty(parentSeqValue)){
				seq = HibernateUtil.extendExecuteUniqueQueryByHqlArr(CfgSeqInfo.class, querySeqInfoHql, id);
			}else{
				seq = HibernateUtil.extendExecuteUniqueQueryByHqlArr(CfgSeqInfo.class, querySeqInfoByParentSeqValHql, id, parentSeqValue);
			}
		}
		if(seq == null){
			seq = new CfgSeqInfo();
			seq.setRefPropCodeRuleDetailId(id);
			seq.setInitDate(new Date());
			seq.setCurrentVal(seqStartVal);
			if(StrUtils.notEmpty(parentSeqValue)){
				seq.setParentSeqVal(parentSeqValue);
			}
			HibernateUtil.saveObject(seq, null);
		}else{
			if(StrUtils.notEmpty(parentSeqValue)){
				seq.setParentSeqVal(parentSeqValue);
			}
			seq.setCurrentVal(seq.getCurrentVal() + seqSkipVal);
			setSeqIsReinit(seq);
			HibernateUtil.updateEntityObject(seq, null);
		}
		return seq.getCurrentVal(parentSeqValue, recSeqLinkSymbol);
	}
	/** 查询序列信息hql语句 */
	private static final String querySeqInfoHql = "from CfgSeqInfo where refPropCodeRuleDetailId=? and (parentSeqVal is null or parentSeqVal = '')";
	/** 查询序列信息hql语句 */
	private static final String querySeqInfoByParentSeqValHql = "from CfgSeqInfo where refPropCodeRuleDetailId=? and parentSeqVal=?";
	
	/**
	 * 设置序列重新初始化
	 * @param seq
	 */
	private void setSeqIsReinit(CfgSeqInfo seq){
		boolean isReInit = false; // 标识是否需要重置
		Date currentDate = new Date();
		switch(seqReinitTime){
			case 1: // 1:hour(每小时)
				if(!DateUtil.formatDate(currentDate, yyyyMMddHHSdf).equals(DateUtil.formatDate(seq.getInitDate(), yyyyMMddHHSdf))){
					isReInit = true;
				}
				break;
			case 2: // 2:day(每天)
				if(!DateUtil.formatDate(currentDate, yyyyMMddSdf).equals(DateUtil.formatDate(seq.getInitDate(), yyyyMMddSdf))){
					isReInit = true;
				}
				break;
			case 3: // 3:month(每月)
				if(!DateUtil.formatDate(currentDate, yyyyMMSdf).equals(DateUtil.formatDate(seq.getInitDate(), yyyyMMSdf))){
					isReInit = true;
				}
				break;
			case 4: // 4:year(每年)
				if(!DateUtil.formatDate(currentDate, yyyySdf).equals(DateUtil.formatDate(seq.getInitDate(), yyyySdf))){
					isReInit = true;
				}
				break;
			case 5: // 5:week(每周)
				if(DateUtil.weekCanlendarOfYear(currentDate) == DateUtil.weekCanlendarOfYear(seq.getInitDate())){
					if(!DateUtil.formatDate(currentDate, yyyySdf).equals(DateUtil.formatDate(seq.getInitDate(), yyyySdf))){
						isReInit = true;
					}
				}else{
					isReInit = true;
				}
				break;
			case 6: // 6:quarter(每季度)
				if(DateUtil.getSeason(currentDate, 1).equals(DateUtil.getSeason(seq.getInitDate(), 1))){
					if(!DateUtil.formatDate(currentDate, yyyySdf).equals(DateUtil.formatDate(seq.getInitDate(), yyyySdf))){
						isReInit = true;
					}
				}else{
					isReInit = true;
				}
				break;
			default: // 默认，0:none(不重新初始化)
				break;
		}
		
		if(isReInit){
			seq.setInitDate(currentDate);
			seq.setCurrentVal(seqStartVal);
		}
	}
	private static final SimpleDateFormat yyyyMMddHHSdf = new SimpleDateFormat("yyyyMMddHH");
	private static final SimpleDateFormat yyyyMMddSdf = new SimpleDateFormat("yyyyMMdd");
	private static final SimpleDateFormat yyyyMMSdf = new SimpleDateFormat("yyyyMM");
	private static final SimpleDateFormat yyyySdf = new SimpleDateFormat("yyyy");
	
	// ------------------------------------------------------------------------------------------
	/**
	 * 获取【3:recursive_seq(递归序列)】
	 * @param resourceName
	 * @param currentJsonObject
	 * @return
	 */
	private Object getRecursiveSeqVal(String resourceName, JSONObject currentJsonObject) {
		if(recSeqParentPropName == null){
			recSeqParentPropName = getPropInfoById(recSeqParentColumnId, false)[0];
		}
		String parentSeqValue = null;
		Object parentIdValue = currentJsonObject.get(recSeqParentPropName);
		if(StrUtils.notEmpty(parentIdValue)){// 不为空，表示是子数据，则要查询出上级数据的编号值
			if(recSeqParentCodeValQueryHql == null){
				recSeqParentCodeValQueryHql = "select " + getPropInfoById(recSeqCodeColumnId, false)[0] + " from " + getTableResourceNameById(recSeqTableId) + " where " + ResourcePropNameConstants.ID + "=?";
			}
			
			Object tmpParentSeqValue = HibernateUtil.executeUniqueQueryByHqlArr(recSeqParentCodeValQueryHql, parentIdValue);
			if(tmpParentSeqValue == null){
				throw new NullPointerException("递归序列查询上级数据的编码值时，查询结果为null，请联系后端系统开发人员");
			}
			parentSeqValue = tmpParentSeqValue.toString();
		}
		return getSeqVal(resourceName, currentJsonObject, parentSeqValue);
	}
	private String recSeqParentPropName;// 递归序列引用的父列属性名
	private String recSeqParentCodeValQueryHql;// 递归序列的父编码值查询hql
	
	// ------------------------------------------------------------------------------------------
	/**
	 * 获取【4:serialNumber(流水号)】
	 * @param resourceName
	 * @param currentJsonObject
	 * @return
	 */
	private Object getSerialNumberVal(String resourceName, JSONObject currentJsonObject) {
		Object value = getSeqVal(resourceName, currentJsonObject, null);
		if(value == null){
			throw new NullPointerException("获取序列值结果为空，请联系后端系统开发人员");
		}
		
		String valueStr = value.toString();
		int valueLength = valueStr.length();
		if(serialNumIsAutoFillnull == 1 && valueLength < serialNumLength){
			valueStr = zeros.substring(0, serialNumLength-valueLength) + valueStr;
		}
		return valueStr;
	}
	private static final String zeros = "00000000000000000000";
	
	// ------------------------------------------------------------------------------------------
	/**
	 * 获取【5:random(随机数)】
	 * @param resourceName
	 * @param currentJsonObject
	 * @return
	 */
	private Object getRandomVal(String resourceName, JSONObject currentJsonObject) {
		return ResourceHandlerUtil.getRandom(randomSeedVal)+"";
	}
	
	// ------------------------------------------------------------------------------------------
	/**
	 * 根据table id，获取对应的table资源名
	 * @param tableId
	 * @return
	 */
	private String getTableResourceNameById(String tableId){
		Object tableResourceName = HibernateUtil.executeUniqueQueryByHqlArr(queryTableResourceNameByIdHql, tableId);
		if(tableResourceName == null){
			throw new NullPointerException("在查询字段编码时，没有查询到id为["+tableId+"]的CfgTable资源名信息");
		}
		return tableResourceName.toString();
	}
	/** 根据id查询table资源名的hql */
	private static final String queryTableResourceNameByIdHql = "select resourceName from CfgTable where " + ResourcePropNameConstants.ID + "=? and isEnabled=1 and isCreated=1 and isBuildModel=1";
	
	
	/**
	 * 根据属性id，获取对应的column/sqlParam属性名和数据类型
	 * @param propId
	 * @param isQueryCondValProp 是否是查询条件值的属性id，如果是，要根据queryCondValPropFrom的值，决定去1:CfgColumn或2:CfgSqlParameter查询对应的数据；不是，就只去CfgColumn查询对应的数据
	 * @return
	 */
	private String[] getPropInfoById(String propId, boolean isQueryCondValPropId){
		String[] propInfo = new String[2];
		Object[] tmpPropInfo = null;
		if(ResourcePropNameConstants.ID.equals(propId)){// 其他基础字段可能不会作为查询条件，所以这里没有处理
			propInfo[0] = ResourcePropNameConstants.ID;
			propInfo[1] = DataTypeConstants.STRING;
		}else{
			if(isQueryCondValPropId){
				if(queryCondValPropFrom == 1){
					tmpPropInfo = (Object[]) HibernateUtil.executeUniqueQueryByHqlArr(queryColumnInfoIdHql, propId);
				}else{
					tmpPropInfo = (Object[]) HibernateUtil.executeUniqueQueryByHqlArr(querySqlParamInfoIdHql, propId);
				}
			}else{
				tmpPropInfo = (Object[]) HibernateUtil.executeUniqueQueryByHqlArr(queryColumnInfoIdHql, propId);
			}
			if(tmpPropInfo == null || tmpPropInfo[0] == null || tmpPropInfo[1] == null){
				throw new NullPointerException("在生成字段编码时，没有查询到id为["+propId+"]的属性信息");
			}
			
			propInfo[0] = tmpPropInfo[0].toString();
			propInfo[1] = tmpPropInfo[1].toString();
		}
		return propInfo;
	}
	/** 根据id查询column属性的hql */
	private static final String queryColumnInfoIdHql = "select propName,columnType from CfgColumn where " + ResourcePropNameConstants.ID + "=?";
	/** 根据id查询SqlParameter属性的hql */
	private static final String querySqlParamInfoIdHql = "select name,dataType from CfgSqlParameter where " + ResourcePropNameConstants.ID + "=?";
	
	/**
	 * 组装order by 语句
	 * @return
	 */
	private String installOrderBy() {
		if(StrUtils.notEmpty(orderByColumnId)){
			String[] orderByColumnInfo = getPropInfoById(orderByColumnId, false);
			return " order by " + orderByColumnInfo[0] + " " + orderByMethod;
		}
		return "";
	}
	
	/**
	 * 获取【6:column(其他列值)】
	 * @param resourceName
	 * @param currentJsonObject
	 * @param valueFrom 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Object getColumnVal(String resourceName, JSONObject currentJsonObject, int valueFrom) {
		if(queryCondValPropName==null){
			queryCondValPropName = getPropInfoById(queryCondValPropId, true)[0];
		}
		
		Object value = currentJsonObject.get(queryCondValPropName);// 0:默认从当前数据中获取值
		if(valueFrom > 0){
			if(queryCondColumnInfo == null){
				queryCondColumnInfo = getPropInfoById(queryCondColumnId, false);
			}
			if(queryColumnValueHql == null){
				if(valueFrom == 2){// 2:其他数据资源对象
					resourceName = getTableResourceNameById(refTableId);
				}
				queryColumnValueHql = "select "+getPropInfoById(refColumnId, false)[0]+" from "+resourceName+" where "+queryCondColumnInfo[0]+"=?" + installOrderBy();
			}
			
			List<Object> list = HibernateUtil.executeListQueryByHqlArr("1", "1", queryColumnValueHql, DataTypeTurnUtil.turnValueDataType(value, queryCondColumnInfo[1], true, true, true));
			if(list != null && list.size() > 0){
				value = list.get(0);
				list.clear();
			}else{
				value = "";
			}
		}
		return value;
	}
	private String queryCondValPropName;
	private String[] queryCondColumnInfo;
	private String queryColumnValueHql;
	
	// ------------------------------------------------------------------------------------------
	/**
	 * 获取【7:code_data_dictionary(编码数据字典值)】
	 * @param resourceName
	 * @param currentJsonObject
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Object getCodeDataDictionaryVal(String resourceName, JSONObject currentJsonObject) {
		Object value = null;
		codeDataDictionarys = HibernateUtil.executeListQueryByHqlArr(null, null, queryCodeDataDictionaryHql, CurrentThreadContext.getProjectId(), CurrentThreadContext.getCustomerId(), codeDataDictionaryId);
		if(codeDataDictionarys != null && codeDataDictionarys.size() > 0){
			value = getColumnVal(resourceName, currentJsonObject, codeDataDictionaryValFrom);
			if(StrUtils.notEmpty(value)){
				String valueStr = value.toString();
				for (Object[] objects : codeDataDictionarys) {
					if(objects[0] != null && objects[0].toString().equals(valueStr)){
						value = objects[1];
						break;
					}
				}
			}
		}
		return value;
	}
	/** 查询编码数据字典值的hql语句 */
	private static final String queryCodeDataDictionaryHql = "select propValue, codeValue from CfgCodeDataDictionary where isEnabled=1 and projectId=? and customerId=? and parentId=?";
	private List<Object[]> codeDataDictionarys;
	
	// ------------------------------------------------------------------------------------------
	/**
	 * 获取【8:weekCalendar(周日历，即当前日期是今年第几周)】
	 * @param resourceName
	 * @param currentJsonObject
	 * @return
	 */
	private Object getWeekCalendarVal(String resourceName, JSONObject currentJsonObject) {
		return DateUtil.weekCanlendarOfYear(new Date());
	}
	
	// ------------------------------------------------------------------------------------------
	/**
	 * 获取【9:seasonCalendar(季度日历，即当前日期是今年第几季度)】
	 * @param resourceName
	 * @param currentJsonObject
	 * @return
	 */
	private Object getSeasonCalendarVal(String resourceName, JSONObject currentJsonObject) {
		return DateUtil.getSeason(new Date(), 1);
	}
}
