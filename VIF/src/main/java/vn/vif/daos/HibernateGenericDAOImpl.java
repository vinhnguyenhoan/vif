package vn.vif.daos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Projections;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.impl.CriteriaImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import vn.vif.utils.FunctionException;
import vn.vif.utils.VIFException;

@SuppressWarnings({ "rawtypes" })
@Repository
public class HibernateGenericDAOImpl implements GenericDAO {
	
	@Autowired
	protected SessionFactory sessionFactory;
	
	public HibernateGenericDAOImpl() {
		//sessionFactory = new Configuration().configure().buildSessionFactory();
	}

	@Autowired
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Transactional
	public void add(Object entity) {
		Session session = null;
		try{
			SessionFactory sessionFactory = getSessionFactory();
			session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			add(session, entity);
			session.flush();
			tx.commit();
		} catch (Exception e) {
			new FunctionException(getClass(), e);
			if (e instanceof ConstraintViolationException) {
				throw new VIFException(CONSTRAINT_VIOLATION, e.getMessage(), e);
			}
			throw e;
		} finally {
			try {
				close(session);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Transactional
	public void add(Session session, Object entity) throws HibernateException {
		try {
			session.persist(entity);
		} catch (ConstraintViolationException e) {
			throw new VIFException(CONSTRAINT_VIOLATION, e.getMessage(), e);
		}
	}

	@Transactional
	public Object merge(Object entity) {
		Session session = null ;
		try {
			SessionFactory sessionFactory = getSessionFactory();
			session = sessionFactory.openSession();
			Object result = session.merge(entity);
			return result;
		} catch (Exception e) {
			new FunctionException(getClass(), e);
			if (e instanceof ConstraintViolationException) {
				throw new VIFException(CONSTRAINT_VIOLATION, e.getMessage(), e);
			}
			throw e;
		} finally {
			try {
				close(session);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Transactional
	public void update(Object entity) {
		Session session = null ;
		try {
			SessionFactory sessionFactory = getSessionFactory();
			session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();		
			update(session, entity);
			tx.commit();
		} catch (Exception e) {
			new FunctionException(getClass(), e);
			if (e instanceof ConstraintViolationException) {
				throw new VIFException(CONSTRAINT_VIOLATION, e.getMessage(), e);
			}
			throw e;
		} finally {
			try {
				close(session);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Transactional
	public void update(Session session, Object entity) throws HibernateException {
		session.update(entity);
	}

	@Transactional
	public void delete(Object entity) {
		Session session = null ;
		try {
			SessionFactory sessionFactory = getSessionFactory();
			session = sessionFactory.openSession();
			delete(session, entity);
		} catch (Exception e) {
			new FunctionException(getClass(), e);
			throw e;
		} finally {
			try {
				close(session);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Transactional
	public void delete(Session session, Object entity) throws HibernateException {
		session.delete(entity);
	}

	@Transactional
	public List<?> list(Class objectClass) {
		Session session = null ;
		try {
			SessionFactory sessionFactory = getSessionFactory();
			session = sessionFactory.openSession();
			String sql = "from " + objectClass.getSimpleName();
			Query query = session.createQuery(sql);
			List result = query.list();
			return result;
		} catch (Exception e) {
			new FunctionException(getClass(), e);
			throw e;
		} finally {
			try {
				close(session);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Transactional
	public Object find(Class objectClass, long id, LazyLoader loader) {
		
		SessionFactory sessionFactory = getSessionFactory();
		Session session = sessionFactory.openSession();
		Object result=null;
		try {
			result = session.get(objectClass, id);
			if (loader != null && result != null) {
				loader.loadData(result);
			}
		} catch (HibernateException e) {
			new FunctionException(getClass(), e);
		} finally{
			try {
				close(session);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	@Transactional
	public Object find(Class objectClass, long id) {
		
		return find(objectClass, id, null);
	}

	@Transactional
	public List<?> list(Class<?> objectClass, String[] fieldNames, Object[] fieldValues, boolean andCompare,
			Map<String, Boolean> sortFields, int start, int length) {
		return list(objectClass, fieldNames, fieldValues, andCompare, sortFields, start, length, null);
	}

	@Transactional
	public List<?> executeQuery(String sql, Object[] values, int start, int length) {
		Session session = null ;
		try {
			SessionFactory sessionFactory = getSessionFactory();
			session = sessionFactory.openSession();
			Query query = session.createQuery(sql);
			if (values != null) {
				int index = 1;
				for (Object value : values) {
					query.setParameter("value" + index, value);
					index++;
				}
			}
			if (start >= 0) {
				query.setFirstResult(start);
			}
			if (length >= 0) {
				query.setMaxResults(length);
			}
			List result = query.list();
			return result;
		} catch (Exception e) {
			new FunctionException(getClass(), e);
			throw e;
		} finally {
			try {
				close(session);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	@Transactional
	public List<?> list(Class<?> objectClass, String[] fieldNames, Object[] fieldValues, boolean andCompare,
			Map<String, Boolean> sortFields, int start, int length, String loadField) {
		Session session = null ;
		try {
			SessionFactory sessionFactory = getSessionFactory();
			session = sessionFactory.openSession();
			String sql = "from " + objectClass.getSimpleName() + " entt";
			if (loadField != null) {
				sql = "select " + loadField + " " + sql;
			}
			int index = 0;
			if (fieldNames != null) {
				List<String> list = Arrays.asList(fieldNames);
				fieldLoop: for (String fieldName : fieldNames) {
					if (fieldName.endsWith("SearchTag")) {
						index++;
						continue fieldLoop;
					}
					if (sql.contains(" where ")) {
						sql += andCompare ? " AND " : " OR ";
					} else {
						sql += " where ";
					}
					if (fieldValues[index] instanceof String) {
						if (list.contains(fieldName + "SearchTag")) {
							sql += "(lower(entt." + fieldName + ") like lower(:value" + index + ") or lower(entt."
									+ fieldName + "SearchTag) like lower(:value" + index + "))";
						} else {
							sql += "entt." + fieldName + " = :value" + index;
						}
					} else if (fieldValues[index] instanceof Object[]) {
						Object[] temp = (Object[]) fieldValues[index];
						if (temp[0] != null || temp[1] != null) {
							if (temp[0] != null && temp[1] != null) {
								sql += "entt." + fieldName + ">=:value" + index + "min AND entt." + fieldName + "<=:value"
										+ index + "max";
							} else if (temp[0] == null) {
								sql += "entt." + fieldName + "<=:value" + index + "max";
							} else {
								sql += "entt." + fieldName + ">=:value" + index + "min";
							}
						}
					} else {
						sql += "entt." + fieldName + "=:value" + index;
					}
					index++;
				}
			}
			if (sortFields != null) {
				index = 0;
				for (Entry<String, Boolean> sort : sortFields.entrySet()) {
					if (index > 0) {
						sql += ", ";
					} else {
						sql += " order by ";
					}
					sql += sort.getKey() + (sort.getValue() ? " ASC" : " DESC");
					index++;
				}
			}
			Query query = session.createQuery(sql);
			if (fieldNames != null) {
				for (int i = 0; i < fieldNames.length; i++) {
					Object value = fieldValues[i];
					if (value instanceof Object[]) {
						Object[] temp = (Object[]) value;
						if (temp[0] != null || temp[1] != null) {
							if (temp[0] != null) {
								query.setParameter("value" + i + "min", temp[0]);
							}
							if (temp[1] != null) {
								query.setParameter("value" + i + "max", temp[1]);
							}
						}
					} else {
						String fieldName = fieldNames[i];
						if (!fieldName.endsWith("SearchTag")) {
							query.setParameter("value" + i, value);
						}
					}
				}
			}
			if (start >= 0) {
				query.setFirstResult(start);
			}
			if (length >= 0) {
				query.setMaxResults(length);
			}
			List result = query.list();
			return result;
		} catch (Exception e) {
			new FunctionException(getClass(), e);
			throw e;
		} finally {
			try {
				close(session);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void close(Session session) {
		if (session!=null) {
			session.flush();
			session.close();
		}
	}

	@Transactional
	public void executeSQLQuery(String sql, Object[] values) {
		Session session = null ;
		try {
			SessionFactory sessionFactory = getSessionFactory();
			session = sessionFactory.openSession();
			SQLQuery query = session.createSQLQuery(sql);
			if (values != null) {
				for (int i = 0; i < values.length; i++) {
					query.setParameter("param" + i, values[i]);
				}
			}
			query.executeUpdate();
		} catch (Exception e) {
			new FunctionException(getClass(), e);
			throw e;
		} finally {
			try {
				close(session);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public List list(Filter filter, int start, int length) {
		Session session = null ;
		try {
			SessionFactory sessionFactory = getSessionFactory();
			session = sessionFactory.openSession();
			Criteria criteria=filter.getCriteria(session);
			if (criteria == null) {
				return new ArrayList(0);
			}
			if (start >= 0) {
				criteria.setFirstResult(start);
			}
			if (length >= 0) {
				criteria.setMaxResults(length);
			}
			List<Object> results;
			if (filter instanceof DistinctFilter) {
				if (length > 0) {
					criteria.setProjection(Projections.distinct(Projections.property(((DistinctFilter)filter).getId())));
					List ids = criteria.list();
					results = new ArrayList<Object>();
					for (Object id : ids) {
						results.add(session.get(
								Class.forName( ((CriteriaImpl)criteria).getEntityOrClassName() ), 
								(Long)id ));
					}
				} else {
					criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
					results = criteria.list();
				}
			} else {
				results = criteria.list();
			}
			return results;
		} catch (HibernateException e) {
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				close(session);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	
	public long count(Filter filter) {
		Session session = null;
		try {
			SessionFactory sessionFactory = getSessionFactory();
			session = sessionFactory.openSession();
			Criteria criteria=filter.getCriteria(session);
			if (criteria == null) {
				return 0;
			}
			if (criteria instanceof DistinctFilter) {
				criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			}
			criteria.setProjection(Projections.rowCount());
			long count=((Number) criteria.uniqueResult()).longValue();
			return count;
		} catch (Exception e) {
			new FunctionException(getClass(), e);
			return 0;
		} finally {
			try {
				close(session);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	public List<?> list(Filter filter) {
		Session session = null ;
		try {
			SessionFactory sessionFactory = getSessionFactory();
			session = sessionFactory.openSession();
			Criteria criteria=filter.getCriteria(session);
			if (criteria == null) {
				return new ArrayList(0);
			}
			if (criteria instanceof DistinctFilter) {
				criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			}
			List results=criteria.list();
			return results;
		} catch (Exception e) {
			new FunctionException(getClass(), e);
			return new ArrayList(0);
		} finally {
			try {
				close(session);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}