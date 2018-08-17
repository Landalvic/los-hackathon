package fr.insee.bidbo.ws.enumeration;

public enum Details {

    FULL,
    DATA_ONLY,
    SERIES_KEY_ONLY,
    NO_DATA;

    public static Details fromString(String references) {
	switch (references.toLowerCase()) {
	default:
	case "full":
	    return FULL;
	case "dataonly":
	case "data_only":
	    return DATA_ONLY;
	case "serieskeyonly":
	case "series_key_only":
	    return SERIES_KEY_ONLY;
	case "nodata":
	case "no_data":
	    return NO_DATA;
	}
    }

}
