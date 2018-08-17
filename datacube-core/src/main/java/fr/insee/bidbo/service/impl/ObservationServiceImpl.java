package fr.insee.bidbo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.insee.bidbo.dao.AttachableComplement;
import fr.insee.bidbo.dao.BaseRDF;
import fr.insee.bidbo.dao.ObservationDao;
import fr.insee.bidbo.model.Observation;
import fr.insee.bidbo.rdfinsee.AbstractGenericService;
import fr.insee.bidbo.rdfinsee.GenericDao;
import fr.insee.bidbo.service.ObservationService;

@Service
public class ObservationServiceImpl extends AbstractGenericService<Observation> implements ObservationService {

    @Autowired
    private ObservationDao observationDao;

    @Override
    protected GenericDao<Observation> getDao() {
	return observationDao;
    }

    @Override
    public List<Observation> observationsBySlice(BaseRDF base, String iriSlice, AttachableComplement complement) {
	return observationDao.observationsBySlice(base, iriSlice, complement);
    }

    @Override
    public List<Observation> observationsByDataset(BaseRDF base, String iriDataset, AttachableComplement complement) {
	return observationDao.observationsByDataset(base, iriDataset, complement);
    }

}
