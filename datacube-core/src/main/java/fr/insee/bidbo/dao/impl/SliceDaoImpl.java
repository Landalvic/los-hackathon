package fr.insee.bidbo.dao.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import fr.insee.bidbo.dao.AbstractAttachableDao;
import fr.insee.bidbo.dao.AttachableComplement;
import fr.insee.bidbo.dao.BaseRDF;
import fr.insee.bidbo.dao.SliceDao;
import fr.insee.bidbo.model.Slice;
import fr.insee.bidbo.rdfinsee.wrapper.RequestWrapper;
import fr.insee.bidbo.vocabulary.str.QBStr;

@Component
public class SliceDaoImpl extends AbstractAttachableDao<Slice> implements SliceDao {

    @Override
    protected Class<Slice> getClazz() {
	return Slice.class;
    }

    @Override
    protected String iriClass() {
	return QBStr.SLICE_CLASS;
    }

    @Override
    public List<Slice> seriesFiltrees(BaseRDF base, AttachableComplement complement) {
	RequestWrapper request = new RequestWrapper();
	request.setComplement(complement);
	searchFields(request, QBStr.SLICE_STRUCTURE, complement.getDatacube().getIriSliceKey());
	return filterByPredicat(base, QBStr.SLICE_STRUCTURE, complement.getDatacube().getIriSliceKey(), request);
    }

    @Override
    public List<Slice> seriesFiltrees(BaseRDF base, AttachableComplement complement, int limit) {
	RequestWrapper request = new RequestWrapper();
	request.setComplement(complement);
	searchFields(request, QBStr.SLICE_STRUCTURE, complement.getDatacube().getIriSliceKey());
	request.getEnd().append("LIMIT " + limit + " ");
	return filterByPredicat(base, QBStr.SLICE_STRUCTURE, complement.getDatacube().getIriSliceKey(), request);
    }

}
