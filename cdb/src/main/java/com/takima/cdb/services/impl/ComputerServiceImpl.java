package com.takima.cdb.services.impl;

import java.util.Collection;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.takima.cdb.dao.Dao;
import com.takima.cdb.entities.Computer;
import com.takima.cdb.exceptions.EntityNotFoundException;
import com.takima.cdb.services.ComputerService;
import com.takima.cdb.utils.Errors;

@Service
public class ComputerServiceImpl implements ComputerService {
	
	/** Computer DAO */
	@Resource
	private Dao<Computer> computerDao;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Computer> getComputers(int offset, int limit) throws EntityNotFoundException {
		return Optional.ofNullable(computerDao.get(offset, limit, Computer.class))
					   .filter(array -> !array.isEmpty())
					   .orElseThrow(() -> new EntityNotFoundException(Errors.ENTITY_NOT_FOUND));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Computer getComputer(Long id) throws EntityNotFoundException {
		return Optional.ofNullable(computerDao.get(id, Computer.class))
					   .orElseThrow(() -> new EntityNotFoundException(Errors.ENTITY_NOT_FOUND));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addComputer(Computer computer) {
		computerDao.add(computer);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateComputer(Computer computer) throws EntityNotFoundException {
		Optional.ofNullable(computerDao.update(computer.getId(), Computer.class, computer))
				.filter(isUpdated -> isUpdated)
				.orElseThrow(() -> new EntityNotFoundException(Errors.ENTITY_NOT_FOUND));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteComputer(Long id) throws EntityNotFoundException {
		Optional.ofNullable(computerDao.delete(id, Computer.class))
				.filter(isDeleted -> isDeleted)
				.orElseThrow(() -> new EntityNotFoundException(Errors.ENTITY_NOT_FOUND));
	}
	
	/**
	 * {@inheritDoc} 
	 */
	@Override
	public Long countComputers() {
		return computerDao.count(Computer.class);
	}
	
	/**
	 * @param computerDao the computerDao to set
	 */
	public void setComputerDao(Dao<Computer> computerDao) {
		this.computerDao = computerDao;
	}
		
}
