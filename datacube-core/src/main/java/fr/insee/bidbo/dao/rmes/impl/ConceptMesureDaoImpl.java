package fr.insee.bidbo.dao.rmes.impl;

import org.springframework.stereotype.Component;

import fr.insee.bidbo.dao.rmes.ConceptMesureDao;
import fr.insee.bidbo.model.rmes.ConceptMesure;
import fr.insee.bidbo.rdfinsee.AbstractGenericDao;

@Component
public class ConceptMesureDaoImpl extends AbstractGenericDao<ConceptMesure> implements ConceptMesureDao {

    @Override
    protected Class<ConceptMesure> getClazz() {
	return ConceptMesure.class;
    }

}
