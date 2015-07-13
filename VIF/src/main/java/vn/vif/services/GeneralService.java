package vn.vif.services;

import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import vn.vif.daos.Filter;
import vn.vif.utils.converter.OptionItem;

public interface GeneralService<T> {

	public void add(T entity);
	
	public void add(Session session, T entity) throws HibernateException;

	public void update(T entity);
	
	public void update(Session session, T entity) throws HibernateException;

	public T merge(T entity);

	public void delete(T entity);
	
	public void delete(Session session, T entity) throws HibernateException;

	public List<T> list();
	
	public List<OptionItem> listOptionItems(String optionKeyName,String OptionValueName);
	
	public List<OptionItem> listOptionItems(String optionKeyName,String OptionValueName, String hql);
	
	public List<OptionItem> listOptionItemsWithCode(String optionKeyName, String optionValueName, String optionCodeName);
	
	public List<OptionItem> listOptionItemsWithCode(String optionKeyName, String optionValueName, String optionCodeName, String hql);

	public T find(long id);

	public T findLazy(long id);
	
	public List<T> list(String[] field, Object[] value, boolean andCompare, Map<String, Boolean> sortFields, int start,
			int length);

	public abstract Class<T> getEntityClass();

	public Object list(String[] field, Object[] value, boolean andCompare, Map<String, Boolean> sortFields, int start,
			int length, String loadField);

	public long count();

	public long count(String[] field, Object[] value, boolean andCompare, Map<String, Boolean> sortFields);

	public void executeSQLQuery(String sql, Object[] values);

	public List<?> executeQuery(String sql, Object[] values, int start, int length);
	
	public List<T> list(Filter filter, int start, int length);
	public List<T> list(Filter filter);
	
	public long count(Filter filter);
	public Session openSession();
	public void closeSession(Session session);
}
