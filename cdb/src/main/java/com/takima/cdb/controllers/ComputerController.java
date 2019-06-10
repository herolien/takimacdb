package com.takima.cdb.controllers;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import static java.util.function.Predicate.*;

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
import org.springframework.http.HttpStatus;

import com.takima.cdb.entities.Computer;
import com.takima.cdb.exceptions.EntityNotFoundException;
import com.takima.cdb.exceptions.InvalidArgumentException;
import com.takima.cdb.services.ComputerService;
import com.takima.cdb.utils.Errors;
import com.takima.cdb.utils.Paths;


@Path(Paths.ROOT_PATH)
@Produces(MediaType.APPLICATION_JSON)
public class ComputerController extends GenericController {
	
	
	/** Computer service */
	@Autowired
	private ComputerService computerService;	
	
	
	/**
	 * Get a computer instance by id.
	 * @param id Id of the computer.
	 * @return (200) if a computer is returned, (404) if the id does not exist in database.
	 */
	@GET
	@Path(Paths.GET_COMPUTER)
	public Response getComputer(@PathParam(PARAMETER_ID) String id) {
		
		logger.debug("Call getComputer() webservice with parameter: id = "+id);
		Response response;
		try {	
			final Computer computer = computerService.getComputer(Long.parseLong(id));
			
			// HTTP 200: request OK.
			response = Response.status(HttpStatus.OK.value())
							   .entity(computer)
							   .build();

		// HTTP 404: entity not found (eg. non existing id...)
		} catch (EntityNotFoundException e1) {
			logger.error("getComputer", e1);
			response = e1.toResponse();
			
		}
		
		return response;
	}
	
	/**
	 * Get a list of Computers.
	 * @param offset The offset.
	 * @param limit Size of the future collection.
	 * @return (200) when a computer collection is returned, (400) when invalid parameter(s) is/are sent, (404) if no results is returned.
	 */
	@GET
	@Path(Paths.LIST_COMPUTER)
	public Response getComputers(@QueryParam("offset") String offset, @QueryParam("limit") String limit) {
		
		logger.debug("Call getComputers() webservice with parameters: offset = "+offset+", limit = "+limit);
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
			
			final Collection<Computer> computers = computerService.getComputers(offsetValue, limitValue);
			
			// HTTP 200: request OK.
			response = Response.status(HttpStatus.OK.value())
							   .entity(computers)
							   .build();
		
		// HTTP 400 (InvalidArgumentException) : bad request (eg. bad format, empty value...)
		// HTTP 404 (EntityNotFoundException)  : no results found in database
		} catch (InvalidArgumentException | EntityNotFoundException e1) {
			logError("getComputers", e1);
			response = e1.toResponse();
		}
		
		return response;
	}

	/**
	 * Add a computer.
	 * @param computer The computer to add.
	 * @return (200) OK with the new generated id, (400) if an invalid parameter is sent.
	 */
	@PUT
	@Path(Paths.ADD_COMPUTER)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addComputer(Computer computer) {
		
		logger.debug("Call addComputer() webservice with parameter: Computer = "+computer);
		Response response;
		
		try {
			computer = Optional.ofNullable(computer)
							   .filter(Computer::hasNullId)		// id must remain null in insertion
							   .filter(Computer::hasName)		// check if the name is correctly set
							   .filter(Computer::hasValidDates)	// check if the discontinued date > introduced date
							   .orElseThrow(() -> new InvalidArgumentException(Errors.INVALID_ENTITY));
			
			computerService.addComputer(computer);
			
			// HTTP 200: OK (a new id has been generated).
			response = Response.status(HttpStatus.OK.value())
							   .entity(Collections.singletonMap(PARAMETER_ID, computer.getId().toString()))
							   .build();
			
		// HTTP 400: bad request (eg. bad format, empty value...)
		} catch(InvalidArgumentException e1) {
			logError("addComputer", e1);
			response = e1.toResponse();
		}
		return response;
	}

	/**
	 * Update a computer.
	 * @param computer The computer to update.
	 * @return (204) No content if update is OK, (400) if Computer data is invalid.
	 */
	@POST
	@Path(Paths.UPDATE_COMPUTER)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateComputer(Computer computer) {
		
		logger.debug("Call updateComputer() webservice with parameter: Computer = "+computer);
		Response response;
		
		try {
			computer = Optional.ofNullable(computer)
							   .filter(not(Computer::hasNullId)) // check if id is set
					   		   .filter(Computer::hasName)		 // check if the name is correctly set
							   .filter(Computer::hasValidDates)	 // check if the discontinued date > introduced date
							   .orElseThrow(() -> new InvalidArgumentException(Errors.INVALID_ENTITY));
			
			computerService.updateComputer(computer);
			
			// HTTP 204: OK
			response = Response.status(HttpStatus.NO_CONTENT.value())
							   .build();
			
		// HTTP 400 (InvalidArgumentException) : bad request (eg. bad format, empty value...)
		// HTTP 404 (EntityNotFoundException)  : no results found in database
		} catch(InvalidArgumentException | EntityNotFoundException e1) {
			logError("updateComputer", e1);
			response = e1.toResponse();
		}
		return response;
	}

	/**
	 * Remove a computer.
	 * @param id Id of the computer to update.
	 * @return (204) No content if OK, (404) if the selected id does not exist in database.
	 */
	@DELETE
	@Path(Paths.DELETE_COMPUTER)
	public Response deleteComputer(@PathParam(PARAMETER_ID) String id) {
		
		logger.debug("Call deleteComputer() webservice with parameters: id = "+id);
		Response response;
		
		try {
			computerService.deleteComputer(Long.parseLong(id));
			
			// HTTP 204: No content.
			response = Response.status(HttpStatus.NO_CONTENT.value())
							   .build();
			
		// HTTP 404: no results found in database
		} catch (EntityNotFoundException e1) {
			logError("deleteComputer", e1);
			response = e1.toResponse();
		}
		return response;
	}
	
	/**
	 * Count computers.
	 * @return (200) if OK.
	 */
	@GET
	@Path(Paths.COUNT_COMPUTER)
	public Response countComputer() {
		
		logger.debug("Call countComputer() webservice");
		return Response.status(HttpStatus.OK.value())
					   .entity(Collections.singletonMap("size", computerService.countComputers()))
					   .build();
	}
	
}
