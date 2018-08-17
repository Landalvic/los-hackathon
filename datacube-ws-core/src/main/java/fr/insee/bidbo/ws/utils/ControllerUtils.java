package fr.insee.bidbo.ws.utils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriInfo;

import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;

public abstract class ControllerUtils {

    public static Response lancerTelechargement(File fichier) {
	StreamingOutput fileStream = new StreamingOutput() {
	    @Override
	    public void write(OutputStream output) throws IOException, WebApplicationException {
		try {
		    Path path = fichier.toPath();
		    byte[] data = Files.readAllBytes(path);
		    output.write(data);
		    output.flush();
		} catch (Exception e) {
		    throw new WebApplicationException("File Not Found !");
		}
	    }
	};
	return Response
		.status(Status.OK)
		.entity(fileStream)
		.header("content-disposition", "attachment; filename = " + fichier.getName())
		.build();
    }

    public static Response lancerTelechargementTurtle(List<Statement> triplets, String fileName) {
	StreamingOutput fileStream = new StreamingOutput() {
	    @Override
	    public void write(OutputStream output) throws IOException, WebApplicationException {
		try {
		    Rio.write(triplets, output, RDFFormat.TURTLE);
		} catch (Exception e) {
		    throw new WebApplicationException("File Not Found !");
		}
	    }
	};
	return Response
		.status(Status.OK)
		.entity(fileStream)
		.header("content-disposition", "attachment; filename = " + fileName)
		.build();
    }

    public static Map<String, List<String>> infoToMap(UriInfo info) {
	Map<String, List<String>> map = new HashMap<>();
	for (Entry<String, List<String>> entry : info.getQueryParameters().entrySet()) {
	    map.put(entry.getKey(), entry.getValue());
	}
	return map;
    }
}
