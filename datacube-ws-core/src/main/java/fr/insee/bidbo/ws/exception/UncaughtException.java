package fr.insee.bidbo.ws.exception;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class UncaughtException extends Throwable implements ExceptionMapper<Throwable> {

    private static final Logger logger = LoggerFactory.getLogger(UncaughtException.class);

    private static final long serialVersionUID = 1L;

    @Override
    public Response toResponse(Throwable exception) {
	if (exception instanceof NotFoundException) {
	    logger.info("not found : " + exception.getMessage());
	    return Response.status(Status.INTERNAL_SERVER_ERROR).entity(exception).type("text/plain").build();
	} else {
	    logger.error(exception.getMessage(), exception);
	    return Response
		    .status(Status.INTERNAL_SERVER_ERROR)
		    .entity("Something bad happened. Please try again !")
		    .type("text/plain")
		    .build();
	}
    }
}
