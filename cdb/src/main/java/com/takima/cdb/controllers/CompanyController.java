package com.takima.cdb.controllers;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.takima.cdb.entities.Company;
import com.takima.cdb.exceptions.EntityNotFoundException;
import com.takima.cdb.exceptions.GenericException;
import com.takima.cdb.exceptions.InvalidArgumentException;
import com.takima.cdb.services.CompanyService;
import com.takima.cdb.utils.Errors;
import com.takima.cdb.utils.Paths;

@Component
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class CompanyController extends GenericController {

	
	/** Company service */
	@Autowired
	private CompanyService companyService;
	
	
	/**
	 * Get a company instance by id.
	 * @param id Id of the computer.
	 * @return (200) if a computer is returned, (404) if the id does not exist in database.
	 */
	@GET
	@Path(Paths.GET_COMPANY)
	public Response getCompany(@PathParam(PARAMETER_ID) String id) {
		
		logger.debug("Call getCompany() webservice with parameter: id = "+id);
		Response response;
		try {	
			final Company company = companyService.getCompany(Long.parseLong(id));
			
			// HTTP 200: request OK.
			response = Response.status(HttpStatus.OK.value())
							   .entity(company)
							   .build();

		// HTTP 404: entity not found (eg. non existant id...)
		} catch (EntityNotFoundException e1) {
			logError("getCompany", e1);
			response = e1.toResponse();
		}
		
		return response;
	}
	
	/**
	 * Get a list of Companies.
	 * @param offset The offset.
	 * @param limit Size of the future collection.
	 * @return (200) when a company collection is returned, (400) when invalid parameter(s) is/are sent, (404) if no results is returned.
	 */
	@GET
	@Path(Paths.LIST_COMPANY)
	public Response getCompanies(@QueryParam("offset") String offset, @QueryParam("limit") String limit) {
		
		logger.debug("Call getCompanies() webservice with parameters: offset = "+offset+", limit = "+limit);
		Response response;
				
		try {
			final Integer offsetValue = Optional.ofNullable(offset)
									   .filter(this::isNumeric)
									   .map(Integer::parseInt)
									   .orElseThrow(() -> new InvalidArgumentException(Errors.INVALID_OFFSET));
			
			final Integer limitValue = Optional.ofNullable(limit)
									  .filter(this::isNumeric)
									  .map(Integer::parseInt)
									  .orElseThrow(() -> new InvalidArgumentException(Errors.INVALID_LIMIT));
			
			final Collection<Company> companies = companyService.getCompanies(offsetValue, limitValue);
			
			// HTTP 200: request OK.
			response = Response.status(HttpStatus.OK.value())
							   .entity(companies)
							   .build();
		
		// HTTP 400 (InvalidArgumentException) : bad request (eg. bad format, empty value...)
		// HTTP 404 (EntityNotFoundException)  : no results found in database
		} catch (InvalidArgumentException | EntityNotFoundException e1) {
			logError("getCompanies", e1);
			response = e1.toResponse();
		}
		
		return response;
	}

	/**
	 * Add a company.
	 * @param company The company to add.
	 * @return 200 OK - the response includes the new id of the company.
	 */
	@PUT
	@Path(Paths.ADD_COMPANY)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addCompany(Company company) {
		
		logger.debug("Call addCompany() webservice with parameters: Company = "+company);
		Response response;
		
		try {
			company = Optional.ofNullable(company)
							   .filter(Company::hasNullId)	// id must remain null in insertion
							   .filter(Company::hasName)	// check if the name is not empty
							   .orElseThrow(() -> new InvalidArgumentException(Errors.INVALID_ENTITY));
			
			companyService.addCompany(company);
			
			// HTTP 200: OK (a new id has been generated).
			response = Response.status(HttpStatus.OK.value())
							   .entity(Collections.singletonMap(PARAMETER_ID, company.getId().toString()))
							   .build();
			
		// HTTP 400: bad request (eg. bad format, empty value...)
		} catch(InvalidArgumentException e1) {
			logError("addCompany", e1);
			response = e1.toResponse();
		}
		return response;
	}

	/**
	 * Update a company.
	 * @param company The company to update.
	 * @return 204 No content.
	 */
	@POST
	@Path(Paths.UPDATE_COMPANY)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateCompany(Company company) {
		
		logger.debug("Call updateCompany() webservice with parameters: Company = "+company);
		Response response;
		
		try {
			company = Optional.ofNullable(company)
							   .filter(Company::hasName)
							   .orElseThrow(() -> new InvalidArgumentException(Errors.INVALID_ENTITY));
			
			companyService.updateCompany(company);
			
			// HTTP 204: OK (a new id has been generated).
			response = Response.status(HttpStatus.NO_CONTENT.value())
							   .build();
			
		// HTTP 400: bad request (eg. bad format, empty value...)
		} catch(InvalidArgumentException e1) {
			logError("updateCompany", e1);
			response = e1.toResponse();
		}
		return response;
	}

	/**
	 * Remove a company.
	 * @param id Id of the company to update.
	 * @return 204 No content.
	 */
	@DELETE
	@Path(Paths.DELETE_COMPANY)
	public Response deleteCompany(@PathParam(PARAMETER_ID) String id) {
		
		logger.debug("Call deleteCompany() webservice with parameters: id = "+id);
		Response response;
		
		try {
			companyService.deleteCompany(Long.parseLong(id));
			
			// HTTP 204: No content.
			response = Response.status(HttpStatus.NO_CONTENT.value())
							   .build();
		
		// HTTP 400: constraint violation
		} catch (DataIntegrityViolationException e1) {
			logError("deleteCompany", e1);
			response = GenericException.toResponse(Errors.DATA_INTEGRITY);
		
		// HTTP 404: no results found in database
		} catch (EntityNotFoundException e2) {
			logError("deleteCompany", e2);
			response = e2.toResponse();
		}
		return response;
	}

	/**
	 * Count computers.
	 * @return (200) if OK.
	 */
	@GET
	@Path(Paths.COUNT_COMPANY)
	public Response countCompany() {
		
		logger.debug("Call countCompany() webservice");
		return Response.status(HttpStatus.OK.value())
					   .entity(Collections.singletonMap("size", companyService.countCompanies()))
					   .build();
	}
	
}
