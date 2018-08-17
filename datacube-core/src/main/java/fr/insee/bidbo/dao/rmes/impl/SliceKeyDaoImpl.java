package fr.insee.bidbo.dao.rmes.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import fr.insee.bidbo.dao.BaseRDF;
import fr.insee.bidbo.dao.rmes.SliceKeyDao;
import fr.insee.bidbo.model.rmes.SliceKey;
import fr.insee.bidbo.rdfinsee.AbstractGenericDao;
import fr.insee.bidbo.rdfinsee.wrapper.RequestWrapper;
import fr.insee.bidbo.vocabulary.QB;

@Component
public class SliceKeyDaoImpl extends AbstractGenericDao<SliceKey> implements SliceKeyDao {

    @Override
    protected Class<SliceKey> getClazz() {
	return SliceKey.class;
    }

    @Override
    public List<SliceKey> sliceKeyByDatacube(String iriDatacube) {
	RequestWrapper request = searchFields();
	request.getWhere().append(delimiter(iriDatacube) + delimiter(QB.SLICE_KEY) + " ?iri . ");
	return findAll(BaseRDF.RMES, request);
    }

}
