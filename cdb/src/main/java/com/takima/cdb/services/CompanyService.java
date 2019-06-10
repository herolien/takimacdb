package com.takima.cdb.services;

import java.util.Collection;

import org.springframework.dao.DataIntegrityViolationException;

import com.takima.cdb.entities.Company;
import com.takima.cdb.exceptions.EntityNotFoundException;

/**
 * The service to use for Company references
 */
public interface CompanyService {
	
	/**
	 * Get a Company list.
	 * @param offset .
	 * @param limit .
	 * @return a Company collection.
	 * @throws EntityNotFoundException when Dao returns no result.
	 */
	Collection<Company> getCompanies(int offset, int limit) throws EntityNotFoundException;

	/**
	 * Get Company by id.
	 * @param id The id.
	 * @return company.
	 * @throws EntityNotFoundException when Dao returns no result.
	 */
	Company getCompany(Long id) throws EntityNotFoundException;

	/**
	 * Add a new Company.
	 * @param company The new Company.
	 */
	void addCompany(Company company);
	
	/**
	 * Update an existing Company.
	 * @param company The Company to update.
	 */
	void updateCompany(Company company);
	
	/**
	 * Delete an existing Company.
	 * @param id Id of the Company.
	 * @throws EntityNotFoundException when Dao returns no result.
	 * @throws DataIntegrityViolationException when a constraint violation occurs.
	 */
	void deleteCompany(Long id) throws EntityNotFoundException, DataIntegrityViolationException;
	
	/**
	 * Count all companies.
	 * @return the number of returned results.
	 */
	Long countCompanies();
}
