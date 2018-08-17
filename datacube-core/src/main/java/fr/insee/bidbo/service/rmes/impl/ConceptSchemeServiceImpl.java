package fr.insee.bidbo.service.rmes.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.insee.bidbo.dao.rmes.ConceptSchemeDao;
import fr.insee.bidbo.model.rmes.ConceptScheme;
import fr.insee.bidbo.rdfinsee.AbstractGenericService;
import fr.insee.bidbo.rdfinsee.GenericDao;
import fr.insee.bidbo.service.rmes.ConceptSchemeService;

@Service
public class ConceptSchemeServiceImpl extends AbstractGenericService<ConceptScheme> implements ConceptSchemeService {

    @Autowired
    private ConceptSchemeDao conceptSchemeDao;

    @Override
    protected GenericDao<ConceptScheme> getDao() {
	return conceptSchemeDao;
    }

    @Override
    public void supprimerEnCascade(ConceptScheme conceptScheme) {
	conceptSchemeDao.supprimerEnCascade(conceptScheme);
    }
}
