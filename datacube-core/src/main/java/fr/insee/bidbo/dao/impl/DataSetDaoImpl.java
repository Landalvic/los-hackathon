package fr.insee.bidbo.dao.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import fr.insee.bidbo.dao.AbstractAttachableDao;
import fr.insee.bidbo.dao.AttachableComplement;
import fr.insee.bidbo.dao.BaseRDF;
import fr.insee.bidbo.dao.DataSetDao;
import fr.insee.bidbo.model.DataSet;
import fr.insee.bidbo.rdfinsee.wrapper.RequestWrapper;
import fr.insee.bidbo.vocabulary.str.QBStr;

@Component
public class DataSetDaoImpl extends AbstractAttachableDao<DataSet> implements DataSetDao {

    @Override
    protected Class<DataSet> getClazz() {
	return DataSet.class;
    }

    @Override
    protected String iriClass() {
	return QBStr.DATA_SET_CLASS;
    }

    @Override
    public List<DataSet> datasetFiltrees(BaseRDF base, AttachableComplement complement) {
	RequestWrapper request = new RequestWrapper();
	request.setComplement(complement);
	searchFields(request, QBStr.STRUCTURE, complement.getDatacube().getIri());
	return filterByPredicat(base, QBStr.STRUCTURE, complement.getDatacube().getIri(), request);
    }

    @Override
    public List<DataSet> datasetFiltrees(BaseRDF base, AttachableComplement complement, int limit) {
	RequestWrapper request = new RequestWrapper();
	request.setComplement(complement);
	searchFields(request, QBStr.SLICE_STRUCTURE, complement.getDatacube().getIriSliceKey());
	request.getEnd().append("LIMIT " + limit + " ");
	return filterByPredicat(base, QBStr.SLICE_STRUCTURE, complement.getDatacube().getIriSliceKey(), request);
    }

}
