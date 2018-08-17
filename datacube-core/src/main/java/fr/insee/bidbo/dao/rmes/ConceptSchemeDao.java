package fr.insee.bidbo.dao.rmes;

import fr.insee.bidbo.model.rmes.ConceptScheme;
import fr.insee.bidbo.rdfinsee.GenericDao;

public interface ConceptSchemeDao extends GenericDao<ConceptScheme> {

    void supprimerEnCascade(ConceptScheme conceptScheme);

}
