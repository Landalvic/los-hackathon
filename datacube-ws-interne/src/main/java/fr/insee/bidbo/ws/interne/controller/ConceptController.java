package fr.insee.bidbo.ws.interne.controller;

import java.util.ArrayList;
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
import org.eclipse.rdf4j.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import fr.insee.bidbo.dao.BaseRDF;
import fr.insee.bidbo.model.rmes.Concept;
import fr.insee.bidbo.model.rmes.ConceptMesure;
import fr.insee.bidbo.model.rmes.GenericConcept;
import fr.insee.bidbo.rdfinsee.vocabulary.DCTERMSStr;
import fr.insee.bidbo.rdfinsee.vocabulary.SKOSStr;
import fr.insee.bidbo.service.rmes.ConceptMesureService;
import fr.insee.bidbo.service.rmes.ConceptService;
import fr.insee.bidbo.service.rmes.DataCubeService;
import fr.insee.bidbo.vocabulary.Insee;
import fr.insee.bidbo.ws.utils.ControllerUtils;

@Path("/")
public class ConceptController {

    private static final Logger logger = LoggerFactory.getLogger(ConceptController.class);

    @Autowired
    private ConceptService conceptService;

    @Autowired
    private ConceptMesureService conceptMesureService;

    @Autowired
    private DataCubeService dataCubeService;

    @POST
    @Path("/concept/chargement/valider")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response validerConcept(ChargementConcept chargement) throws Exception {
	logger.info("/concept/chargement/valider a été appelé");
	if (chargement.isMesure()) {
	    ConceptMesure concept = new ConceptMesure();
	    remplirConcept(chargement, concept);
	    conceptMesureService.add(BaseRDF.RMES, concept);
	} else {
	    Concept concept = new Concept();
	    remplirConcept(chargement, concept);
	    conceptService.add(BaseRDF.RMES, concept);
	}
	return Response.status(200).build();
    }

    @POST
    @Path("/concept/chargement/telecharger")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.TEXT_PLAIN })
    public Response telechargerConcept(ChargementConcept chargement) throws Exception {
	logger.info("/concept/chargement/telecharger a été appelé");

	List<Statement> triplets = new ArrayList<>();
	if (chargement.isMesure()) {
	    ConceptMesure concept = new ConceptMesure();
	    remplirConcept(chargement, concept);
	    conceptMesureService.add(concept, triplets);
	} else {
	    Concept concept = new Concept();
	    remplirConcept(chargement, concept);
	    conceptService.add(concept, triplets);
	}

	return ControllerUtils.lancerTelechargementTurtle(triplets, chargement.getCode() + ".ttle");
    }

    private void remplirConcept(ChargementConcept chargement, GenericConcept concept) {
	if (chargement.isMesure()) {
	    concept.setIri(Insee.NAMESPACE_RMES_CONCEPT_MESURE + chargement.getCode());
	} else {
	    concept.setIri(Insee.NAMESPACE_RMES_CONCEPT + chargement.getCode());
	}
	concept.setCode(chargement.getCode());
	concept.setLibelleFr(chargement.getLibelleFr());
	concept.setLibelleEn(chargement.getLibelleEn());
    }

    @GET
    @Path("/concepts")
    @Produces({ MediaType.APPLICATION_JSON })
    public Response concepts() throws Exception {
	logger.info("/concepts a été appelé");
	GenericEntity<List<Concept>> liste = new GenericEntity<List<Concept>>(conceptService.findAll(BaseRDF.RMES)) {
	};
	return Response.status(200).entity(liste).build();
    }

    @GET
    @Path("/concept/{code}")
    @Produces({ MediaType.APPLICATION_JSON })
    public Response concept(@PathParam("code") String code) throws Exception {
	logger.info("/concept/" + code + " a été appelé");
	GenericConcept concept = conceptService.findByPredicat(BaseRDF.RMES, SKOSStr.NOTATION, code);
	if (concept == null) {
	    concept = conceptMesureService.findByPredicat(BaseRDF.RMES, DCTERMSStr.IDENTIFIER, code);
	    if (concept == null) {
		return Response.status(404).build();
	    }
	    concept.setDatacubes(dataCubeService.relatedToAConceptMesure(concept.getIri()));
	} else {
	    concept.setDatacubes(dataCubeService.relatedToAConcept(concept.getIri()));
	}
	return Response.status(200).entity(concept).build();
    }

    @GET
    @Path("/concept/{code}/telecharger")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_OCTET_STREAM })
    public Response telechargerConceptCode(@PathParam("code") String code) throws Exception {
	logger.info("/concept/" + code + "/telecharger a été appelé");

	List<Statement> triplets = new ArrayList<>();
	Concept concept = conceptService.findByPredicat(BaseRDF.RMES, SKOSStr.NOTATION, code);
	if (concept == null) {
	    ConceptMesure conceptMesure = conceptMesureService.findByPredicat(BaseRDF.RMES, DCTERMSStr.IDENTIFIER,
		    code);
	    conceptMesureService.add(conceptMesure, triplets);
	} else {
	    conceptService.add(concept, triplets);
	}

	return ControllerUtils.lancerTelechargementTurtle(triplets, code + ".ttle");
    }

    @DELETE
    @Path("/concept/{code}/suppression")
    public Response codeListeSuppression(@PathParam("code") String code) throws Exception {
	logger.info("/concept/" + code + "/suppression a été appelé");
	Concept concept = conceptService.findByPredicat(BaseRDF.RMES, SKOSStr.NOTATION, code);
	if (concept == null) {
	    ConceptMesure conceptMesure = conceptMesureService.findByPredicat(BaseRDF.RMES, DCTERMSStr.IDENTIFIER,
		    code);
	    if (conceptMesure == null) {
		return Response.status(404).build();
	    }
	    conceptMesure.setDatacubes(dataCubeService.relatedToAConceptMesure(conceptMesure.getIri()));
	    if (CollectionUtils.isEmpty(conceptMesure.getDatacubes())) {
		conceptMesureService.remove(BaseRDF.RMES, conceptMesure);
		return Response.status(204).build();
	    } else {
		return Response.status(403).build();
	    }
	} else {
	    concept.setDatacubes(dataCubeService.relatedToAConcept(concept.getIri()));
	    if (CollectionUtils.isEmpty(concept.getDatacubes())) {
		conceptService.remove(BaseRDF.RMES, concept);
		return Response.status(204).build();
	    } else {
		return Response.status(403).build();
	    }
	}
    }

    public static class ChargementConcept {

	private String code;
	private String libelleFr;
	private String libelleEn;
	private boolean mesure;

	public String getCode() {
	    return code;
	}

	public void setCode(String code) {
	    this.code = code;
	}

	public String getLibelleFr() {
	    return libelleFr;
	}

	public void setLibelleFr(String libelleFr) {
	    this.libelleFr = libelleFr;
	}

	public String getLibelleEn() {
	    return libelleEn;
	}

	public void setLibelleEn(String libelleEn) {
	    this.libelleEn = libelleEn;
	}

	public boolean isMesure() {
	    return mesure;
	}

	public void setMesure(boolean mesure) {
	    this.mesure = mesure;
	}

    }

}
