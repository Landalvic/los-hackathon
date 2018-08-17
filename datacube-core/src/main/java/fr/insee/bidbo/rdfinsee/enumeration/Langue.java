package fr.insee.bidbo.rdfinsee.enumeration;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

@XmlEnum(String.class)
public enum Langue {

    @XmlEnumValue("null")
    NULL("null", "Null"),
    @XmlEnumValue("fr")
    FR("fr", "Français"),
    @XmlEnumValue("en")
    EN("en", "Anglais");

    private String id, libelle;

    private Langue(String id, String libelle) {
	this.id = id;
	this.libelle = libelle;
    }

    public String getId() {
	return id;
    }

    public String getLibelle() {
	return libelle;
    }

    public static Langue getLangue(String id) {
	switch (id.toLowerCase()) {
	case "fr":
	    return FR;
	case "en":
	    return EN;
	default:
	    return NULL;
	}
    }
}
