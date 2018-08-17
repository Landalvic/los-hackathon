package fr.insee.bidbo.service.rmes.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.insee.bidbo.dao.rmes.SliceKeyDao;
import fr.insee.bidbo.model.rmes.SliceKey;
import fr.insee.bidbo.rdfinsee.AbstractGenericService;
import fr.insee.bidbo.rdfinsee.GenericDao;
import fr.insee.bidbo.service.rmes.SliceKeyService;

@Service
public class SliceKeyServiceImpl extends AbstractGenericService<SliceKey> implements SliceKeyService {

    @Autowired
    private SliceKeyDao sliceKeyDao;

    @Override
    protected GenericDao<SliceKey> getDao() {
	return sliceKeyDao;
    }

    @Override
    public List<SliceKey> sliceKeyByDatacube(String iriDatacube) {
	return sliceKeyDao.sliceKeyByDatacube(iriDatacube);
    }
}
