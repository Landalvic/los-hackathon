package fr.insee.bidbo.service;

import java.util.List;

import fr.insee.bidbo.dao.AttachableComplement;
import fr.insee.bidbo.dao.BaseRDF;
import fr.insee.bidbo.model.Observation;
import fr.insee.bidbo.rdfinsee.GenericService;

public interface ObservationService extends GenericService<Observation> {

    List<Observation> observationsBySlice(BaseRDF base, String iriSlice, AttachableComplement complement);

    List<Observation> observationsByDataset(BaseRDF base, String iriDataset, AttachableComplement complement);

}
