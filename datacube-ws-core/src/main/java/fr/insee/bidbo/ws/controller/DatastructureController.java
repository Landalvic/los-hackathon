package fr.insee.bidbo.ws.controller;

import java.util.List;
import java.util.Map;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.insee.bidbo.dao.AttachableComplement;
import fr.insee.bidbo.dao.BaseRDF;
import fr.insee.bidbo.dao.Parametres;
import fr.insee.bidbo.model.DataSet;
import fr.insee.bidbo.model.Slice;
import fr.insee.bidbo.model.rmes.DataCube;
import fr.insee.bidbo.service.DataSetService;
import fr.insee.bidbo.service.ObservationService;
import fr.insee.bidbo.service.SliceService;
import fr.insee.bidbo.service.rmes.DataCubeService;
import fr.insee.bidbo.vocabulary.str.QBStr;
import fr.insee.bidbo.ws.enumeration.Details;
import fr.insee.bidbo.ws.enumeration.References;
import fr.insee.bidbo.ws.exception.NotFoundException;
import fr.insee.bidbo.ws.utils.ControllerUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;

@Path("/")
@Api
@Component
public class DatastructureController {

    private static final Logger logger = LoggerFactory.getLogger(DatastructureController.class);

    @Autowired
    private DataCubeService dataCubeService;

    @Autowired
    private DataSetService dataSetService;

    @Autowired
    private SliceService sliceService;

    @Autowired
    private ObservationService observationService;

    @GET
    @Path("/datastructure")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Response datastructure() throws Exception {
	logger.info("/datastructure a été appelé");
	GenericEntity<List<DataCube>> liste = new GenericEntity<List<DataCube>>(dataCubeService.findAll(BaseRDF.RMES)) {
	};
	return Response.status(Status.OK).entity(liste).build();
    }

    @GET
    @Path("/datastructure/{codeDatacude}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Response datacube(@ApiParam(value = "code of the cube") @PathParam("codeDatacude") String codeDatacude,
	    @QueryParam("references") References references) throws Exception {
	logger.info("/datastructure/" + codeDatacude + " a été appelé");
	DataCube datacube = dataCubeService.trouverDatacubeParCodeAvecModalites(codeDatacude);
	if (datacube == null) {
	    throw new NotFoundException("Cube " + codeDatacude + " was not found.");
	} else if (references == References.CHILDREN) {
	    datacube.setConceptsMesures(dataCubeService.trouverConceptsMesures(datacube.getIri()));
	}
	return Response.status(Status.OK).entity(datacube).build();
    }

    @GET
    @Path("/data/{codeDatacude}/datasets")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Response datasets(@PathParam("codeDatacude") String codeDatacude, @Context UriInfo info,
	    @QueryParam("timePeriodRef") String timePeriodRef) throws Exception {
	logger.info("/data/" + codeDatacude + "/datasets a été appelé");

	Map<String, List<String>> modalites = ControllerUtils.infoToMap(info);
	Parametres parametres = new Parametres();
	parametres.setModalites(modalites);

	DataCube datacube = dataCubeService.trouverDatacubeParCodeAvecModalites(codeDatacude);
	AttachableComplement complement = new AttachableComplement(datacube, modalites, timePeriodRef);

	List<DataSet> datasets = dataSetService.datasetFiltrees(BaseRDF.INTERNE, complement);
	datasets.stream().parallel().forEach(dataset -> dataset.setObservations(
		observationService.observationsByDataset(BaseRDF.INTERNE, dataset.getIri(), complement)));
	GenericEntity<List<DataSet>> liste = new GenericEntity<List<DataSet>>(datasets) {
	};
	return Response.status(Status.OK).entity(liste).build();
    }

    @GET
    @Path("/data/{codeDatacude}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Response datasets(@PathParam("codeDatacude") String codeDatacude,
	    @QueryParam("startPeriod") String startPeriod, @QueryParam("endPeriod") String endPeriod,
	    @QueryParam("firstNObservations") Integer firstNObservations,
	    @QueryParam("lastNObservations") Integer lastNObservations,
	    @DefaultValue("full") @QueryParam("details") Details details,
	    @QueryParam("timePeriodRef") String timePeriodRef, @Context UriInfo info) throws Exception {
	logger.info("/data/" + codeDatacude + " a été appelé");

	Map<String, List<String>> modalites = ControllerUtils.infoToMap(info);
	Parametres parametres = new Parametres();
	parametres.setModalites(modalites);

	DataCube datacube = dataCubeService.trouverDatacubeParCodeAvecModalites(codeDatacude);
	AttachableComplement complement = new AttachableComplement(datacube, modalites, timePeriodRef);
	if (datacube.getIriSliceKey() != null) {
	    if (details == Details.DATA_ONLY || details == Details.SERIES_KEY_ONLY) {
		datacube.setAttributs(null);
	    }
	    List<Slice> slices = sliceService.seriesFiltrees(BaseRDF.INTERNE, complement);
	    if (details == Details.FULL) {
		slices.stream().parallel().forEach(slice -> slice.setObservations(
			observationService.observationsBySlice(BaseRDF.INTERNE, slice.getIri(), complement)));
	    } else if (details == Details.DATA_ONLY) {
		slices.stream().parallel().forEach(slice -> slice.setObservations(
			observationService.observationsBySlice(BaseRDF.INTERNE, slice.getIri(), null)));
	    }
	    GenericEntity<List<Slice>> liste = new GenericEntity<List<Slice>>(slices) {
	    };
	    return Response.status(Status.OK).entity(liste).build();
	} else {
	    if (details == Details.NO_DATA) {
		GenericEntity<List<DataSet>> liste = new GenericEntity<List<DataSet>>(
			dataSetService.filterByPredicat(BaseRDF.INTERNE, QBStr.STRUCTURE, datacube.getIri())) {
		};
		return Response.status(Status.OK).entity(liste).build();
	    } else {

	    }
	}
	return Response.status(Status.OK).entity(datacube).build();
    }

}
