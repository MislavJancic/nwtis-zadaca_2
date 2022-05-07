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
	public String getIdent() {
		return ident;
	}
	public void setIdent(String ident) {
		this.ident = ident;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Timestamp getStored() {
		return stored;
	}
	public void setStored(Timestamp stored) {
		this.stored = stored;
	}
	

}
