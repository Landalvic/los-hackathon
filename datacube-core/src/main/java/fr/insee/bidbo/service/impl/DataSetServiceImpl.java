package fr.insee.bidbo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.insee.bidbo.dao.AttachableComplement;
import fr.insee.bidbo.dao.BaseRDF;
import fr.insee.bidbo.dao.DataSetDao;
import fr.insee.bidbo.model.DataSet;
import fr.insee.bidbo.rdfinsee.AbstractGenericService;
import fr.insee.bidbo.rdfinsee.GenericDao;
import fr.insee.bidbo.service.DataSetService;

@Service
public class DataSetServiceImpl extends AbstractGenericService<DataSet> implements DataSetService {

    @Autowired
    private DataSetDao dataSetDao;

    @Override
    protected GenericDao<DataSet> getDao() {
	return dataSetDao;
    }

    @Override
    public List<DataSet> datasetFiltrees(BaseRDF base, AttachableComplement complement) {
	return dataSetDao.datasetFiltrees(base, complement);
    }

    @Override
    public List<DataSet> datasetFiltrees(BaseRDF base, AttachableComplement complement, int limit) {
	return dataSetDao.datasetFiltrees(base, complement, limit);
    }

}
