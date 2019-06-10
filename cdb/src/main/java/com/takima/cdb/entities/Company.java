package com.takima.cdb.entities;

import java.io.Serializable;
import java.util.Optional;
import static java.util.function.Predicate.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Company entity
 */
@Entity
@Table(name = "company")
public class Company implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 5848088212214441224L;
	
	
	/** ID of the company */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	/** Name of the company */
	@Column(name = "name")
	private String name;

	
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
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "Company [id=" + id + ", name=" + name + "]";
	}

	/**
	 * Check if id is null.
	 * @return boolean.
	 */
	public boolean hasNullId() {
		return Optional.ofNullable(id).isEmpty();
	}
	
	/**
	 * Check if name is blank.
	 * @return boolean.
	 */
	public boolean hasName() {
		return !Optional.ofNullable(name)
					   .filter(not(String::isBlank))
					   .isEmpty();
	}
	
}
