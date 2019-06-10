/**
 * 
 */
package com.takima.cdb.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Optional;
import static java.util.function.Predicate.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Computer entity
 */
@Entity
@Table(name = "computer")
public class Computer implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 3516774347166386175L;
	
	
	/** ID of the item */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	/** Name */
	@Column(name = "name")
	private String name;
	
	/** Date (UTC) of when the computer was introduced */
	@JsonFormat(pattern="yyyy-MM-dd")
	@Column(name = "introduced")
	private LocalDate introducedDate;
	
	/** Date (UTC) of when the computer was discontinued */
	@JsonFormat(pattern="yyyy-MM-dd")
	@Column(name = "discontinued")
	private LocalDate discontinuedDate;
	
	/** Manufacturer id */
	@Column(name = "company_id")
	private Long manufacturer;
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the introducedDate
	 */
	public LocalDate getIntroducedDate() {
		return introducedDate;
	}

	/**
	 * @param introducedDate the introducedDate to set
	 */
	public void setIntroducedDate(LocalDate introducedDate) {
		this.introducedDate = introducedDate;
	}

	/**
	 * @return the discontinuedDate
	 */
	public LocalDate getDiscontinuedDate() {
		return discontinuedDate;
	}

	/**
	 * @param discontinuedDate the discontinuedDate to set
	 */
	public void setDiscontinuedDate(LocalDate discontinuedDate) {
		this.discontinuedDate = discontinuedDate;
	}

	/**
	 * @return the manufacturer
	 */
	public Long getManufacturer() {
		return manufacturer;
	}
	
	/**
	 * @param manufacturer the manufacturer to set
	 */
	public void setManufacturer(Long manufacturer) {
		this.manufacturer = manufacturer;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "Computer [id=" + id + ", name=" + name + ", introducedDate=" + introducedDate + ", discontinuedDate="
				+ discontinuedDate + ", manufacturer=" + manufacturer + "]";
	}

	/**
	 * Check if id is null.
	 * @return boolean.
	 */
	public boolean hasNullId() {
		return Optional.ofNullable(id).isEmpty();
	}

	/**
	 * Check if the computer has a valid name.
	 * @param computer The computer.
	 * @return true if a name is set, false in other cases.
	 */
	public boolean hasName() {
		return !Optional.ofNullable(name)
					   .filter(not(String::isBlank))
					   .isEmpty();
	}
	
	/**
	 * Check if the discontinued date is greater than the introduced one.
	 * @param computer The computer.
	 * @return true if at least one of the dates is null or when the discontinuedDate is greater than the introducedDate, false in other cases.
	 */
	public boolean hasValidDates() {
		return Optional.ofNullable(discontinuedDate)
					   .filter(not(discontinuedDate -> Optional.ofNullable(introducedDate).isEmpty()))
					   .map(discontinuedDate -> discontinuedDate.compareTo(introducedDate) > 0)
					   .orElse(true);
	}
	
}
