package fr.insee.bidbo.ws.interne.controller;

import java.io.File;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.eclipse.rdf4j.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import fr.insee.bidbo.chargement.ChargementCodeListeService;
import fr.insee.bidbo.chargement.model.ValidationChoixGestionnaire;
import fr.insee.bidbo.dao.BaseRDF;
import fr.insee.bidbo.model.rmes.ConceptScheme;
import fr.insee.bidbo.rdfinsee.vocabulary.SKOSStr;
import fr.insee.bidbo.service.rmes.ConceptSchemeService;
import fr.insee.bidbo.service.rmes.DataCubeService;
import fr.insee.bidbo.service.rmes.ModaliteService;
import fr.insee.bidbo.utils.CsvUtils;
import fr.insee.bidbo.ws.utils.ControllerUtils;

@Path("/")
public class CodeListeController {

    private static final Logger logger = LoggerFactory.getLogger(CodeListeController.class);

    @Autowired
    private ChargementCodeListeService chargementCodeListeService;

    @Autowired
    private ConceptSchemeService conceptSchemeService;

    @Autowired
    private ModaliteService modaliteService;

    @Autowired
    private DataCubeService dataCubeService;

    @POST
    @Path("/code-liste/chargement/valider")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response validerCodeListe(ValidationChoixGestionnaire validation) throws Exception {
	logger.info("/code-liste/chargement/valider a été appelé");
	File fichier = FileUtils.getFile(validation.getLienFichier());
	List<String[]> lignes = CsvUtils.readAll(fichier, '#');
	chargementCodeListeService.chargerCodeListe(lignes, false);
	return Response.status(200).build();
    }

    @POST
    @Path("/code-liste/chargement/telecharger")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.TEXT_PLAIN })
    public Response telechargerCodeListe(ValidationChoixGestionnaire validation) throws Exception {
	logger.info("/code-liste/chargement/telecharger a été appelé");
	File fichier = FileUtils.getFile(validation.getLienFichier());
	List<String[]> lignes = CsvUtils.readAll(fichier, '#');
	List<Statement> triplets = chargementCodeListeService.chargerCodeListe(lignes, true);
	return ControllerUtils.lancerTelechargementTurtle(triplets, lignes.get(0)[2] + ".ttle");
    }

    @GET
    @Path("/code-liste/concept-scheme")
    @Produces({ MediaType.APPLICATION_JSON })
    public Response codeListeConceptScheme() throws Exception {
	logger.info("/code-liste/concept-scheme a été appelé");
	GenericEntity<List<ConceptScheme>> liste = new GenericEntity<List<ConceptScheme>>(
		conceptSchemeService.findAll(BaseRDF.RMES)) {
	};
	return Response.status(200).entity(liste).build();
    }

    @DELETE
    @Path("/code-liste/{code}/suppression")
    public Response codeListeSuppression(@PathParam("code") String code) throws Exception {
	logger.info("/code-liste/" + code + "/suppression a été appelé");
	ConceptScheme cs = conceptSchemeService.findByPredicat(BaseRDF.RMES, SKOSStr.NOTATION, code);
	cs.setModalites(modaliteService.modalitesByConceptScheme(code));
	cs.setDatacubes(dataCubeService.relatedToAConceptScheme(cs.getIri()));
	if (CollectionUtils.isEmpty(cs.getDatacubes())) {
	    conceptSchemeService.supprimerEnCascade(cs);
	    return Response.status(200).build();
	} else {
	    return Response.status(403).build();
	}
    }

    @GET
    @Path("/code-liste/{code}")
    @Produces({ MediaType.APPLICATION_JSON })
    public Response codeListe(@PathParam("code") String code) throws Exception {
	logger.info("/code-liste/" + code + " a été appelé");
	ConceptScheme cs = conceptSchemeService.findByPredicat(BaseRDF.RMES, SKOSStr.NOTATION, code);
	cs.setModalites(modaliteService.modalitesByConceptScheme(code));
	cs.setDatacubes(dataCubeService.relatedToAConceptScheme(cs.getIri()));
	return Response.status(200).entity(cs).build();
    }

    @GET
    @Path("/code-liste/{code}/telecharger")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.TEXT_PLAIN })
    public Response telechargerCodeListeCode(@PathParam("code") String code) throws Exception {
	logger.info("/code-liste/chargement/telecharger a été appelé");
	List<Statement> triplets = chargementCodeListeService.telecharger(code);
	return ControllerUtils.lancerTelechargementTurtle(triplets, code + ".ttle");
    }

}
