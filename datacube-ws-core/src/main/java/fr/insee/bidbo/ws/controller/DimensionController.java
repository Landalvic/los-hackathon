package fr.insee.bidbo.ws.controller;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.insee.bidbo.dao.BaseRDF;
import fr.insee.bidbo.model.rmes.Concept;
import fr.insee.bidbo.model.rmes.ConceptMesure;
import fr.insee.bidbo.model.rmes.Modalite;
import fr.insee.bidbo.service.rmes.ConceptMesureService;
import fr.insee.bidbo.service.rmes.ConceptService;
import fr.insee.bidbo.service.rmes.ModaliteService;
import io.swagger.annotations.Api;

@Component
@Api
@Path("/")
public class DimensionController {

    private static final Logger logger = LoggerFactory.getLogger(DimensionController.class);

    @Autowired
    private ConceptMesureService conceptMesureService;

    @Autowired
    private ModaliteService modaliteService;

    @Autowired
    private ConceptService conceptService;

    @GET
    @Path("/conceptscheme")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Response conceptscheme() throws Exception {
	logger.info("/conceptscheme a été appelé");
	GenericEntity<List<Concept>> liste = new GenericEntity<List<Concept>>(conceptService.findAll(BaseRDF.RMES)) {
	};
	return Response.status(200).entity(liste).build();
    }

    @GET
    @Path("/codelist/CL_Measure")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Response measures() throws Exception {
	logger.info("/codelist/CL_Measure a été appelé");
	GenericEntity<List<ConceptMesure>> liste = new GenericEntity<List<ConceptMesure>>(
		conceptMesureService.findAll(BaseRDF.RMES)) {
	};
	return Response.status(200).entity(liste).build();
    }

    @GET
    @Path("/codelist/CL_{dimension}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Response modalitesDimension(@PathParam("dimension") String dimension) throws Exception {
	logger.info("/codelist/CL_" + dimension + " a été appelé");
	GenericEntity<List<Modalite>> liste = new GenericEntity<List<Modalite>>(
		modaliteService.modalitesByConceptScheme(dimension)) {
	};
	return Response.status(200).entity(liste).build();
    }

}
