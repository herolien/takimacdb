/**
 * 
 */
package com.takima.cdb.dao;

import java.util.Collection;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

/**
 * DAO implementation.
 */
@Transactional
@Repository
public class GenericDao<T> implements Dao<T>{

	/** Entity manager */
	@PersistenceContext	
	private EntityManager entityManager;
	
	/** 
	 * {@inheritDoc} 
	 */
	@Override
	public void add(T object) {
		entityManager.persist(object);
	}

	/** 
	 * {@inheritDoc} 
	 */
	@Override
	public void update(T object) {
		entityManager.merge(object);
	}

	/** 
	 * {@inheritDoc} 
	 */
	@Override
	public boolean delete(Long id, Class<T> type) {
		return Optional.ofNullable(entityManager.find(type, id))
					   .map(this::delete)
					   .orElse(false);
	}

	/** 
	 * {@inheritDoc} 
	 */
	@Override
	public T get(Long id, Class<T> type) {
		return entityManager.find(type, id); 
	}

	/** 
	 * {@inheritDoc} 
	 */
	@Override
	public Collection<T> get(int offset, int limit, Class<T> type) {
		return entityManager.createQuery("SELECT t FROM "+type.getSimpleName()+" t", type)
							.setFirstResult(offset)
							.setMaxResults(limit)
							.getResultList();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long count(Class<T> type) {
		return entityManager.createQuery("SELECT count(*) FROM "+type.getSimpleName()+" t", Long.class)
							.getSingleResult();
							
	}
	
	/**
	 * Execute the removal of the current entity and return a boolean.
	 * @param entity An entity.
	 * @return true if succeeded
	 */
	private Boolean delete(T entity){
		entityManager.remove(entity);
		return true;
	}
	
}
