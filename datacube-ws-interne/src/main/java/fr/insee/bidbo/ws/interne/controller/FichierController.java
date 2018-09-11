package fr.insee.bidbo.ws.interne.controller;

import java.io.File;
import java.io.InputStream;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.FileUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.stereotype.Component;

import fr.insee.bidbo.chargement.model.InfosFichier;

@Path("/")
@Component
public class FichierController {

	@POST
	@Path("/fichier/temp/chargement")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces({ MediaType.APPLICATION_JSON })
	public Response uploadFichier(@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetails) throws Exception {
		Date date = new Date();
		File fichierCsv = FileUtils.getFile("C:\\Users\\Julien\\Desktop\\hackathonprog",
				date.getTime() + fileDetails.getFileName());
		FileUtils.copyInputStreamToFile(uploadedInputStream, fichierCsv);

		InfosFichier infos = new InfosFichier();
		infos.setLienFichier(fichierCsv.getAbsolutePath());
		return Response.status(200).entity(infos).build();
	}

}
