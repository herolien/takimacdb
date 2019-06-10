package com.takima.cdb.services;

import java.util.Collection;

import com.takima.cdb.entities.Computer;
import com.takima.cdb.exceptions.EntityNotFoundException;

/**
 * The service to use for computer references
 */
public interface ComputerService {
	
	/**
	 * Get a Computer list.
	 * @param offset .
	 * @param limit .
	 * @return a Computer collection.
	 * @throws EntityNotFoundException when Dao returns no result.
	 */
	Collection<Computer> getComputers(int offset, int limit) throws EntityNotFoundException;

	/**
	 * Get Computer by id.
	 * @param id The id.
	 * @return Computer.
	 * @throws EntityNotFoundException when Dao returns no result.
	 */
	Computer getComputer(Long id) throws EntityNotFoundException;

	/**
	 * Add a new computer.
	 * @param computer The new computer.
	 */
	void addComputer(Computer computer);
	
	/**
	 * Update an existing computer.
	 * @param computer The new computer.
	 */
	void updateComputer(Computer computer);
	
	/**
	 * Delete an existing computer.
	 * @param id Id of the computer.
	 * @throws EntityNotFoundException when Dao returns no result.
	 */
	void deleteComputer(Long id) throws EntityNotFoundException;
	
	/**
	 * Count all computers.
	 * @return the number of returned results.
	 */
	Long countComputers();
}
