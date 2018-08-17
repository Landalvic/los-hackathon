package fr.insee.bidbo.dao;

import java.util.List;

import fr.insee.bidbo.model.Observation;
import fr.insee.bidbo.rdfinsee.GenericDao;

public interface ObservationDao extends GenericDao<Observation> {

    List<Observation> observationsBySlice(BaseRDF base, String iriSlice, AttachableComplement complement);

    List<Observation> observationsByDataset(BaseRDF base, String iriDataset, AttachableComplement complement);

}
