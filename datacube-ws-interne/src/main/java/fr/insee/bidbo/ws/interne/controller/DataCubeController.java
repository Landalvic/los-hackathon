package fr.insee.bidbo.ws.interne.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
import fr.insee.bidbo.model.CodeVariableModalite;
import fr.insee.bidbo.model.DataSet;
import fr.insee.bidbo.model.Observation;
import fr.insee.bidbo.model.Slice;
import fr.insee.bidbo.model.rmes.ConceptMesure;
import fr.insee.bidbo.model.rmes.DataCube;
import fr.insee.bidbo.service.DataSetService;
import fr.insee.bidbo.service.ObservationService;
import fr.insee.bidbo.service.SliceService;
import fr.insee.bidbo.service.rmes.ConceptMesureService;
import fr.insee.bidbo.service.rmes.DataCubeService;
import fr.insee.bidbo.ws.interne.model.IriWrapper;
import fr.insee.bidbo.ws.utils.ControllerUtils;

@Path("/")
@Component
public class DataCubeController {

    private static final Logger logger = LoggerFactory.getLogger(DataCubeController.class);

    @Autowired
    private DataCubeService dataCubeService;

    @Autowired
    private SliceService sliceService;

    @Autowired
    private ObservationService observationService;

    @Autowired
    private DataSetService dataSetService;

    @Autowired
    private ConceptMesureService conceptMesureService;

    @GET
    @Path("/datacubes")
    @Produces({ MediaType.APPLICATION_JSON })
    public Response datacubes() throws Exception {
	logger.info("/datacubes a été appelé");
	GenericEntity<List<DataCube>> liste = new GenericEntity<List<DataCube>>(dataCubeService.findAll(BaseRDF.RMES)) {
	};
	return Response.status(200).entity(liste).build();
    }

    @POST
    @Path("/datacube/components")
    @Produces({ MediaType.APPLICATION_JSON })
    public Response datacube(IriWrapper datacube) throws Exception {
	logger.info("/datacube/attributs a été appelé");
	return Response.status(200).entity(dataCubeService.trouverDatacubeAvecModalites(datacube.getIri())).build();
    }

    @GET
    @Path("/datacube/{datacube}/series/valeurs")
    @Produces({ MediaType.APPLICATION_JSON })
    public Response valeurs(@PathParam("datacube") String code, @QueryParam("timePeriodRef") String timePeriodRef,
	    @Context UriInfo info) throws Exception {
	logger.info("/datacube/" + code + "/series/valeurs a été appelé");

	Map<String, List<String>> modalites = ControllerUtils.infoToMap(info);
	Parametres parametres = new Parametres();
	parametres.setModalites(modalites);

	DataCube datacube = dataCubeService.trouverDatacubeParCodeAvecModalites(code);
	AttachableComplement complement = new AttachableComplement(datacube, modalites, timePeriodRef);
	if (datacube.getIriSliceKey() != null) {
	    datacube.setAttributs(null);
	    List<Slice> slices = sliceService.seriesFiltrees(BaseRDF.INTERNE, complement, 5);
	    slices.stream().parallel().forEach(slice -> slice
		    .setObservations(observationService.observationsBySlice(BaseRDF.INTERNE, slice.getIri(), null)));
	    GenericEntity<List<Slice>> liste = new GenericEntity<List<Slice>>(slices) {
	    };
	    return Response.status(Status.OK).entity(liste).build();
	} else {
	    List<DataSet> datasets = dataSetService.datasetFiltrees(BaseRDF.INTERNE, complement);
	    int nbr = 0;
	    List<Slice> slices = new ArrayList<>();
	    dataset: for (DataSet dataset : datasets) {
		List<Observation> observations = observationService.observationsByDataset(BaseRDF.INTERNE,
			dataset.getIri(), complement);
		for (Observation observation : observations) {
		    Slice fakeSlice = new Slice();
		    fakeSlice.setDimensions(observation.getDimensions());
		    fakeSlice.setObservations(Arrays.asList(observation));
		    ConceptMesure c = conceptMesureService.findByIri(BaseRDF.RMES, observation.getIriMeasureType());
		    StringBuilder idBuilder = new StringBuilder(c.getCode());
		    for (CodeVariableModalite modalite : observation.getDimensions()) {
			idBuilder.append("-" + modalite.getCodeModalite());
		    }
		    idBuilder.append("-" + observation.getTimePeriod());
		    fakeSlice.setCode(idBuilder.toString());
		    slices.add(fakeSlice);
		    nbr++;
		    if (nbr == 5) {
			break dataset;
		    }
		}
	    }

	    GenericEntity<List<Slice>> liste = new GenericEntity<List<Slice>>(slices) {
	    };
	    return Response.status(Status.OK).entity(liste).build();
	}
    }

}
