package fr.insee.bidbo.service.rmes;

import fr.insee.bidbo.model.rmes.ConceptScheme;
import fr.insee.bidbo.rdfinsee.GenericService;

public interface ConceptSchemeService extends GenericService<ConceptScheme> {

    void supprimerEnCascade(ConceptScheme conceptScheme);

}
