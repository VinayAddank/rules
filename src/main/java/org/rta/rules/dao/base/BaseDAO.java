package org.rta.rules.dao.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.ReplicationMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseDAO<T> implements GenericDAO<T> {

	@Autowired
	private SessionFactory sessionFactory;
	private Class<T> persistObject;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	public BaseDAO(Class<T> persistentClass) {
		this.persistObject = persistentClass;
	}

	public Class<T> getPersistentClass() {
		return persistObject;
	}

	public void update(T entity) {

		getSession().update(entity);

	}

    public void delete(Object obj) {
        getSession().delete(obj);
	}

	@SuppressWarnings("unchecked")
	public List<T> getAll() {
		List<T> getList = new ArrayList<T>();
		try {
			Criteria criteria = getSession().createCriteria(getPersistentClass());
			getList = criteria.list();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return getList;
	}

	@SuppressWarnings("unchecked")
	public List<T> getAll(boolean isActive) {
		List<T> getList = new ArrayList<T>();
		try {
			Criteria criteria = getSession().createCriteria(getPersistentClass());
			criteria.add(Restrictions.eq("status", isActive));
			getList = criteria.list();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return getList;
	}

	public Serializable save(T entity) {
		return (Long) getSession().save(entity);

	}

	public void saveOrUpdate(T entity) {

		getSession().saveOrUpdate(entity);

	}

	@Override
	@SuppressWarnings("unchecked")
	public T getEntity(Class<T> clazz, Long id) {
		return (T) getSession().get(clazz, id);
	}

    @SuppressWarnings("unchecked")
    @Override
    public T getEntity(Class<T> clazz, Integer id) {
        return (T) getSession().get(clazz, id);
    }
    
    public void replicate(T entity) {
        getSession().replicate(entity, ReplicationMode.IGNORE);
    }
}
