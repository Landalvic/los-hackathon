package fr.insee.bidbo.exception;

public class MelodiException extends Exception {

    private static final long serialVersionUID = 2019793447742343332L;

    public MelodiException(String message) {
	super(message);
    }

    public MelodiException(String message, Throwable cause) {
	super(message, cause);
    }
}
