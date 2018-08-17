package fr.insee.bidbo.service.rmes.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.insee.bidbo.dao.rmes.ConceptMesureDao;
import fr.insee.bidbo.model.rmes.ConceptMesure;
import fr.insee.bidbo.rdfinsee.AbstractGenericService;
import fr.insee.bidbo.rdfinsee.GenericDao;
import fr.insee.bidbo.service.rmes.ConceptMesureService;

@Service
public class ConceptMesureServiceImpl extends AbstractGenericService<ConceptMesure> implements ConceptMesureService {

    @Autowired
    private ConceptMesureDao conceptMesureDao;

    @Override
    protected GenericDao<ConceptMesure> getDao() {
	return conceptMesureDao;
    }

}
