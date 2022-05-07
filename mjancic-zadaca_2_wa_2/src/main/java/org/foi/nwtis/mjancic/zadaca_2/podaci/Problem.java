package org.foi.nwtis.mjancic.zadaca_2.podaci;

import java.sql.Timestamp;

/**
 * The Class Problem.
 */
public class Problem {
	
	/** The ident. */
	public String ident;
	
	/** The description. */
	public String description;
	
	/** The stored. */
	public Timestamp stored;

	/**
	 * Instantiates a new problem.
	 *
	 * @param ident  ident
	 * @param description  description
	 * @param stored  stored
	 */
	public Problem(String ident, String description, Timestamp stored) {
		super();
		this.ident = ident;
		this.description = description;
		this.stored = stored;
	}
	
	/**
	 * Instantiates a new problem.
	 *
	 * @param ident  ident
	 * @param description  description
	 */
	public Problem(String ident, String description) {
		super();
		this.ident = ident;
		this.description = description;
	}
	
	/**
	 * Dohvaća  ident.
	 *
	 * @return  ident
	 */
	public String getIdent() {
		return ident;
	}
	
	/**
	 * Postavlja  ident.
	 *
	 * @param ident  new ident
	 */
	public void setIdent(String ident) {
		this.ident = ident;
	}
	
	/**
	 * dohvca description.
	 *
	 * @return  description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Postavlja  description.
	 *
	 * @param description  new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Dohvaća  stored.
	 *
	 * @return  stored
	 */
	public Timestamp Dohvaćatored() {
		return stored;
	}
	
	/**
	 * Postavlja stored.
	 *
	 * @param stored  novi stored
	 */
	public void Postavljatored(Timestamp stored) {
		this.stored = stored;
	}
	

}
