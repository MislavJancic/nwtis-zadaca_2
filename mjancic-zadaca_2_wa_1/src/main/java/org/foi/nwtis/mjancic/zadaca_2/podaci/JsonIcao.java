package org.foi.nwtis.mjancic.zadaca_2.podaci;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Klasa JsonIcao, služi za JSON za icao.
 */
@XmlRootElement
public class JsonIcao {
	
	/** The icao. */
	@XmlElement public String icao;

}
