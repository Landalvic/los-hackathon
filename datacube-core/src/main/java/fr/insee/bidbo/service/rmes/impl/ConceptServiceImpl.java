package fr.insee.bidbo.service.rmes.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.insee.bidbo.dao.rmes.ConceptDao;
import fr.insee.bidbo.model.rmes.Concept;
import fr.insee.bidbo.rdfinsee.AbstractGenericService;
import fr.insee.bidbo.rdfinsee.GenericDao;
import fr.insee.bidbo.service.rmes.ConceptService;

@Service
public class ConceptServiceImpl extends AbstractGenericService<Concept> implements ConceptService {

    @Autowired
    private ConceptDao conceptDao;

    @Override
    protected GenericDao<Concept> getDao() {
	return conceptDao;
    }

}
