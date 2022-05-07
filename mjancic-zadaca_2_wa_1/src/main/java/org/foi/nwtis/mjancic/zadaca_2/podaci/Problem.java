package org.foi.nwtis.mjancic.zadaca_2.podaci;

import java.sql.Timestamp;

// TODO: Auto-generated Javadoc
/**
 * Klasa Problem.
 */
public class Problem {
	
	/** Identifikacija ICAO. */
	public String ident;
	
	/** Opis problema. */
	public String description;
	
	/** Timestamp kad je zapisano u bazu podataka. */
	public Timestamp stored;

	/**
	 * Instancira novi problem.
	 *
	 * @param ident identifikacija
	 * @param description opis
	 * @param stored timestamp zapisa
	 */
	public Problem(String ident, String description, Timestamp stored) {
		super();
		this.ident = ident;
		this.description = description;
		this.stored = stored;
	}
	
	/**
	 * Instancira novi problem.
	 *
	  *@param ident identifikacija
	 * @param description opis
	 */
	public Problem(String ident, String description) {
		super();
		this.ident = ident;
		this.description = description;
	}

}
