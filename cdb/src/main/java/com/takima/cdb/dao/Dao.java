package com.takima.cdb.dao;

import java.util.Collection;

/**
 * DAO Interface.
 *
 * @param <T> Entity's type mapped with database.
 */
public interface Dao<T> {
	
	/**
	 * Get an entity by its id.
	 * @param id Requested id.
	 * @param type Entity's type.
	 * @return the entity (null if no results found).
	 */
	T get(Long id, Class<T> type);
	
	/**
	 * Get a list of entities.
	 * @param offset The offset.
	 * @param limit Size of the future list.
	 * @param type Entity's type.
	 * @return A collection of the selected entity (null if no results found).
	 */
	Collection<T> get(int offset, int limit, Class<T> type);
	
	/**
	 * Add an entity. If the process succeed, then a new id is added in the entity instance.
	 * @param object The entity to persist.
	 */
	void add(T object);
	
	/**
	 * Update an entity.
	 * @param object The entity to update.
	 */
	void update(T object);
	
	/**
	 * Delete an entity by its id.
	 * @param id Id of the entity.
	 * @param type Type of the entity.
	 * @return true if the process succeed, false if the selected id does not exist.
	 */
	boolean delete(Long id, Class<T> type);
	
	/**
	 * Count all entities in database.
	 * @param type Type of the entity.
	 * @return size.
	 */
	Long count(Class<T> type);
}
