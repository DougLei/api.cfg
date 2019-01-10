package org.hibernate.types;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;

import com.api.util.StrUtils;

/**
 * 自定义的double类型
 * @author DougLei
 */
public class Double implements UserType{
	private static final int[] SQL_TYPES = {Types.DECIMAL};
	
	public int[] sqlTypes() {
		return SQL_TYPES;
	}

	@SuppressWarnings("rawtypes")
	public Class returnedClass() {
		return java.lang.Double.class;
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
		Object obj = rs.getObject(names[0]);
		if(obj == null){
			return null;
		}
		return BigDecimal.valueOf(java.lang.Double.valueOf(obj.toString()));
	}

	/**
	 * 数据保存时被调用
	 */
	public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
		if(StrUtils.notEmpty(value)){
			st.setBigDecimal(index, new BigDecimal(value.toString()));
		}else{
			st.setNull(index, Types.DECIMAL);
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
