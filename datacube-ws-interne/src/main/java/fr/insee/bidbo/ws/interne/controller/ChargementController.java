package fr.insee.bidbo.ws.interne.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.io.FileUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.insee.bidbo.chargement.ChargementDonneesAgregeesService;
import fr.insee.bidbo.chargement.model.ValidationChoixGestionnaire;
import fr.insee.bidbo.dao.BaseRDF;
import fr.insee.bidbo.model.Colonne;
import fr.insee.bidbo.service.ColonneService;

@Path("/chargement")
@Component
public class ChargementController {

	private static final Logger logger = LoggerFactory.getLogger(ChargementController.class);

	@Autowired
	private ChargementDonneesAgregeesService chargementDonneesAgregeesService;

	@Autowired
	private ColonneService colonneService;

	@GET
	@Path("/colonnes")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response colonnes() throws Exception {
		logger.info("/chargement/colonnes a été appelé");
		GenericEntity<List<Colonne>> liste = new GenericEntity<List<Colonne>>(colonneService.findAll(BaseRDF.INTERNE)) {
		};
		return Response.status(200).entity(liste).build();
	}

	@POST
	@Path("/fichier")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces({ MediaType.APPLICATION_JSON })
	public Response uploadFichier(@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetails) throws Exception {
		logger.info("/chargement/fichier a été appelé");
		Date date = new Date();
		File fichierCsv = FileUtils.getFile("C:\\Users\\Julien\\Desktop\\hackathonprog",
				date.getTime() + fileDetails.getFileName());
		FileUtils.copyInputStreamToFile(uploadedInputStream, fichierCsv);
		return Response.status(200).entity(chargementDonneesAgregeesService.infosFichier(fichierCsv)).build();
	}

	@POST
	@Path("/fichier/donnees-agregees/valider")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response validerFichierDonneesAgregees(ValidationChoixGestionnaire validation) throws Exception {
		logger.info("/chargement/fichier/donnees-agregees/valider a été appelé");
		chargementDonneesAgregeesService.enregistrerPreferences(validation);
		chargementDonneesAgregeesService.chargerFichier(validation);
		return Response.status(200).build();
	}

	@POST
	@Path("/fichier/donnees-agregees/telecharger-turtle")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ MediaType.TEXT_PLAIN })
	public Response telechargerTurtleFichierDonneesAgregees(ValidationChoixGestionnaire validation) throws Exception {
		logger.info("/chargement/fichier/donnees-agregees/valider a été appelé");
		chargementDonneesAgregeesService.enregistrerPreferences(validation);
		StreamingOutput fileStream = new StreamingOutput() {
			@Override
			public void write(OutputStream output) throws IOException, WebApplicationException {
				try {
					chargementDonneesAgregeesService.chargerFichier(validation, output);
				} catch (Exception e) {
					throw new WebApplicationException("File Not Found !");
				}
			}
		};
		return Response.status(Status.OK).entity(fileStream)
				.header("content-disposition", "attachment; filename = dataset.ttl").build();
	}

}
