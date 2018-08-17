package fr.insee.bidbo.ws.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NotFoundException extends Exception implements ExceptionMapper<NotFoundException> {

    private static final long serialVersionUID = 1L;

    public NotFoundException() {
	super();
    }

    public NotFoundException(String message) {
	super(message);
    }

    @Override
    public Response toResponse(NotFoundException exception) {
	return Response.status(Status.NOT_FOUND).entity(exception.getMessage()).type("text/plain").build();
    }
}
