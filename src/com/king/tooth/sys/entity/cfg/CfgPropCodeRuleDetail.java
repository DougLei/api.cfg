package com.king.tooth.sys.entity.cfg;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.annotation.Table;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.IEntityPropAnalysis;
import com.king.tooth.thread.current.CurrentThreadContext;
import com.king.tooth.util.ResourceHandlerUtil;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 属性编码规则明细表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class CfgPropCodeRuleDetail extends BasicEntity implements IEntity, IEntityPropAnalysis{
	
	/**
	 * 关联的属性编码规则id
	 */
	private String refPropCodeRuleId;
	/**
	 * 连接符
	 * <p>当前段连接下一段的连接符；如果没有下一段，则连接符无效</p>
	 * <p>默认值为空字符串</p>
	 */
	private String linkNextSymbol;
	/**
	 * 排序值
	 * <p>按照顺序从小到大组装编码结果</p>
	 */
	private Integer orderCode;
	/**
	 * 规则类型
	 * <p>0:default(默认固定值)、1:date(日期)、2:seq(序列)、3:serialNumber(流水号)、4:random(随机数)、5:column(其他列值)、6:data_dictionary(数据字典值)</p>
	 * <p>0</p>
	 */
	private Integer ruleType;
	
	// ------------------------
	/**
	 * 默认固定值
	 * <p>默认值为空字符串</p>
	 */
	private String defValue;
	
	// ------------------------
	/**
	 * 日期格式化格式
	 * <p>yyyy表示年，MM表示月，dd表示日，hh表示小时，mm表示分钟，ss表示秒，SSS表示毫秒，可任意组装</p>
	 * <p>默认值为yyyyMMddHHmmssSSS</p>
	 */
	private String dateFormate;
	
	// ------------------------
	/**
	 * 序列再次初始化的时机
	 * <p>0:none(不重新初始化)、1:hour(每小时)、2:day(每天)、3:month(每月)、4:year(每年)、5:week(每周)、6:quarter(每季度)，这个字段，ruleType为2和3，都使用</p>
	 * <p>默认值为0</p>
	 */
	private Integer seqReinitTime;
	/**
	 * 序列的起始值
	 * <p>默认值是1，这个字段，ruleType为2和3，都使用</p>
	 */
	private Integer seqStartVal;
	/**
	 * 序列的间隔值
	 * <p>序列值每次自增的大小，这个字段，ruleType为2和3，都使用</p>
	 * <p>默认值为1</p>
	 */
	private Integer seqSkipVal;
	
	/**
	 * 流水号长度
	 * <p>默认值为3</p>
	 */
	private Integer serialNumLength;
	/**
	 * 流水号是否自动补缺空位
	 * <p>默认值为1，即如果实际值不满足长度的，是否用0补全前面的空位</p>
	 */
	private Integer serialNumIsAutoFillnull;
	
	// ------------------------
	/**
	 * 随机数种子的值
	 * <p>默认为100000</p>
	 */
	private Integer randomSeedVal;
	
	// ------------------------
	/**
	 * 列值的来源
	 * <p>0:当前数据、1:当前数据资源对象、2:其他数据资源对象</p>
	 * <p>默认值为0</p>
	 */
	private Integer columnValFrom;
	
	/**
	 * 数据字典值的来源
	 * <p>0:当前数据、1:当前数据资源对象、2:其他数据资源对象</p>
	 * <p>默认值为0</p>
	 */
	private Integer dataDictionaryValFrom;
	/**
	 * 数据字典id
	 */
	private String dataDictionaryId;
	
	/**
	 * 引用表的id
	 * <p>这个字段，ruleType为5和6，都使用</p>
	 */
	private String refTableId;
	/**
	 * 引用列的id
	 * <p>这个字段，ruleType为5和6，都使用</p>
	 */
	private String refColumnId;
	/**
	 * 查询条件列的id
	 * <p>这个字段，ruleType为5和6，都使用</p>
	 */
	private String queryCondColumnId;
	/**
	 * 查询条件值的列id
	 * <p>即当前对象中的某一个属性值作为条件，这个字段，ruleType为5和6，都使用</p>
	 */
	private String queryCondValColumnId;
	
	// ------------------------
	/**
	 * 值截取的起始位置
	 * <p>默认值为0，该配置优先级高于正则表达式</p>
	 */
	private Integer valSubStartIndex;
	/**
	 * 值截取的结束位置
	 * <p>默认值为0，该配置优先级高于正则表达式</p>
	 */
	private Integer valSubEndIndex;
	/**
	 * 值截取的正则表达式
	 */
	private String valSubRegex;
	/**
	 * 值截取的正则表达式第n次匹配
	 * <p>默认值为1，取第一次匹配的值</p>
	 */
	private Integer valSubMatchNum;
	
	//-------------------------------------------------------------------------

	public String getRefPropCodeRuleId() {
		return refPropCodeRuleId;
	}
	public void setRefPropCodeRuleId(String refPropCodeRuleId) {
		this.refPropCodeRuleId = refPropCodeRuleId;
	}
	public String getLinkNextSymbol() {
		return linkNextSymbol;
	}
	public void setLinkNextSymbol(String linkNextSymbol) {
		this.linkNextSymbol = linkNextSymbol;
	}
	public Integer getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(Integer orderCode) {
		this.orderCode = orderCode;
	}
	public Integer getRuleType() {
		return ruleType;
	}
	public void setRuleType(Integer ruleType) {
		this.ruleType = ruleType;
	}
	public String getDefValue() {
		if(defValue == null){
			return "";
		}
		return defValue;
	}
	public void setDefValue(String defValue) {
		this.defValue = defValue;
	}
	public String getDateFormate() {
		return dateFormate;
	}
	public void setDateFormate(String dateFormate) {
		this.dateFormate = dateFormate;
	}
	public Integer getSeqReinitTime() {
		return seqReinitTime;
	}
	public void setSeqReinitTime(Integer seqReinitTime) {
		this.seqReinitTime = seqReinitTime;
	}
	public Integer getSeqStartVal() {
		return seqStartVal;
	}
	public void setSeqStartVal(Integer seqStartVal) {
		this.seqStartVal = seqStartVal;
	}
	public Integer getSeqSkipVal() {
		return seqSkipVal;
	}
	public void setSeqSkipVal(Integer seqSkipVal) {
		this.seqSkipVal = seqSkipVal;
	}
	public Integer getSerialNumLength() {
		return serialNumLength;
	}
	public void setSerialNumLength(Integer serialNumLength) {
		this.serialNumLength = serialNumLength;
	}
	public Integer getSerialNumIsAutoFillnull() {
		return serialNumIsAutoFillnull;
	}
	public void setSerialNumIsAutoFillnull(Integer serialNumIsAutoFillnull) {
		this.serialNumIsAutoFillnull = serialNumIsAutoFillnull;
	}
	public Integer getRandomSeedVal() {
		return randomSeedVal;
	}
	public void setRandomSeedVal(Integer randomSeedVal) {
		this.randomSeedVal = randomSeedVal;
	}
	public Integer getColumnValFrom() {
		return columnValFrom;
	}
	public void setColumnValFrom(Integer columnValFrom) {
		this.columnValFrom = columnValFrom;
	}
	public Integer getDataDictionaryValFrom() {
		return dataDictionaryValFrom;
	}
	public void setDataDictionaryValFrom(Integer dataDictionaryValFrom) {
		this.dataDictionaryValFrom = dataDictionaryValFrom;
	}
	public String getDataDictionaryId() {
		return dataDictionaryId;
	}
	public void setDataDictionaryId(String dataDictionaryId) {
		this.dataDictionaryId = dataDictionaryId;
	}
	public String getRefTableId() {
		return refTableId;
	}
	public void setRefTableId(String refTableId) {
		this.refTableId = refTableId;
	}
	public String getRefColumnId() {
		return refColumnId;
	}
	public void setRefColumnId(String refColumnId) {
		this.refColumnId = refColumnId;
	}
	public String getQueryCondColumnId() {
		return queryCondColumnId;
	}
	public void setQueryCondColumnId(String queryCondColumnId) {
		this.queryCondColumnId = queryCondColumnId;
	}
	public String getQueryCondValColumnId() {
		return queryCondValColumnId;
	}
	public void setQueryCondValColumnId(String queryCondValColumnId) {
		this.queryCondValColumnId = queryCondValColumnId;
	}
	public Integer getValSubStartIndex() {
		return valSubStartIndex;
	}
	public void setValSubStartIndex(Integer valSubStartIndex) {
		this.valSubStartIndex = valSubStartIndex;
	}
	public Integer getValSubEndIndex() {
		return valSubEndIndex;
	}
	public void setValSubEndIndex(Integer valSubEndIndex) {
		this.valSubEndIndex = valSubEndIndex;
	}
	public String getValSubRegex() {
		return valSubRegex;
	}
	public void setValSubRegex(String valSubRegex) {
		this.valSubRegex = valSubRegex;
	}
	public Integer getValSubMatchNum() {
		return valSubMatchNum;
	}
	public void setValSubMatchNum(Integer valSubMatchNum) {
		this.valSubMatchNum = valSubMatchNum;
	}
	
	
	@JSONField(serialize = false)
	public List<CfgColumn> getColumnList() {
		List<CfgColumn> columns = new ArrayList<CfgColumn>(22+7);
		
		CfgColumn refPropCodeRuleIdColumn = new CfgColumn("ref_prop_code_rule_id", DataTypeConstants.STRING, 32);
		refPropCodeRuleIdColumn.setName("关联的属性编码规则id");
		refPropCodeRuleIdColumn.setComments("关联的属性编码规则id");
		columns.add(refPropCodeRuleIdColumn);
		
		CfgColumn linkNextSymbolColumn = new CfgColumn("link_next_symbol", DataTypeConstants.STRING, 5);
		linkNextSymbolColumn.setName("连接符");
		linkNextSymbolColumn.setComments("当前段连接下一段的连接符；如果没有下一段，则连接符无效；默认值为空字符串");
		linkNextSymbolColumn.setDefaultValue("");
		columns.add(linkNextSymbolColumn);
		
		CfgColumn orderCodeColumn = new CfgColumn("order_code", DataTypeConstants.INTEGER, 4);
		orderCodeColumn.setName("排序值");
		orderCodeColumn.setComments("按照顺序从小到大组装编码结果");
		columns.add(orderCodeColumn);
		
		CfgColumn ruleTypeColumn = new CfgColumn("rule_type", DataTypeConstants.INTEGER, 1);
		ruleTypeColumn.setName("规则类型");
		ruleTypeColumn.setComments("0:default(默认固定值)、1:date(日期)、2:seq(序列)、3:serialNumber(流水号)、4:random(随机数)、5:column(其他列值)、6:data_dictionary(数据字典值)，默认值为0");
		ruleTypeColumn.setDefaultValue("0");
		columns.add(ruleTypeColumn);
		
		CfgColumn defValueColumn = new CfgColumn("defValue", DataTypeConstants.STRING, 10);
		defValueColumn.setName("默认固定值");
		defValueColumn.setComments("默认值为空字符串");
		defValueColumn.setDefaultValue("");
		columns.add(defValueColumn);
		
		CfgColumn dateFormateColumn = new CfgColumn("date_formate", DataTypeConstants.STRING, 20);
		dateFormateColumn.setName("日期格式化格式");
		dateFormateColumn.setComments("yyyy表示年，MM表示月，dd表示日，hh表示小时，mm表示分钟，ss表示秒，SSS表示毫秒，可任意组装，默认值为yyyyMMddHHmmssSSS");
		dateFormateColumn.setDefaultValue("yyyyMMddHHmmssSSS");
		columns.add(dateFormateColumn);
		
		CfgColumn seqReinitTimeColumn = new CfgColumn("seq_reinit_time", DataTypeConstants.INTEGER, 1);
		seqReinitTimeColumn.setName("序列再次初始化的时机");
		seqReinitTimeColumn.setComments("0:none(不重新初始化)、1:hour(每小时)、2:day(每天)、3:month(每月)、4:year(每年)、5:week(每周)、6:quarter(每季度)，默认值为0，这个字段，ruleType为2和3，都使用");
		seqReinitTimeColumn.setDefaultValue("0");
		columns.add(seqReinitTimeColumn);
		
		CfgColumn seqStartValColumn = new CfgColumn("seq_start_val", DataTypeConstants.INTEGER, 8);
		seqStartValColumn.setName("序列的起始值");
		seqStartValColumn.setComments("序列的起始值，默认值是1，这个字段，ruleType为2和3，都使用");
		seqStartValColumn.setDefaultValue("1");
		columns.add(seqStartValColumn);
		
		CfgColumn seqSkipValColumn = new CfgColumn("seq_skip_val", DataTypeConstants.INTEGER, 4);
		seqSkipValColumn.setName("序列的间隔值");
		seqSkipValColumn.setComments("序列值每次自增的大小，默认值为1，这个字段，ruleType为2和3，都使用");
		seqSkipValColumn.setDefaultValue("1");
		columns.add(seqSkipValColumn);
		
		CfgColumn serialNumLengthColumn = new CfgColumn("serial_num_length", DataTypeConstants.INTEGER, 2);
		serialNumLengthColumn.setName("流水号长度");
		serialNumLengthColumn.setComments("默认值为3");
		serialNumLengthColumn.setDefaultValue("3");
		columns.add(serialNumLengthColumn);
		
		CfgColumn serialNumIsAutoFillnullColumn = new CfgColumn("serial_num_is_auto_fillnull", DataTypeConstants.INTEGER, 1);
		serialNumIsAutoFillnullColumn.setName("流水号是否自动补缺空位");
		serialNumIsAutoFillnullColumn.setComments("默认值为1，即如果实际值不满足长度的，是否用0补全前面的空位");
		serialNumIsAutoFillnullColumn.setDefaultValue("1");
		columns.add(serialNumIsAutoFillnullColumn);
		
		CfgColumn randomSeedValColumn = new CfgColumn("random_seed_val", DataTypeConstants.INTEGER, 10);
		randomSeedValColumn.setName("随机数种子的值");
		randomSeedValColumn.setComments("默认为100000");
		randomSeedValColumn.setDefaultValue("100000");
		columns.add(randomSeedValColumn);
		
		CfgColumn columnValFromColumn = new CfgColumn("column_val_from", DataTypeConstants.INTEGER, 1);
		columnValFromColumn.setName("列值的来源");
		columnValFromColumn.setComments("0:当前数据、1:当前数据资源对象、2:其他数据资源对象，默认值为0");
		columnValFromColumn.setDefaultValue("0");
		columns.add(columnValFromColumn);
		
		CfgColumn dataDictionaryValFromColumn = new CfgColumn("data_dictionary_val_from", DataTypeConstants.INTEGER, 1);
		dataDictionaryValFromColumn.setName("数据字典值的来源");
		dataDictionaryValFromColumn.setComments("0:当前数据、1:当前数据资源对象、2:其他数据资源对象，默认值为0");
		dataDictionaryValFromColumn.setDefaultValue("0");
		columns.add(dataDictionaryValFromColumn);
		
		CfgColumn dataDictionaryIdColumn = new CfgColumn("data_dictionary_id", DataTypeConstants.STRING, 50);
		dataDictionaryIdColumn.setName("数据字典id");
		dataDictionaryIdColumn.setComments("数据字典id");
		columns.add(dataDictionaryIdColumn);
		
		CfgColumn refTableIdColumn = new CfgColumn("ref_table_id", DataTypeConstants.STRING, 32);
		refTableIdColumn.setName("引用表的id");
		refTableIdColumn.setComments("这个字段，ruleType为5和6，都使用");
		columns.add(refTableIdColumn);
		
		CfgColumn refColumnIdColumn = new CfgColumn("ref_column_id", DataTypeConstants.STRING, 32);
		refColumnIdColumn.setName("引用列的id");
		refColumnIdColumn.setComments("这个字段，ruleType为5和6，都使用");
		columns.add(refColumnIdColumn);
		
		CfgColumn queryCondColumnIdColumn = new CfgColumn("query_cond_column_id", DataTypeConstants.STRING, 32);
		queryCondColumnIdColumn.setName("查询条件列的id");
		queryCondColumnIdColumn.setComments("这个字段，ruleType为5和6，都使用");
		columns.add(queryCondColumnIdColumn);
		
		CfgColumn queryCondValColumnIdColumn = new CfgColumn("query_cond_val_column_id", DataTypeConstants.STRING, 32);
		queryCondValColumnIdColumn.setName("查询条件值的列id");
		queryCondValColumnIdColumn.setComments("即当前对象中的某一个属性值作为条件，这个字段，ruleType为5和6，都使用");
		columns.add(queryCondValColumnIdColumn);
		
		CfgColumn valSubStartIndexColumn = new CfgColumn("val_sub_start_index", DataTypeConstants.INTEGER, 3);
		valSubStartIndexColumn.setName("值截取的起始位置");
		valSubStartIndexColumn.setComments("默认值为0，该配置优先级高于正则表达式");
		valSubStartIndexColumn.setDefaultValue("0");
		columns.add(valSubStartIndexColumn);
		
		CfgColumn valSubEndIndexColumn = new CfgColumn("val_sub_end_index", DataTypeConstants.INTEGER, 3);
		valSubEndIndexColumn.setName("值截取的结束位置");
		valSubEndIndexColumn.setComments("默认值为0，该配置优先级高于正则表达式");
		valSubEndIndexColumn.setDefaultValue("0");
		columns.add(valSubEndIndexColumn);
		
		CfgColumn valSubRegexColumn = new CfgColumn("val_sub_regex", DataTypeConstants.STRING, 300);
		valSubRegexColumn.setName("值截取的正则表达式");
		valSubRegexColumn.setComments("值截取的正则表达式");
		columns.add(valSubRegexColumn);
		
		CfgColumn valSubMatchNumColumn = new CfgColumn("val_sub_match_num", DataTypeConstants.INTEGER, 2);
		valSubMatchNumColumn.setName("值截取的正则表达式第n次匹配");
		valSubMatchNumColumn.setComments("默认值为1，取第一次匹配的值");
		valSubMatchNumColumn.setDefaultValue("1");
		columns.add(valSubMatchNumColumn);
		
		return columns;
	}
	
	public CfgTable toCreateTable() {
		CfgTable table = new CfgTable(toDropTable());
		table.setName("属性编码规则明细表");
		table.setComments("属性编码规则明细表");
		
		table.setColumns(getColumnList());
		return table;
	}

	public String toDropTable() {
		return "CFG_PROP_CODE_RULE_DETAIL";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "CfgPropCodeRuleDetail";
	}
	
	public String validNotNullProps() {
		return null;
	}
	
	public String analysisResourceProp() {
		String result = validNotNullProps();
		if(result == null){
		}
		return result;
	}
	
	// ------------------------------------------------------------------------------------------------------
	
	/**
	 * 得到当前段的编码值
	 * @param resourceName 当前对象资源名
	 * @param currentJsonObject 当前对象的json对象，获取当前段的值的时候，可能会用到当前对象的数据
	 * @return
	 */
	public String getCurrentStageCodeVal(String resourceName, JSONObject currentJsonObject) {
		Object value = null;
		switch(ruleType){
			case 1: // 1:date(日期)
				value = getDateVal(resourceName, currentJsonObject);
				break;
			case 2: // 2:seq(序列)
				value = getSeqVal(resourceName, currentJsonObject);
				break;
			case 3: // 3:serialNumber(流水号)
				value = getSerialNumberVal(resourceName, currentJsonObject);
				break;
			case 4: // 4:random(随机数)
				value = getRandomVal(resourceName, currentJsonObject);
				break;
			case 5: // 5:column(其他列值)
				value = getColumnVal(resourceName, currentJsonObject, columnValFrom);
				break;
			case 6: // 6:data_dictionary(数据字典值)
				value = getDataDictionaryVal(resourceName, currentJsonObject);
				break;
			default: // 默认值为0，0:default(默认固定值)
				value = getDefaultVal(resourceName, currentJsonObject);
				break;
		}
		
		if(value == null){
			return "";
		}
		String valueStr = value.toString();
		if(valSubEndIndex > 0 && valSubEndIndex <= valueStr.length()){
			if(valSubStartIndex < 1){
				valSubStartIndex = 1;
			}
			valueStr = valueStr.substring(valSubStartIndex-1, valSubEndIndex);
		}else if(StrUtils.notEmpty(valSubRegex)){
			int matchNum = 1;
			if(valSubMatchNum < 1){
				valSubMatchNum = matchNum;
			}
			Matcher matcher = Pattern.compile(valSubRegex, Pattern.MULTILINE).matcher(valueStr);
			
			while(matcher.find()){
				if(matchNum == valSubMatchNum){
					valueStr = matcher.group();
					break;
				}
				matchNum++;
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
		return new SimpleDateFormat(dateFormate).format(new Date());
	}
	
	// ------------------------------------------------------------------------------------------
	/**
	 * 获取【2:seq(序列)】
	 * @param resourceName
	 * @param currentJsonObject
	 * @return
	 */
	private synchronized Object getSeqVal(String resourceName, JSONObject currentJsonObject) {
		// TODO 这整个方法，都要放到线程锁中执行
		
		CfgSeqInfo seq = HibernateUtil.extendExecuteUniqueQueryByHqlArr(CfgSeqInfo.class, querySeqInfoHql, id);
		if(seq == null){
			seq = new CfgSeqInfo();
			seq.setRefPropCodeRuleDetailId(id);
			seq.setInitDate(new Date());
			seq.setCurrentVal(seqStartVal);
			HibernateUtil.saveObject(seq, null);
		}else{
			seq.setCurrentVal(seq.getCurrentVal() + seqSkipVal);
			setSeqIsReinit(seq);
			HibernateUtil.updateObject(seq, null);
		}
		return seq.getCurrentVal();
	}
	/** 查询序列信息hql语句 */
	private static final String querySeqInfoHql = "from CfgSeqInfo where columnCodeRuleDetailId=?";
	
	/**
	 * 设置序列重新初始化
	 * @param seq
	 */
	private void setSeqIsReinit(CfgSeqInfo seq){
		boolean isReInit = false; // 标识是否需要重置
		
		// TODO 
		Date currentDate = new Date();
		switch(seqReinitTime){
			case 1: // 1:hour(每小时)
				break;
			case 2: // 2:day(每天)
				break;
			case 3: // 3:month(每月)
				break;
			case 4: // 4:year(每年)
				break;
			case 5: // 5:week(每周)
				break;
			case 6: // 6:quarter(每季度)
				break;
			default: // 默认，0:none(不重新初始化)
				break;
		}
		
		if(isReInit){
			seq.setInitDate(currentDate);
			seq.setCurrentVal(seqStartVal);
		}
	}
	
	// ------------------------------------------------------------------------------------------
	/**
	 * 获取【3:serialNumber(流水号)】
	 * @param resourceName
	 * @param currentJsonObject
	 * @return
	 */
	private Object getSerialNumberVal(String resourceName, JSONObject currentJsonObject) {
		Object value = getSeqVal(resourceName, currentJsonObject);
		if(value == null){
			throw new NullPointerException("获取序列值结果为空，请联系后端系统开发人员");
		}
		
		String valueStr = value.toString();
		int valueLength = valueStr.length();
		if(serialNumIsAutoFillnull == 1 && valueLength < serialNumLength){
			valueStr = zero.substring(0, serialNumLength-valueLength) + valueStr;
		}
		return valueStr;
	}
	private static final String zero = "00000000000000000000";
	
	// ------------------------------------------------------------------------------------------
	/**
	 * 获取【4:random(随机数)】
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
		Object tableResourceName = HibernateUtil.extendExecuteUniqueQueryByHqlArr(CfgColumn.class, queryTableResourceNameByIdHql, tableId);
		if(tableResourceName == null){
			throw new NullPointerException("在查询字段编码时，没有查询到id为["+tableId+"]的CfgTable资源名信息");
		}
		return tableResourceName.toString();
	}
	/** 根据id查询table资源名的hql */
	private static final String queryTableResourceNameByIdHql = "select resourceName from CfgTable where " + ResourcePropNameConstants.ID + "=? and isEnabled=1 and isCreated=1 and isBuildModel=1";
	
	
	/**
	 * 根据column id，获取对应的column属性名
	 * @param columnId
	 * @return
	 */
	private String getColumnPropNameById(String columnId){
		Object columnPropName = null;
		if(ResourcePropNameConstants.ID.equals(columnId)){
			columnPropName = columnId;
		}else{
			columnPropName = HibernateUtil.executeUniqueQueryByHqlArr(queryColumnPropNameByIdHql, columnId);
		}
		if(columnPropName == null){
			throw new NullPointerException("在查询字段编码时，没有查询到id为["+columnId+"]的CfgColumn属性名信息");
		}
		return columnPropName.toString();
	}
	/** 根据id查询column属性名的hql */
	private static final String queryColumnPropNameByIdHql = "select propName from CfgColumn where " + ResourcePropNameConstants.ID + "=? and isEnabled=1 and operStatus="+CfgColumn.CREATED;
	
	/**
	 * 获取【5:column(其他列值)】
	 * @param resourceName
	 * @param currentJsonObject
	 * @param valueFrom 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Object getColumnVal(String resourceName, JSONObject currentJsonObject, int valueFrom) {
		Object value = null;
		
		String refColumnPropName = getColumnPropNameById(refColumnId);
		if(valueFrom == 0){ // 0:当前数据
			value = currentJsonObject.getString(refColumnPropName);
		}else{
			Object queryCondValue = currentJsonObject.get(getColumnPropNameById(queryCondValColumnId));
			if(valueFrom == 2){// 2:其他数据资源对象
				resourceName = getTableResourceNameById(refTableId);
			}
			List<Object> list = HibernateUtil.executeListQueryByHqlArr(null, null, "select "+refColumnPropName+" from "+resourceName+" where "+getColumnPropNameById(queryCondColumnId)+"=?", queryCondValue==null?"":queryCondValue);
			if(list != null && list.size() > 0){
				value = list.get(0);
				list.clear();
			}
		}
		return value;
	}
	
	
	
	// ------------------------------------------------------------------------------------------
	/**
	 * 获取【6:data_dictionary(数据字典值)】
	 * @param resourceName
	 * @param currentJsonObject
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Object getDataDictionaryVal(String resourceName, JSONObject currentJsonObject) {
		Object value = null;
		List<Object[]> dataDictionarys = HibernateUtil.executeListQueryByHqlArr(null, null, queryDataDictionaryHql, CurrentThreadContext.getProjectId(), CurrentThreadContext.getCustomerId(), null);
		if(dataDictionarys != null && dataDictionarys.size() > 0){
			value = getColumnVal(resourceName, currentJsonObject, dataDictionaryValFrom);
			if(value != null){
				for (Object[] objects : dataDictionarys) {
					if(objects[0] != null && objects[0].equals(value)){
						value = objects[1];
						break;
					}
				}
			}
			dataDictionarys.clear();
		}
		return value;
	}
	/** 查询数据字典值的hql语句 */
	private static final String queryDataDictionaryHql = "select val, caption from SysDataDictionary where isEnabled=1 and isDelete=0 and projectId=? and customerId=? and parentId=?";
}
