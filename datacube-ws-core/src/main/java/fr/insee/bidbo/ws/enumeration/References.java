package fr.insee.bidbo.ws.enumeration;

public enum References {

    CHILDREN;

    public static References fromString(String references) {
	switch (references.toLowerCase()) {
	case "children":
	    return CHILDREN;
	default:
	    return null;
	}
    }

}
