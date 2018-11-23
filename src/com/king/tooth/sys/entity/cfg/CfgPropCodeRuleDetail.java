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
import com.king.tooth.util.DateUtil;
import com.king.tooth.util.ResourceHandlerUtil;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.datatype.DataTypeTurnUtil;
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
	 * <p>0:default(默认固定值)、1:date(日期)、2:seq(序列)、3:recursive_seq(递归序列)、4:serialNumber(流水号)、5:random(随机数)、6:column(其他列值)、7:code_data_dictionary(编码数据字典值)、8:weekCalendar(周日历，即当前日期是今年第几周)、9:seasonCalendar(季度日历，即当前日期是今年第几季度)</p>
	 * <p>默认值为0</p>
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
	 * <p>0:none(不重新初始化)、1:hour(每小时)、2:day(每天)、3:month(每月)、4:year(每年)、5:week(每周)、6:quarter(每季度)，默认值为0，这个字段，ruleType为2、3、4，都使用</p>
	 * <p>默认值为0</p>
	 */
	private Integer seqReinitTime;
	/**
	 * 序列的起始值
	 * <p>默认值是1，这个字段，ruleType为2、3、4，都使用</p>
	 */
	private Integer seqStartVal;
	/**
	 * 序列的间隔值
	 * <p>序列值每次自增的大小，这个字段，ruleType为2、3、4，都使用</p>
	 * <p>默认值为1</p>
	 */
	private Integer seqSkipVal;
	
	/**
	 * 递归序列引用的表id
	 * <p>如果是给表资源配，该值默认是当前配置字段所属的表id；如果是给sql参数配置，该值是用户选择的一张表id</p>
	 */
	private String recSeqTableId;
	/**
	 * 如果是给表资源配，该值默认是当前配置字段所属的表id；如果是给sql参数配置，该值是用户选择的一张表id
	 * <p>如果是给表资源配，该值默认是当前配置的字段id；如果是给sql参数配置，则是rec_seq_table_id字段存储的表中的某个字段id</p>
	 */
	private String recSeqCodeColumnId;
	/**
	 * 递归序列引用的父列id
	 * <p>就是表中，实现递归的字段，例如一般都是parent_id</p>
	 */
	private String recSeqParentColumnId;
	/**
	 * 递归序列间的连接符
	 * <p>默认值为.</p>
	 */
	private String recSeqLinkSymbol;
	
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
	 * 编码数据字典值的来源
	 * <p>0:当前数据、1:当前数据资源对象、2:其他数据资源对象</p>
	 * <p>默认值为0</p>
	 */
	private Integer codeDataDictionaryValFrom;
	/**
	 * 编码数据字典id
	 */
	private String codeDataDictionaryId;
	
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
	 * 查询条件值的属性id
	 * <p>即当前对象中的某一个属性值作为条件，这个字段，ruleType为5和6，都使用</p>
	 */
	private String queryCondValPropId;
	/**
	 * 查询条件值的属性来源
	 * <p>默认值为1，1:CfgColumn、2:CfgSqlParameter，这个字段，ruleType为5和6，都使用</p>
	 */
	private Integer queryCondValPropFrom;
	
	/**
	 * 排序列的id
	 * <p>这个字段，ruleType为5和6，都使用</p>
	 */
	private String orderByColumnId;
	/**
	 * 排序的方式
	 * <p>默认值为asc，可为asc、desc，这个字段，ruleType为5和6，都使用</p>
	 */
	private String orderByMethod;
	// ------------------------
	/**
	 * 值截取的起始位置
	 * <p>默认值为0，该配置优先级高于正则表达式，目前只有ruleType为6时才有效</p>
	 */
	private Integer valSubStartIndex;
	/**
	 * 值截取的结束位置
	 * <p>默认值为0，该配置优先级高于正则表达式，目前只有ruleType为6时才有效</p>
	 */
	private Integer valSubEndIndex;
	/**
	 * 值截取的正则表达式，目前只有ruleType为6时才有效
	 */
	private String valSubRegex;
	/**
	 * 值截取的正则表达式第n次匹配
	 * <p>默认值为1，取第一次匹配的值，目前只有ruleType为6时才有效</p>
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
		if(linkNextSymbol == null){
			return "";
		}
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
	public String getRecSeqParentColumnId() {
		return recSeqParentColumnId;
	}
	public void setRecSeqParentColumnId(String recSeqParentColumnId) {
		this.recSeqParentColumnId = recSeqParentColumnId;
	}
	public String getRecSeqTableId() {
		return recSeqTableId;
	}
	public void setRecSeqTableId(String recSeqTableId) {
		this.recSeqTableId = recSeqTableId;
	}
	public String getRecSeqCodeColumnId() {
		return recSeqCodeColumnId;
	}
	public void setRecSeqCodeColumnId(String recSeqCodeColumnId) {
		this.recSeqCodeColumnId = recSeqCodeColumnId;
	}
	public String getRecSeqLinkSymbol() {
		return recSeqLinkSymbol;
	}
	public void setRecSeqLinkSymbol(String recSeqLinkSymbol) {
		this.recSeqLinkSymbol = recSeqLinkSymbol;
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
	public Integer getCodeDataDictionaryValFrom() {
		return codeDataDictionaryValFrom;
	}
	public void setCodeDataDictionaryValFrom(Integer codeDataDictionaryValFrom) {
		this.codeDataDictionaryValFrom = codeDataDictionaryValFrom;
	}
	public String getCodeDataDictionaryId() {
		return codeDataDictionaryId;
	}
	public void setCodeDataDictionaryId(String codeDataDictionaryId) {
		this.codeDataDictionaryId = codeDataDictionaryId;
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
	public String getQueryCondValPropId() {
		return queryCondValPropId;
	}
	public void setQueryCondValPropId(String queryCondValPropId) {
		this.queryCondValPropId = queryCondValPropId;
	}
	public Integer getQueryCondValPropFrom() {
		return queryCondValPropFrom;
	}
	public void setQueryCondValPropFrom(Integer queryCondValPropFrom) {
		this.queryCondValPropFrom = queryCondValPropFrom;
	}
	public String getOrderByColumnId() {
		return orderByColumnId;
	}
	public void setOrderByColumnId(String orderByColumnId) {
		this.orderByColumnId = orderByColumnId;
	}
	public String getOrderByMethod() {
		return orderByMethod;
	}
	public void setOrderByMethod(String orderByMethod) {
		this.orderByMethod = orderByMethod;
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
		List<CfgColumn> columns = new ArrayList<CfgColumn>(30+7);
		
		CfgColumn refPropCodeRuleIdColumn = new CfgColumn("ref_prop_code_rule_id", DataTypeConstants.STRING, 32);
		refPropCodeRuleIdColumn.setName("关联的属性编码规则id");
		refPropCodeRuleIdColumn.setComments("关联的属性编码规则id");
		columns.add(refPropCodeRuleIdColumn);
		
		CfgColumn linkNextSymbolColumn = new CfgColumn("link_next_symbol", DataTypeConstants.STRING, 5);
		linkNextSymbolColumn.setName("连接符");
		linkNextSymbolColumn.setComments("当前段连接下一段的连接符；如果没有下一段，则连接符无效；默认值为空字符串");
		columns.add(linkNextSymbolColumn);
		
		CfgColumn orderCodeColumn = new CfgColumn("order_code", DataTypeConstants.INTEGER, 4);
		orderCodeColumn.setName("排序值");
		orderCodeColumn.setComments("按照顺序从小到大组装编码结果");
		columns.add(orderCodeColumn);
		
		CfgColumn ruleTypeColumn = new CfgColumn("rule_type", DataTypeConstants.INTEGER, 1);
		ruleTypeColumn.setName("规则类型");
		ruleTypeColumn.setComments("0:default(默认固定值)、1:date(日期)、2:seq(序列)、3:recursive_seq(递归序列)、4:serialNumber(流水号)、5:random(随机数)、6:column(其他列值)、7:code_data_dictionary(编码数据字典值)、8:weekCalendar(周日历，即当前日期是今年第几周)、9:seasonCalendar(季度日历，即当前日期是今年第几季度)，默认值为0");
		ruleTypeColumn.setDefaultValue("0");
		columns.add(ruleTypeColumn);
		
		CfgColumn defValueColumn = new CfgColumn("def_value", DataTypeConstants.STRING, 10);
		defValueColumn.setName("默认固定值");
		defValueColumn.setComments("默认值为空字符串");
		columns.add(defValueColumn);
		
		CfgColumn dateFormateColumn = new CfgColumn("date_formate", DataTypeConstants.STRING, 20);
		dateFormateColumn.setName("日期格式化格式");
		dateFormateColumn.setComments("yyyy表示年，MM表示月，dd表示日，hh表示小时，mm表示分钟，ss表示秒，SSS表示毫秒，可任意组装，默认值为yyyyMMddHHmmssSSS");
		dateFormateColumn.setDefaultValue("yyyyMMddHHmmssSSS");
		columns.add(dateFormateColumn);
		
		CfgColumn seqReinitTimeColumn = new CfgColumn("seq_reinit_time", DataTypeConstants.INTEGER, 1);
		seqReinitTimeColumn.setName("序列再次初始化的时机");
		seqReinitTimeColumn.setComments("0:none(不重新初始化)、1:hour(每小时)、2:day(每天)、3:month(每月)、4:year(每年)、5:week(每周)、6:quarter(每季度)，默认值为0，这个字段，ruleType为2、3、4，都使用");
		seqReinitTimeColumn.setDefaultValue("0");
		columns.add(seqReinitTimeColumn);
		
		CfgColumn seqStartValColumn = new CfgColumn("seq_start_val", DataTypeConstants.INTEGER, 8);
		seqStartValColumn.setName("序列的起始值");
		seqStartValColumn.setComments("序列的起始值，默认值是1，这个字段，ruleType为2、3、4，都使用");
		seqStartValColumn.setDefaultValue("1");
		columns.add(seqStartValColumn);
		
		CfgColumn seqSkipValColumn = new CfgColumn("seq_skip_val", DataTypeConstants.INTEGER, 4);
		seqSkipValColumn.setName("序列的间隔值");
		seqSkipValColumn.setComments("序列值每次自增的大小，默认值为1，这个字段，ruleType为2、3、4，都使用");
		seqSkipValColumn.setDefaultValue("1");
		columns.add(seqSkipValColumn);
		
		CfgColumn recSeqTableIdColumn = new CfgColumn("rec_seq_table_id", DataTypeConstants.STRING, 32);
		recSeqTableIdColumn.setName("递归序列引用的表id");
		recSeqTableIdColumn.setComments("如果是给表资源配，该值默认是当前配置字段所属的表id；如果是给sql参数配置，该值是用户选择的一张表id");
		columns.add(recSeqTableIdColumn);
		
		CfgColumn recSeqCodeColumnIdColumn = new CfgColumn("rec_seq_code_column_id", DataTypeConstants.STRING, 32);
		recSeqCodeColumnIdColumn.setName("递归序列引用的编码列id");
		recSeqCodeColumnIdColumn.setComments("如果是给表资源配，该值默认是当前配置的字段id；如果是给sql参数配置，则是rec_seq_table_id字段存储的表中的某个字段id");
		columns.add(recSeqCodeColumnIdColumn);
		
		CfgColumn recSeqParentColumnIdColumn = new CfgColumn("rec_seq_parent_column_id", DataTypeConstants.STRING, 32);
		recSeqParentColumnIdColumn.setName("递归序列引用的父列id");
		recSeqParentColumnIdColumn.setComments("就是表中，实现递归的字段，例如一般都是parent_id");
		columns.add(recSeqParentColumnIdColumn);
		
		CfgColumn recSeqLinkSymbolColumn = new CfgColumn("rec_seq_link_symbol", DataTypeConstants.STRING, 5);
		recSeqLinkSymbolColumn.setName("递归序列间的连接符");
		recSeqLinkSymbolColumn.setComments("默认值为.");
		recSeqLinkSymbolColumn.setDefaultValue(".");
		columns.add(recSeqLinkSymbolColumn);
		
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
		
		CfgColumn codeDataDictionaryValFromColumn = new CfgColumn("code_data_dictionary_val_from", DataTypeConstants.INTEGER, 1);
		codeDataDictionaryValFromColumn.setName("编码数据字典值的来源");
		codeDataDictionaryValFromColumn.setComments("0:当前数据、1:当前数据资源对象、2:其他数据资源对象，默认值为0");
		codeDataDictionaryValFromColumn.setDefaultValue("0");
		columns.add(codeDataDictionaryValFromColumn);
		
		CfgColumn codeDataDictionaryIdColumn = new CfgColumn("code_data_dictionary_id", DataTypeConstants.STRING, 50);
		codeDataDictionaryIdColumn.setName("编码数据字典id");
		codeDataDictionaryIdColumn.setComments("编码数据字典id");
		columns.add(codeDataDictionaryIdColumn);
		
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
		
		CfgColumn queryCondValPropIdColumn = new CfgColumn("query_cond_val_prop_id", DataTypeConstants.STRING, 32);
		queryCondValPropIdColumn.setName("查询条件值的属性id");
		queryCondValPropIdColumn.setComments("即当前对象中的某一个属性值作为条件，这个字段，ruleType为5和6，都使用");
		columns.add(queryCondValPropIdColumn);
		
		CfgColumn queryCondValPropFromColumn = new CfgColumn("query_cond_val_prop_from", DataTypeConstants.INTEGER, 1);
		queryCondValPropFromColumn.setName("查询条件值的属性来源");
		queryCondValPropFromColumn.setComments("默认值为1，1:CfgColumn、2:CfgSqlParameter，这个字段，ruleType为5和6，都使用");
		queryCondValPropFromColumn.setDefaultValue("1");
		columns.add(queryCondValPropFromColumn);
		
		CfgColumn orderByColumnIdColumn = new CfgColumn("order_by_column_id", DataTypeConstants.STRING, 32);
		orderByColumnIdColumn.setName("排序列的id");
		orderByColumnIdColumn.setComments("这个字段，ruleType为5和6，都使用");
		columns.add(orderByColumnIdColumn);
		
		CfgColumn orderByMethodColumn = new CfgColumn("order_by_method", DataTypeConstants.STRING, 4);
		orderByMethodColumn.setName("排序的方式");
		orderByMethodColumn.setComments("默认值为asc，可为asc、desc，这个字段，ruleType为5和6，都使用");
		orderByMethodColumn.setDefaultValue("asc");
		columns.add(orderByMethodColumn);
		
		CfgColumn valSubStartIndexColumn = new CfgColumn("val_sub_start_index", DataTypeConstants.INTEGER, 3);
		valSubStartIndexColumn.setName("值截取的起始位置");
		valSubStartIndexColumn.setComments("默认值为0，该配置优先级高于正则表达式，目前只有ruleType为6时才有效");
		valSubStartIndexColumn.setDefaultValue("0");
		columns.add(valSubStartIndexColumn);
		
		CfgColumn valSubEndIndexColumn = new CfgColumn("val_sub_end_index", DataTypeConstants.INTEGER, 3);
		valSubEndIndexColumn.setName("值截取的结束位置");
		valSubEndIndexColumn.setComments("默认值为0，该配置优先级高于正则表达式，目前只有ruleType为6时才有效");
		valSubEndIndexColumn.setDefaultValue("0");
		columns.add(valSubEndIndexColumn);
		
		CfgColumn valSubRegexColumn = new CfgColumn("val_sub_regex", DataTypeConstants.STRING, 300);
		valSubRegexColumn.setName("值截取的正则表达式");
		valSubRegexColumn.setComments("值截取的正则表达式，目前只有ruleType为6时才有效");
		columns.add(valSubRegexColumn);
		
		CfgColumn valSubMatchNumColumn = new CfgColumn("val_sub_match_num", DataTypeConstants.INTEGER, 2);
		valSubMatchNumColumn.setName("值截取的正则表达式第n次匹配");
		valSubMatchNumColumn.setComments("默认值为1，取第一次匹配的值，目前只有ruleType为6时才有效");
		valSubMatchNumColumn.setDefaultValue("1");
		columns.add(valSubMatchNumColumn);
		
		return columns;
	}
	
	public CfgTable toCreateTable() {
		CfgTable table = new CfgTable(toDropTable());
		table.setName("属性编码规则明细表");
		table.setRemark("属性编码规则明细表");
		
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
	
	/**
	 * 是否需要锁住
	 * <p>需要锁住的ruleType包括：2:seq(序列)、3:recursive_seq(递归序列)、4:serialNumber(流水号)</p>
	 * @return
	 */
	public boolean isNeedLock(){
		return ruleType > 1 || ruleType < 5;
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
			HibernateUtil.saveObject(seq, null);
		}else{
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
		if(StrUtils.isEmpty(recSeqParentPropName)){
			recSeqParentPropName = getPropInfoById(recSeqParentColumnId, false)[0];
		}
		String parentSeqValue = null;
		Object parentIdValue = currentJsonObject.get(recSeqParentPropName);
		if(StrUtils.notEmpty(parentIdValue)){// 不为空，表示是子数据，则要查询出上级数据的编号值
			if(StrUtils.isEmpty(recSeqParentCodeValQueryHql)){
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
		Object value = currentJsonObject.get(getPropInfoById(queryCondValPropId, true)[0]);// 0:默认从当前数据中获取值
		if(valueFrom > 0){
			if(valueFrom == 2){// 2:其他数据资源对象
				resourceName = getTableResourceNameById(refTableId);
			}
			String orderBy = installOrderBy();
			String[] queryCondColumnInfo = getPropInfoById(queryCondColumnId, false);
			List<Object> list = HibernateUtil.executeListQueryByHqlArr("1", "1", "select "+getPropInfoById(refColumnId, false)[0]+" from "+resourceName+" where "+queryCondColumnInfo[0]+"=?" + orderBy, DataTypeTurnUtil.turnValueDataType(value, queryCondColumnInfo[1], true, true, true));
			if(list != null && list.size() > 0){
				value = list.get(0);
				list.clear();
			}else{
				value = "";
			}
		}
		return value;
	}
	
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
		List<Object[]> codeDataDictionarys = HibernateUtil.executeListQueryByHqlArr(null, null, queryCodeDataDictionaryHql, CurrentThreadContext.getProjectId(), CurrentThreadContext.getCustomerId(), codeDataDictionaryId);
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
			codeDataDictionarys.clear();
		}
		return value;
	}
	/** 查询编码数据字典值的hql语句 */
	private static final String queryCodeDataDictionaryHql = "select propValue, codeValue from CfgCodeDataDictionary where isEnabled=1 and projectId=? and customerId=? and parentId=?";
	
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
