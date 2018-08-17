package fr.insee.bidbo.ws.interne.controller;

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

import org.eclipse.rdf4j.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import fr.insee.bidbo.chargement.ChargementDSDService;
import fr.insee.bidbo.dao.BaseRDF;
import fr.insee.bidbo.model.rmes.DataCube;
import fr.insee.bidbo.service.rmes.DataCubeService;
import fr.insee.bidbo.ws.utils.ControllerUtils;

@Path("/")
public class DSDController {

    private static final Logger logger = LoggerFactory.getLogger(DSDController.class);

    @Autowired
    private ChargementDSDService chargementDSDService;

    @Autowired
    private DataCubeService dataCubeService;

    @POST
    @Path("/dsd/chargement/valider")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON })
    public Response validerDSD(DataCube datacube) throws Exception {
	logger.info("/dsd/chargement/valider a été appelé");
	DataCube dsd = dataCubeService.trouverDatacubeParCodeSansModalites(datacube.getCode());
	dataCubeService.supprimerEnCascade(dsd);
	chargementDSDService.chargerDSD(datacube, false);
	return Response.status(200).build();
    }

    @POST
    @Path("/dsd/chargement/telecharger")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.TEXT_PLAIN })
    public Response telechargerDSD(DataCube datacube) throws Exception {
	logger.info("/dsd/chargement/telecharger a été appelé");
	List<Statement> triplets = chargementDSDService.chargerDSD(datacube, true);
	return ControllerUtils.lancerTelechargementTurtle(triplets, ".ttle");
    }

    @GET
    @Path("/dsd")
    @Produces({ MediaType.APPLICATION_JSON })
    public Response listeDSD() throws Exception {
	logger.info("/dsd a été appelé");
	GenericEntity<List<DataCube>> liste = new GenericEntity<List<DataCube>>(dataCubeService.findAll(BaseRDF.RMES)) {
	};
	return Response.status(200).entity(liste).build();
    }

    @GET
    @Path("/dsd/{code}")
    @Produces({ MediaType.APPLICATION_JSON })
    public Response dsd(@PathParam("code") String code) throws Exception {
	logger.info("/dsd/" + code + " a été appelé");
	DataCube dsd = dataCubeService.trouverDatacubeParCodeSansModalites(code);
	return Response.status(200).entity(dsd).build();
    }

    @DELETE
    @Path("/dsd/{code}/suppression")
    public Response codeListeSuppression(@PathParam("code") String code) throws Exception {
	logger.info("/code-liste/" + code + "/suppression a été appelé");
	DataCube dsd = dataCubeService.trouverDatacubeParCodeSansModalites(code);
	dataCubeService.supprimerEnCascade(dsd);
	return Response.status(200).build();
    }

}
