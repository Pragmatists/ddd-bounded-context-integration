package ddd.workshop.tickets.infrastructure.persistence;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Objects;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;

public class ValueObjectUserType implements UserType, ParameterizedType {

    private Class<?> type;
    protected int sqlType = Types.VARCHAR;
    private Method factoryMethod;

    public int[] sqlTypes() {
        return new int[] { sqlType };
    }

    public Class<?> returnedClass() {
        return type;
    }

    public boolean equals(Object x, Object y) throws HibernateException {

        return Objects.equals(x, y);
    }

    public int hashCode(Object x) throws HibernateException {

        return x.hashCode();
    }

    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    public boolean isMutable() {
        return false;
    }

    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }

    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] columns, SharedSessionContractImplementor session, Object owner)
            throws HibernateException, SQLException {

        String value = rs.getString(columns[0]);
        if(value == null){
            return null;
        }

        try {
        
            if(factoryMethod != null){
                return factoryMethod.invoke(null, value);
            }
        
            Constructor<?> constructor = type.getDeclaredConstructor(String.class);
            constructor.setAccessible(true);
            return constructor.newInstance(value);
        } catch (Exception e) {
            throw new RuntimeException("Could not find suitable constructor!", e);
        }
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session)
            throws HibernateException, SQLException {

        if(value == null){
            st.setNull(index, sqlType);
            return;
        }
        
        st.setString(index, value.toString());
    }

    @Override
    public void setParameterValues(Properties params) {
        String clazz = params.getProperty("class");
        try {
            type = Class.forName(clazz);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Could not register custom ValueObjectType", e);
        }

        try {

            String factory = params.getProperty("factoryMethod");
            if(factory != null){
                factoryMethod = type.getDeclaredMethod(factory, String.class);
                factoryMethod.setAccessible(true);
            } 
        } catch (NoSuchMethodException | SecurityException e) {
            throw new RuntimeException("Could not use factory method for ValueObjectType", e);
        }
    }
}
