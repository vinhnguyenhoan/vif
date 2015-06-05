package vn.vif.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import vn.vif.daos.Filter;
import vn.vif.daos.GenericDAO;
import vn.vif.daos.LazyLoader;
import vn.vif.utils.GPSUtils;
import vn.vif.utils.converter.OptionItem;
import vn.vif.utils.converter.OptionItemComparator;

@SuppressWarnings("unchecked")
public abstract class GeneralServiceImpl<T> implements GeneralService<T> {

	@Autowired
	private GenericDAO genericDAO;
	@Autowired
	private MessageSource messageSource;
	protected LazyLoader lazyLoader = null;
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	public GenericDAO getGenericDAO() {
		return genericDAO;
	}

	public void setGenericDAO(GenericDAO genericDAO) {
		this.genericDAO = genericDAO;
	}

	public abstract Class<T> getEntityClass();
	
	public LazyLoader getLazyLoader() {
		return lazyLoader;
	}

	public List<T> list() {
		return (List<T>) genericDAO.list(getEntityClass());
	}

	/**
	 * Tạo danh mục các option trong dropdown của các combo trong các bộ lọc
	 * 
	 * @param optionKeyName
	 *            tên của field trong entity dùng làm key trả về trong option,
	 *            thường là <em>"id"</em>
	 * @param optionValueName
	 *            ten của field trong entity dùng làm value hiển thị trong
	 *            option, thường là <em>"ten"</em>
	 * @return list các option items tương ứng với bảng
	 */
	public List<OptionItem> listOptionItems(String optionKeyName,
			String optionValueName) {
		return listOptionItems(optionKeyName, optionValueName,
				String.format("from %s", getEntityClass().getSimpleName()));
	}

	/**
	 * Tạo danh mục các option trong dropdown của các combo trong các bộ lọc
	 * @param optionKeyName tên của field trong entity dùng làm key trả về trong option
	 * @param optionValueName tên của field trong entity dùng làm value hiển thị trong  option
	 * @param hql câu lệnh hql bắt đầu từ <em>from ....</em>, không có phần select và order by
	 * @return list các option items thỏa mãn hql
	 */
	public List<OptionItem> listOptionItems(String optionKeyName,
			String optionValueName, String hql) {
		SessionFactory factory = genericDAO.getSessionFactory();
		Session session = null;
		List<OptionItem> items = new ArrayList<OptionItem>();
		try {
			session = factory.openSession();
			String sql = String.format("select %s as id, %s as name %s order by %s asc",
					optionKeyName, optionValueName, hql, optionValueName);
			Query query = session.createQuery(sql);
			/*List<Object[]> queryresult = query.list();
			// cast các dòng về OptionItem
			for (Object[] rec : queryresult) {
				items.add(new OptionItem((Long) rec[0], (String) rec[1]));
			}*/
			items = query. setResultTransformer(Transformers.aliasToBean(OptionItem.class)).list();
			Collections.sort(items, new OptionItemComparator());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			genericDAO.close(session);
		}
		return items;
	}
	
	public List<OptionItem> listOptionItemsWithCode(String optionKeyName,
			String optionValueName, String optionCodeName) {
		return listOptionItemsWithCode(optionKeyName, optionValueName, optionCodeName,
				String.format("from %s", getEntityClass().getSimpleName()));
	}

	public List<OptionItem> listOptionItemsWithCode(String optionKeyName,
			String optionValueName, String optionCodeName, String hql) {
		SessionFactory factory = genericDAO.getSessionFactory();
		Session session = null;
		List<OptionItem> items = new ArrayList<OptionItem>();
		try {
			session = factory.openSession();
			String sql = String.format("select %s as id, " + GPSUtils.formatNameSQL("%s", "%s") + " as name %s order by %s asc",
					optionKeyName, optionCodeName, optionValueName, hql, optionValueName);
			Query query = session.createQuery(sql);
			/*List<Object[]> queryresult = query.list();
			// cast các dòng về OptionItem
			for (Object[] rec : queryresult) {
				items.add(new OptionItem((Long) rec[0], (String) rec[1]));
			}*/
			items = query. setResultTransformer(Transformers.aliasToBean(OptionItem.class)).list();
			Collections.sort(items, new OptionItemComparator());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			genericDAO.close(session);
		}
		return items;
	}
	
	
	public T find(long id) {
		return (T) genericDAO.find(getEntityClass(), id);
	}

	
	public T findLazy(long id) {
		return (T) genericDAO.find(getEntityClass(), id, getLazyLoader());
	}
	
	
	public List<T> list(String[] field, Object[] value, boolean andCompare,
			Map<String, Boolean> sortFields, int start, int length) {
		return (List<T>) genericDAO.list(getEntityClass(), field, value,
				andCompare, sortFields, start, length);
	}

	
	public Object list(String[] fieldNames, Object[] fieldValues,
			boolean andCompare, Map<String, Boolean> sortFields, int start,
			int length, String loadField) {
		return genericDAO.list(getEntityClass(), fieldNames, fieldValues,
				andCompare, sortFields, start, length, loadField);
	}

	
	public long count() {
		Long result = (Long) genericDAO.list(getEntityClass(), null, null,
				false, null, -1, -1, "count(*)").get(0);
		return result.longValue();
	}

	public long count(String[] field, Object[] value, boolean andCompare,
			Map<String, Boolean> sortFields) {
		Long result = (Long) genericDAO.list(getEntityClass(), field, value,
				andCompare, sortFields, -1, -1, "count(*)").get(0);
		return result.longValue();
	}

	protected void actionBeforeDelete(T entity) {
	}

	public void executeSQLQuery(String sql, Object[] values) {
		genericDAO.executeSQLQuery(sql, values);
	}

	
	public T merge(T entity) {
		T result = (T) genericDAO.merge(entity);
		return result;
	}

	
	public List<?> executeQuery(String sql, Object[] values, int start,
			int length) {
		return getGenericDAO().executeQuery(sql, values, start, length);
	}

	
	public void add(T entity) {
		getGenericDAO().add(entity);
	}
	
	
	public void add(Session session, T entity) throws HibernateException {
		getGenericDAO().add(session, entity);
	}

	
	public void update(T entity) {
		getGenericDAO().update(entity);
	}
	
	
	public void update(Session session, T entity) throws HibernateException {
		getGenericDAO().update(session, entity);
	}

	
	public void delete(T entity) {
		actionBeforeDelete(entity);
		getGenericDAO().delete(entity);
	}
	
	
	public void delete(Session session, T entity) throws HibernateException {
		actionBeforeDelete(entity);
		getGenericDAO().delete(session, entity);
	}
	
	public List<T> list(Filter filter, int start, int length){
		return (List<T>) genericDAO.list(filter, start, length);
	}
	
	public List<T> list(Filter filter){
		return (List<T>) genericDAO.list(filter);
	}
	
	public long count(Filter filter){
		return genericDAO.count(filter);
	}

	protected boolean addWhereField(boolean addAnd, StringBuffer sql, String whereField) {
		if (addAnd) {
			sql.append("and ");
		} else {
			sql.append("where ");
			addAnd = true;
		}
		sql.append(whereField);
		return addAnd;
	}

	protected boolean isValid(Integer num) {
		return num != null && num != 0;
	}
	
	protected boolean isValid(Long num) {
		return num != null && num != 0;
	}

	protected boolean isValid(String s) {
		return s != null && !s.isEmpty();
	}

}
