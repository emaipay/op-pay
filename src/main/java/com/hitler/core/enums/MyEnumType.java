package com.hitler.core.enums;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.DynamicParameterizedType;
import org.hibernate.usertype.UserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import com.hitler.core.utils.EnumUtil;

/**
 * 用户持久化枚举类型的用户自定义hibernate映射类型
 * <p>
 * 使用此类型来进行映射的枚举类，必须实现{@PersistEnum} 接口
 * 
 * @author Kylin
 * 2015-7-23 下午4:12:28
 */
public class MyEnumType implements UserType, DynamicParameterizedType, Serializable {
	private static final long serialVersionUID = 5629758848430519023L;
	protected Logger logger = LoggerFactory.getLogger(MyEnumType.class);

    private Class<Enum<?>> enumClass;
	
	@SuppressWarnings("unchecked")
	@Override
	public void setParameterValues(Properties parameters) {
		if (parameters != null) {
			final ParameterType reader = (ParameterType) parameters.get(PARAMETER_TYPE);
            try {
            	enumClass = reader.getReturnedClass().asSubclass( Enum.class );
            } catch (Exception e) {
            	logger.error("MyEnumType:" + e.getMessage());
			}
        }
	}

	/**
	 * 值转为枚举对象
	 */
	@Override
	public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner)
			throws HibernateException, SQLException {
		String value = rs.getString(names[0]);
		Object returnVal = null;

		if (value == null)
			return null;
		else {
			try {
				returnVal = EnumUtil.get(enumClass, value);
			} catch (Exception e) {
				logger.error("MyEnumType error" , e);
			}
		}
		return returnVal;
	}

	/**
	 * 枚举对象转为值 
	 */
	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session)
			throws HibernateException, SQLException {
		String prepStmtVal = null;

		if (value == null) {
			st.setObject(index, null);
		} else {
			try {
				PersistEnum<?> p = (PersistEnum<?>)value;
				prepStmtVal = p.getValue();
				st.setString(index, prepStmtVal);
			} catch (Exception e) {
				logger.error("MyEnumType error" , e);
			}
		}
	}
	
	@Override
	public int[] sqlTypes() {
		return new int[] { Types.VARCHAR };
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class returnedClass() {
		return enumClass;
	}

	@Override
	public boolean equals(Object x, Object y) throws HibernateException {
		return ObjectUtils.nullSafeEquals(x, y);
	}

	@Override
	public int hashCode(Object x) throws HibernateException {
		return x.hashCode();
	}

	@Override
	public Object deepCopy(Object value) throws HibernateException {
		return value;
	}

	@Override
	public boolean isMutable() {
		return false;
	}

	@Override
	public Serializable disassemble(Object value) throws HibernateException {
		Object deepCopy = deepCopy(value);
		 
        if (!(deepCopy instanceof Serializable))
            return (Serializable) deepCopy;
 
        return null;
	}

	@Override
	public Object assemble(Serializable cached, Object owner)
			throws HibernateException {
		return deepCopy(cached);
	}

	@Override
	public Object replace(Object original, Object target, Object owner)
			throws HibernateException {
		return deepCopy(original);
	}

}
