package org.hibernate.types.sqlserver;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;

import com.king.tooth.util.DateUtil;
import com.king.tooth.util.StrUtils;

/**
 * 自定义的timestamp类型
 * @author DougLei
 */
public class Timestamp implements UserType{
	private static final int[] SQL_TYPES = {Types.TIMESTAMP};
	
	public int[] sqlTypes() {
		return SQL_TYPES;
	}

	@SuppressWarnings("rawtypes")
	public Class returnedClass() {
		return String.class;
	}

	public boolean equals(Object x, Object y) throws HibernateException {
		return true;
	}

	public int hashCode(Object x) throws HibernateException {
		return x.hashCode();
	}

	/**
	 * 数据读取时被调用
	 */
	public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
		return rs.getObject(names[0]);
	}

	/**
	 * 数据保存时被调用
	 */
	public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
		if(StrUtils.notEmpty(value)){
			if(value instanceof String){
				st.setTimestamp(index, DateUtil.parseTimestamp(value.toString()));
			}else{
				st.setTimestamp(index, DateUtil.parseTimestamp((java.util.Date)value));
			}
		}else{
			st.setNull(index, Types.TIMESTAMP);
		}
	}

	public Object deepCopy(Object value) throws HibernateException {
		return value;
	}

	public boolean isMutable() {
		return false;
	}

	public Serializable disassemble(Object value) throws HibernateException {
		return null;
	}

	public Object assemble(Serializable cached, Object owner) throws HibernateException {
		return null;
	}

	public Object replace(Object original, Object target, Object owner) throws HibernateException {
		return null;
	}
}
