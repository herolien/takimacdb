package com.takima.cdb.services.impl;

import java.util.Collection;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.takima.cdb.dao.Dao;
import com.takima.cdb.entities.Company;
import com.takima.cdb.exceptions.EntityNotFoundException;
import com.takima.cdb.services.CompanyService;
import com.takima.cdb.utils.Errors;

@Service
public class CompanyServiceImpl implements CompanyService {

	@Resource
	private Dao<Company> companyDao;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Company> getCompanies(int offset, int limit) throws EntityNotFoundException {
		return Optional.ofNullable(companyDao.get(offset, limit, Company.class))
					   .filter(array -> !array.isEmpty())
					   .orElseThrow(() -> new EntityNotFoundException(Errors.ENTITY_NOT_FOUND));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Company getCompany(Long id) throws EntityNotFoundException {
		return Optional.ofNullable(companyDao.get(id, Company.class))
					   .orElseThrow(() -> new EntityNotFoundException(Errors.ENTITY_NOT_FOUND));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addCompany(Company company) {
		companyDao.add(company);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateCompany(Company company) throws EntityNotFoundException {
		Optional.ofNullable(companyDao.update(company.getId(), Company.class, company))
				.filter(isUpdated -> isUpdated)
				.orElseThrow(() -> new EntityNotFoundException(Errors.ENTITY_NOT_FOUND));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteCompany(Long id) throws EntityNotFoundException, DataIntegrityViolationException {
		Optional.ofNullable(companyDao.delete(id, Company.class))
				.filter(isDeleted -> isDeleted)
				.orElseThrow(() -> new EntityNotFoundException(Errors.ENTITY_NOT_FOUND));
	}
	
	/**
	 * {@inheritDoc} 
	 */
	@Override
	public Long countCompanies() {
		return companyDao.count(Company.class);
	}
	
	/**
	 * @param CompanyDao the CompanyDao to set
	 */
	public void setCompanyDao(Dao<Company> companyDao) {
		this.companyDao = companyDao;
	}
	
}
