package fr.insee.bidbo.dao.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import fr.insee.bidbo.dao.AbstractAttachableDao;
import fr.insee.bidbo.dao.AttachableComplement;
import fr.insee.bidbo.dao.BaseRDF;
import fr.insee.bidbo.dao.ObservationDao;
import fr.insee.bidbo.model.Observation;
import fr.insee.bidbo.rdfinsee.wrapper.RequestWrapper;
import fr.insee.bidbo.vocabulary.QB;
import fr.insee.bidbo.vocabulary.str.QBStr;

@Component
public class ObservationDaoImpl extends AbstractAttachableDao<Observation> implements ObservationDao {

    @Override
    protected Class<Observation> getClazz() {
	return Observation.class;
    }

    @Override
    protected String iriClass() {
	return QBStr.OBSERVATION_CLASS;
    }

    @Override
    public List<Observation> observationsBySlice(BaseRDF base, String iriSlice, AttachableComplement complement) {
	RequestWrapper request = new RequestWrapper();
	request.setComplement(complement);
	searchFields(request);
	request.getWhere().append(delimiter(iriSlice) + delimiter(QB.OBSERVATION) + " ?iri . ");
	return findAll(base, request);
    }

    @Override
    public List<Observation> observationsByDataset(BaseRDF base, String iriDataset, AttachableComplement complement) {
	RequestWrapper request = new RequestWrapper();
	request.setComplement(complement);
	searchFields(request, QBStr.DATA_SET, iriDataset);
	return filterByPredicat(base, QBStr.DATA_SET, iriDataset, request);
    }

}
