package org.foi.nwtis.mjancic.zadaca_2.podaci;

import java.sql.Timestamp;

public class Problem {
	public String ident;
	public String description;
	public Timestamp stored;

	public Problem(String ident, String description, Timestamp stored) {
		super();
		this.ident = ident;
		this.description = description;
		this.stored = stored;
	}
	public Problem(String ident, String description) {
		super();
		this.ident = ident;
		this.description = description;
	}

}
