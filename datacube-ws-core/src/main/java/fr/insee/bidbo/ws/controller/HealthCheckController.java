package fr.insee.bidbo.ws.controller;

import java.net.Socket;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public class HealthCheckController {

    @GET
    @Path("/healthcheck")
    @Produces(MediaType.TEXT_PLAIN)
    public Response healthcheck() {
	try (Socket socket = new Socket("10.243.8.202", 7200)) {
	    System.out.println(socket.isConnected());
	    return Response.status(200).entity("Base : OK").build();
	} catch (Exception e) {
	    e.printStackTrace();
	    return Response.status(200).entity("Base : KO").build();
	}
    }

}
