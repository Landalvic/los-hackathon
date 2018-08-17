package fr.insee.bidbo.dao.rmes.impl;

import org.springframework.stereotype.Component;

import fr.insee.bidbo.dao.rmes.ConceptDao;
import fr.insee.bidbo.model.rmes.Concept;
import fr.insee.bidbo.rdfinsee.AbstractGenericDao;

@Component
public class ConceptDaoImpl extends AbstractGenericDao<Concept> implements ConceptDao {

    @Override
    protected Class<Concept> getClazz() {
	return Concept.class;
    }

}
