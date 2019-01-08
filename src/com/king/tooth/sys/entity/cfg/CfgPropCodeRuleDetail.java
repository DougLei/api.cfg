package com.king.tooth.sys.entity.cfg;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.annotation.Table;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.IEntityPropAnalysis;
import com.king.tooth.sys.entity.cfg.prop.code.rule.CurrentStageCodeProcesser;

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
		if(valSubStartIndex < 1){
			valSubStartIndex = 1;
		}
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
		if(valSubMatchNum < 1){
			valSubMatchNum = 1;
		}
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
		return ruleType > 1 && ruleType < 5;
	}
	
	// ------------------------------------------------------------------------------------------------------
	
	/**
	 * 当前段的编码值处理者实例
	 */
	@JSONField(serialize = false)
	private CurrentStageCodeProcesser cscp;
	
	/**
	 * 关联的属性类型
	 * <p>1:column(列)、2:sqlparam(sql参数)</p>
	 */
	@JSONField(serialize = false)
	private int refPropType;
	public int getRefPropType() {
		return refPropType;
	}
	
	/**
	 * 得到当前段的编码值
	 * @param resourceName 当前对象资源名
	 * @param refPropType 关联的属性类型，1:column(列)、2:sqlparam(sql参数)
	 * @param currentJsonObject 当前对象的json对象，获取当前段的值的时候，可能会用到当前对象的数据
	 * @return
	 */
	public String getCurrentStageCodeVal(String resourceName, int refPropType, JSONObject currentJsonObject) {
		if(cscp == null){
			this.refPropType = refPropType;
			cscp = new CurrentStageCodeProcesser(this);
		}
		return cscp.getCodeVal(resourceName, currentJsonObject);
	}
}
