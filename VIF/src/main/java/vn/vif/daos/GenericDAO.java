package vn.vif.daos;

import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public interface GenericDAO {
	
	//cho phep cac service truy cap vao session va thuc hien cac lenh batch
	public SessionFactory getSessionFactory();
	
	public void close(Session session);
	//
	public void add(Object entity);

	public void add(Session session, Object entity) throws HibernateException;
	
	public void update(Object entity);
	
	public void update(Session session, Object entity) throws HibernateException;

	public Object merge(Object entity);

	public void delete(Object entity);
	
	public void delete(Session session, Object entity) throws HibernateException;

	public List<?> list(Class<?> objectClass);

	public Object find(Class<?> objectClass, long id);
	
	public Object find(Class<?> objectClass, long id, LazyLoader loader);

	public List<?> list(Class<?> objectClass, String[] fields, Object[] values, boolean andCompare,
			Map<String, Boolean> sortFields, int start, int length);

	public List<?> executeQuery(String sql, Object[] values, int start, int length);
	

	public List<?> list(Class<?> objectClass, String[] field, Object[] value, boolean andCompare,
			Map<String, Boolean> sortFields, int start, int length, String loadField);

	public void executeSQLQuery(String sql, Object[] values);
	
	/**
	 * Tạo danh sách thỏa mãn tiêu chuẩn lọc
	 * @param filter là một đối tượng lọc implements interface vn.itt.gpstracking.models.filters.Filter
	 * @param start trang bắt đầu của danh sách (dùng cho phân trang)
	 * @param length số dòng trên 1 trang
	 * @return danh sách
	 */
	public List<?> list(Filter filter, int start, int length);
	/**
	 * Đếm số bản ghi thỏa mãn tiêu chuẩn lọc
	 * @param filter là một đối tượng lọc implements interface vn.itt.gpstracking.models.filters.Filter
	 * @return count
	 */
	public long count(Filter filter);
	
	public List<?> list(Filter filter);
}
