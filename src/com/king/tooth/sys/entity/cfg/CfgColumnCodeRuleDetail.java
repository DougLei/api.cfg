package com.king.tooth.sys.entity.cfg;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.annotation.Table;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.sys.builtin.data.BuiltinDataType;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.IEntityPropAnalysis;
import com.king.tooth.sys.entity.ISysResource;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.util.DateUtil;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 字段编码规则明细表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class CfgColumnCodeRuleDetail extends BasicEntity implements ITable, IEntity, IEntityPropAnalysis{
	
	/**
	 * 关联的字段编码规则id
	 */
	private String columnCodeRuleId;
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
	 * <p>0:default(默认固定值)、1:date(日期)、2:seq(序列)、3:serialNumber(流水号)、4:random(随机数)、5:column(其他列值)、6:type(类型值)</p>
	 * <p>0</p>
	 */
	private Integer ruleType;
	
	/**
	 * 默认固定值
	 * <p>默认值为空字符串</p>
	 */
	private String defValue;
	
	/**
	 * 日期格式化格式
	 * <p>yyyy表示年，MM表示月，dd表示日，hh表示小时，mm表示分钟，ss表示秒，SSS表示毫秒，可任意组装</p>
	 * <p>默认值为yyyyMMddHHmmssSSS</p>
	 */
	private String dateFormate;
	
	/**
	 * 序列再次初始化的时机
	 * <p>0:none(不重新初始化)、1:hour(每小时)、2:day(每天)、3:month(每月)、4:year(每年)、5:week(每周)、6:quarter(每季度)</p>
	 * <p>默认值为0</p>
	 */
	private Integer seqReinitTime;
	/**
	 * 序列的起始值
	 * <p>默认值是1</p>
	 */
	private Integer seqStartVal;
	/**
	 * 序列的当前值
	 */
	private Integer seqCurrentVal;
	/**
	 * 序列的间隔值
	 * <p>序列值每次自增的大小</p>
	 * <p>默认值为1</p>
	 */
	private Integer seqSkipVal;
	
	/**
	 * 随机数长度
	 * <p>默认值为6</p>
	 */
	private Integer randomLength;
	/**
	 * 随机数种子的值
	 * <p>随机数长度定义随机数种子的值，默认为100000</p>
	 */
	private Integer randomSeedVal;
	
	/**
	 * 列值的来源
	 * <p>0:当前数据、1:当前数据资源对象、2:其他数据资源对象</p>
	 * <p>默认值为0</p>
	 */
	private Integer columnValFrom;
	/**
	 * 引用表的id
	 */
	private String refTableId;
	/**
	 * 引用列的id
	 */
	private String refColumnId;
	/**
	 * 查询列的id
	 */
	private String queryColumnId;
	/**
	 * 查询列的条件值
	 */
	private String queryColumnCondVal;
	
	/**
	 * 类型值的列值来源
	 * <p>0:当前数据、1:当前数据资源对象、2:其他数据资源对象</p>
	 * <p>默认值为0</p>
	 */
	private Integer typeColumnValFrom;
	/**
	 * 类型引用表的id
	 */
	private String typeRefTableId;
	/**
	 * 类型引用列的id
	 */
	private String typeRefColumnId;
	/**
	 * 类型查询列的id
	 */
	private String typeQueryColumnId;
	/**
	 * 类型查询列的条件值
	 */
	private String typeQueryColumnCondVal;
	/**
	 * 类型映射json
	 * <p>这种结构：{"类型值1":"展示值1","类型值2":"展示值2" ...}</p>
	 */
	private String typeMapJson;
	
	//-------------------------------------------------------------------------

	public String getColumnCodeRuleId() {
		return columnCodeRuleId;
	}
	public void setColumnCodeRuleId(String columnCodeRuleId) {
		this.columnCodeRuleId = columnCodeRuleId;
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
	public Integer getSeqCurrentVal() {
		return seqCurrentVal;
	}
	public void setSeqCurrentVal(Integer seqCurrentVal) {
		this.seqCurrentVal = seqCurrentVal;
	}
	public Integer getSeqSkipVal() {
		return seqSkipVal;
	}
	public void setSeqSkipVal(Integer seqSkipVal) {
		this.seqSkipVal = seqSkipVal;
	}
	public Integer getRandomLength() {
		return randomLength;
	}
	public void setRandomLength(Integer randomLength) {
		this.randomLength = randomLength;
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
	public String getQueryColumnId() {
		return queryColumnId;
	}
	public void setQueryColumnId(String queryColumnId) {
		this.queryColumnId = queryColumnId;
	}
	public String getQueryColumnCondVal() {
		return queryColumnCondVal;
	}
	public void setQueryColumnCondVal(String queryColumnCondVal) {
		this.queryColumnCondVal = queryColumnCondVal;
	}
	public Integer getTypeColumnValFrom() {
		return typeColumnValFrom;
	}
	public void setTypeColumnValFrom(Integer typeColumnValFrom) {
		this.typeColumnValFrom = typeColumnValFrom;
	}
	public String getTypeRefTableId() {
		return typeRefTableId;
	}
	public void setTypeRefTableId(String typeRefTableId) {
		this.typeRefTableId = typeRefTableId;
	}
	public String getTypeRefColumnId() {
		return typeRefColumnId;
	}
	public void setTypeRefColumnId(String typeRefColumnId) {
		this.typeRefColumnId = typeRefColumnId;
	}
	public String getTypeQueryColumnId() {
		return typeQueryColumnId;
	}
	public void setTypeQueryColumnId(String typeQueryColumnId) {
		this.typeQueryColumnId = typeQueryColumnId;
	}
	public String getTypeQueryColumnCondVal() {
		return typeQueryColumnCondVal;
	}
	public void setTypeQueryColumnCondVal(String typeQueryColumnCondVal) {
		this.typeQueryColumnCondVal = typeQueryColumnCondVal;
	}
	public String getTypeMapJson() {
		return typeMapJson;
	}
	public void setTypeMapJson(String typeMapJson) {
		this.typeMapJson = typeMapJson;
	}
	
	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata("CFG_COLUMN_CODE_RULE_DETAIL", 0);
		table.setName("字段编码规则表");
		table.setComments("字段编码规则表");
		table.setIsBuiltin(1);
		table.setIsNeedDeploy(1);
		table.setIsCreated(1);
		table.setBelongPlatformType(ISysResource.COMMON_PLATFORM);
		
		List<ComColumndata> columns = new ArrayList<ComColumndata>(28);
		
		ComColumndata columnCodeRuleIdColumn = new ComColumndata("column_code_rule_id", BuiltinDataType.STRING, 32);
		columnCodeRuleIdColumn.setName("关联的字段编码规则id");
		columnCodeRuleIdColumn.setComments("关联的字段编码规则id");
		columns.add(columnCodeRuleIdColumn);
		
		ComColumndata linkNextSymbolColumn = new ComColumndata("link_next_symbol", BuiltinDataType.STRING, 5);
		linkNextSymbolColumn.setName("连接符");
		linkNextSymbolColumn.setComments("当前段连接下一段的连接符；如果没有下一段，则连接符无效；默认值为空字符串");
		linkNextSymbolColumn.setDefaultValue("");
		columns.add(linkNextSymbolColumn);
		
		ComColumndata orderCodeColumn = new ComColumndata("order_code", BuiltinDataType.INTEGER, 4);
		orderCodeColumn.setName("排序值");
		orderCodeColumn.setComments("按照顺序从小到大组装编码结果");
		columns.add(orderCodeColumn);
		
		ComColumndata ruleTypeColumn = new ComColumndata("rule_type", BuiltinDataType.INTEGER, 1);
		ruleTypeColumn.setName("规则类型");
		ruleTypeColumn.setComments("0:default(默认固定值)、1:date(日期)、2:seq(序列)、3:serialNumber(流水号)、4:random(随机数)、5:column(其他列值)、6:type(类型值)，默认值为0");
		ruleTypeColumn.setDefaultValue("0");
		columns.add(ruleTypeColumn);
		
		ComColumndata defValueColumn = new ComColumndata("defValue", BuiltinDataType.STRING, 10);
		defValueColumn.setName("默认固定值");
		defValueColumn.setComments("默认值为空字符串");
		defValueColumn.setDefaultValue("");
		columns.add(defValueColumn);
		
		ComColumndata dateFormateColumn = new ComColumndata("date_formate", BuiltinDataType.STRING, 20);
		dateFormateColumn.setName("日期格式化格式");
		dateFormateColumn.setComments("yyyy表示年，MM表示月，dd表示日，hh表示小时，mm表示分钟，ss表示秒，SSS表示毫秒，可任意组装，默认值为yyyyMMddHHmmssSSS");
		dateFormateColumn.setDefaultValue("yyyyMMddHHmmssSSS");
		columns.add(dateFormateColumn);
		
		ComColumndata seqReinitTimeColumn = new ComColumndata("seq_reinit_time", BuiltinDataType.INTEGER, 1);
		seqReinitTimeColumn.setName("序列再次初始化的时机");
		seqReinitTimeColumn.setComments("0:none(不重新初始化)、1:hour(每小时)、2:day(每天)、3:month(每月)、4:year(每年)、5:week(每周)、6:quarter(每季度)，默认值为0");
		seqReinitTimeColumn.setDefaultValue("0");
		columns.add(seqReinitTimeColumn);
		
		ComColumndata seqStartValColumn = new ComColumndata("seq_start_val", BuiltinDataType.INTEGER, 8);
		seqStartValColumn.setName("序列的起始值");
		seqStartValColumn.setComments("序列的起始值，默认值是1");
		seqStartValColumn.setDefaultValue("1");
		columns.add(seqStartValColumn);
		
		ComColumndata seqCurrentValColumn = new ComColumndata("seq_current_val", BuiltinDataType.INTEGER, 8);
		seqCurrentValColumn.setName("序列的当前值");
		seqCurrentValColumn.setComments("序列的当前值");
		columns.add(seqCurrentValColumn);
		
		ComColumndata seqSkipValColumn = new ComColumndata("seq_skip_val", BuiltinDataType.INTEGER, 4);
		seqSkipValColumn.setName("序列的间隔值");
		seqSkipValColumn.setComments("序列值每次自增的大小，默认值为1");
		seqSkipValColumn.setDefaultValue("1");
		columns.add(seqSkipValColumn);
		
		ComColumndata randomLengthColumn = new ComColumndata("random_length", BuiltinDataType.INTEGER, 15);
		randomLengthColumn.setName("随机数长度");
		randomLengthColumn.setComments("默认值为6");
		randomLengthColumn.setDefaultValue("6");
		columns.add(randomLengthColumn);
		
		ComColumndata randomSeedValColumn = new ComColumndata("random_seed_val", BuiltinDataType.INTEGER, 15);
		randomSeedValColumn.setName("随机数种子的值");
		randomSeedValColumn.setComments("随机数长度定义随机数种子的值，默认为");
		randomSeedValColumn.setDefaultValue("100000");
		columns.add(randomSeedValColumn);
		
		ComColumndata columnValFromColumn = new ComColumndata("column_val_from", BuiltinDataType.INTEGER, 1);
		columnValFromColumn.setName("列值的来源");
		columnValFromColumn.setComments("0:当前数据、1:当前数据资源对象、2:其他数据资源对象，默认值为0");
		columnValFromColumn.setDefaultValue("0");
		columns.add(columnValFromColumn);
		
		ComColumndata refTableIdColumn = new ComColumndata("ref_table_id", BuiltinDataType.STRING, 32);
		refTableIdColumn.setName("引用表的id");
		refTableIdColumn.setComments("引用表的id");
		columns.add(refTableIdColumn);
		
		ComColumndata refColumnIdColumn = new ComColumndata("ref_column_id", BuiltinDataType.STRING, 32);
		refColumnIdColumn.setName("引用列的id");
		refColumnIdColumn.setComments("引用列的id");
		columns.add(refColumnIdColumn);
		
		ComColumndata queryColumnIdColumn = new ComColumndata("query_column_id", BuiltinDataType.STRING, 32);
		queryColumnIdColumn.setName("查询列的id");
		queryColumnIdColumn.setComments("查询列的id");
		columns.add(queryColumnIdColumn);
		
		ComColumndata queryColumnCondValColumn = new ComColumndata("query_column_cond_val", BuiltinDataType.STRING, 60);
		queryColumnCondValColumn.setName("查询列的条件值");
		queryColumnCondValColumn.setComments("查询列的条件值");
		columns.add(queryColumnCondValColumn);
		
		ComColumndata typeColumnValFromColumn = new ComColumndata("type_column_val_from", BuiltinDataType.INTEGER, 1);
		typeColumnValFromColumn.setName("类型值的列值来源");
		typeColumnValFromColumn.setComments("0:当前数据、1:当前数据资源对象、2:其他数据资源对象，默认值为0");
		typeColumnValFromColumn.setDefaultValue("0");
		columns.add(typeColumnValFromColumn);
		
		ComColumndata typeRefTableIdColumn = new ComColumndata("type_ref_table_id", BuiltinDataType.STRING, 32);
		typeRefTableIdColumn.setName("类型引用表的id");
		typeRefTableIdColumn.setComments("类型引用表的id");
		columns.add(typeRefTableIdColumn);
		
		ComColumndata typeRefColumnIdColumn = new ComColumndata("type_ref_column_id", BuiltinDataType.STRING, 32);
		typeRefColumnIdColumn.setName("类型引用列的id");
		typeRefColumnIdColumn.setComments("类型引用列的id");
		columns.add(typeRefColumnIdColumn);
		
		ComColumndata typeQueryColumnIdColumn = new ComColumndata("type_query_column_id", BuiltinDataType.STRING, 32);
		typeQueryColumnIdColumn.setName("类型查询列的id");
		typeQueryColumnIdColumn.setComments("类型查询列的id");
		columns.add(typeQueryColumnIdColumn);
		
		ComColumndata typeQueryColumnCondValColumn = new ComColumndata("type_query_column_cond_val", BuiltinDataType.STRING, 60);
		typeQueryColumnCondValColumn.setName("类型查询列的条件值");
		typeQueryColumnCondValColumn.setComments("类型查询列的条件值");
		columns.add(typeQueryColumnCondValColumn);
		
		ComColumndata typeMapJsonColumn = new ComColumndata("type_map_json", BuiltinDataType.STRING, 300);
		typeMapJsonColumn.setName("类型映射json");
		typeMapJsonColumn.setComments("这种结构：{\"类型值1\":\"展示值1\",\"类型值2\":\"展示值2\" ...}");
		columns.add(typeMapJsonColumn);
		
		table.setColumns(columns);
		return table;
	}

	public String toDropTable() {
		return "CFG_COLUMN_CODE_RULE_DETAIL";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "CfgColumnCodeRuleDetail";
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
		switch(ruleType){
			case 0: // 0:default(默认固定值)
				return getDefaultVal(resourceName, currentJsonObject);
			case 1: // 1:date(日期)
				return getDate(resourceName, currentJsonObject);
			case 2: // 2:seq(序列)
				return getSeq(resourceName, currentJsonObject);
			case 3: // 3:serialNumber(流水号)
				return getSerialNumber(resourceName, currentJsonObject);
			case 4: // 4:random(随机数)
				return getRandom(resourceName, currentJsonObject);
			case 5: // 5:column(其他列值)
				return getColumn(resourceName, currentJsonObject);
			case 6: // 6:type(类型值)
				return getType(resourceName, currentJsonObject);
			default: // 默认值为0，0:default(默认固定值)
				return getDefaultVal(resourceName, currentJsonObject);
		}
	}
	
	/**
	 * 获取【0:default(默认固定值)】
	 * @param currentJsonObject
	 * @param resourceName
	 * @return
	 */
	private String getDefaultVal(String resourceName, JSONObject currentJsonObject) {
		return defValue;
	}
	
	/**
	 * 获取【1:date(日期)】
	 * @param currentJsonObject
	 * @param resourceName
	 * @return
	 */
	private String getDate(String resourceName, JSONObject currentJsonObject) {
		return new SimpleDateFormat(dateFormate).format(new Date());
	}
	
	/**
	 * 获取【2:seq(序列)】
	 * @param currentJsonObject
	 * @param resourceName
	 * @return
	 */
	private String getSeq(String resourceName, JSONObject currentJsonObject) {
		// TODO 
		return null;
	}
	
	/**
	 * 获取【3:serialNumber(流水号)】
	 * @param currentJsonObject
	 * @param resourceName
	 * @return
	 */
	private synchronized String getSerialNumber(String resourceName, JSONObject currentJsonObject) {
		return DateUtil.getSerialNumberDateStr();
	}
	
	/**
	 * 获取【4:random(随机数)】
	 * @param currentJsonObject
	 * @param resourceName
	 * @return
	 */
	private String getRandom(String resourceName, JSONObject currentJsonObject) {
		return ThreadLocalRandom.current().nextInt(randomSeedVal)+"";
	}
	
	/**
	 * 获取【5:column(其他列值)】
	 * @param currentJsonObject
	 * @param resourceName
	 * @return
	 */
	private String getColumn(String resourceName, JSONObject currentJsonObject) {
		ComColumndata column = HibernateUtil.extendExecuteUniqueQueryByHqlArr(ComColumndata.class, queryColumnPropNamesByIdHql, refColumnId);
		switch (columnValFrom) {
			case 0: // 0:当前数据
				return currentJsonObject.getString(column.getPropName());
			case 1: // 1:当前数据资源对象
				return "select " + column.getPropName() + " from " + resourceName + " where ";
			case 2: // 2:其他数据资源对象
				return null;
			default: // 默认值为0，0:当前数据
				return null;
		}
	}
	/** 根据ids查询column属性名集合的hql */
	private static final String queryColumnPropNamesByIdHql = "select propName from ComColumndata where " + ResourcePropNameConstants.ID + "=? and isEnabled=1 and operStatus="+ComColumndata.CREATED;
	
	/**
	 * 获取【6:type(类型值)】
	 * @param currentJsonObject
	 * @param resourceName
	 * @return
	 */
	private String getType(String resourceName, JSONObject currentJsonObject) {
		switch (typeColumnValFrom) {
			case 0: // 0:当前数据
				return null;
			case 1: // 1:当前数据资源对象
				return null;
			case 2: // 2:其他数据资源对象
				return null;
			default: // 默认值为0，0:当前数据
				return null;
		}
	}
}
