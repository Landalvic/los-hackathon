package fr.insee.bidbo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.insee.bidbo.dao.AttachableComplement;
import fr.insee.bidbo.dao.BaseRDF;
import fr.insee.bidbo.dao.SliceDao;
import fr.insee.bidbo.model.Slice;
import fr.insee.bidbo.rdfinsee.AbstractGenericService;
import fr.insee.bidbo.rdfinsee.GenericDao;
import fr.insee.bidbo.service.SliceService;

@Service
public class SliceServiceImpl extends AbstractGenericService<Slice> implements SliceService {

    @Autowired
    private SliceDao sliceDao;

    @Override
    protected GenericDao<Slice> getDao() {
	return sliceDao;
    }

    @Override
    public List<Slice> seriesFiltrees(BaseRDF base, AttachableComplement complement) {
	return sliceDao.seriesFiltrees(base, complement);
    }

    @Override
    public List<Slice> seriesFiltrees(BaseRDF base, AttachableComplement complement, int limit){
	return sliceDao.seriesFiltrees(base, complement, limit);
    }

}
