package org.foi.nwtis.mjancic.zadaca_2.podaci;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class JsonIcao {
	@XmlElement public String icao;

}
