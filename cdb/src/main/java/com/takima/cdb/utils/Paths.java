package com.takima.cdb.utils;

/**
 * Path constants.
 */
public interface Paths {

	/** Root path */
	public static final String ROOT_PATH = "/";
	
	///// ComputerController
	
	/** Path - Add computer */
	public static final String ADD_COMPUTER = "/computer/add";
	
	/** Path - Get computer */
	public static final String GET_COMPUTER = "/computer/{id:[0-9]+}";

	/** Path - Get list of computers */
	public static final String LIST_COMPUTER = "/computer/list";

	/** Path - Update computer */
	public static final String UPDATE_COMPUTER = "/computer/update";

	/** Path - Delete computer */
	public static final String DELETE_COMPUTER = "/computer/{id:[0-9]+}";

	/** Path - Count computer */
	public static final String COUNT_COMPUTER = "/computer/size";
	
	///// CompanyController
	
	/** Path - Add Company */
	public static final String ADD_COMPANY = "/company/add";
	
	/** Path - Get Company */
	public static final String GET_COMPANY = "/company/{id:[0-9]+}";

	/** Path - Get list of Companies */
	public static final String LIST_COMPANY = "/company/list";

	/** Path - Update Company */
	public static final String UPDATE_COMPANY = "/company/update";

	/** Path - Delete Company */
	public static final String DELETE_COMPANY = "/company/{id:[0-9]+}";

	/** Path - Count Company */
	public static final String COUNT_COMPANY = "/company/size";
	
}
