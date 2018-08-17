package fr.insee.bidbo.ws.interne.controller;

import java.io.File;
import java.io.InputStream;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.FileUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import fr.insee.bidbo.chargement.model.InfosFichier;
import fr.insee.bidbo.service.DossiersService;
import fr.insee.bidbo.ws.utils.ControllerUtils;

@Path("/")
public class FichierController {

    private static final Logger logger = LoggerFactory.getLogger(FichierController.class);

    @Autowired
    private DossiersService dossiersService;

    @POST
    @Path("/fichier/temp/chargement")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces({ MediaType.APPLICATION_JSON })
    public Response uploadFichier(@FormDataParam("file") InputStream uploadedInputStream,
	    @FormDataParam("file") FormDataContentDisposition fileDetails) throws Exception {
	logger.info("/fichier/temp/chargement a été appelé");
	Date date = new Date();
	File fichierCsv = FileUtils.getFile(dossiersService.dossierBatchTemporaire(),
		date.getTime() + fileDetails.getFileName());
	FileUtils.copyInputStreamToFile(uploadedInputStream, fichierCsv);

	InfosFichier infos = new InfosFichier();
	infos.setLienFichier(fichierCsv.getAbsolutePath());
	return Response.status(200).entity(infos).build();
    }

    @GET
    @Path("/fichier/temp/telecharger/{fichier}")
    @Produces({ MediaType.APPLICATION_OCTET_STREAM })
    public Response telechargerTurtle(@PathParam("fichier") String fichier) throws Exception {
	logger.info("/fichier/temp/telecharger/" + fichier + " a été appelé");
	File file = dossiersService.fichierBatchTemporaire(fichier);
	return ControllerUtils.lancerTelechargement(file);
    }

}
