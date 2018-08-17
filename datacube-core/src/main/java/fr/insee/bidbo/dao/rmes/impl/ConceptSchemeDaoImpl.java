package fr.insee.bidbo.dao.rmes.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.rdf4j.model.Statement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.insee.bidbo.dao.BaseRDF;
import fr.insee.bidbo.dao.RDFConnection;
import fr.insee.bidbo.dao.rmes.ConceptSchemeDao;
import fr.insee.bidbo.model.rmes.ConceptScheme;
import fr.insee.bidbo.model.rmes.Modalite;
import fr.insee.bidbo.rdfinsee.AbstractGenericDao;
import fr.insee.bidbo.service.rmes.ModaliteService;

@Component
public class ConceptSchemeDaoImpl extends AbstractGenericDao<ConceptScheme> implements ConceptSchemeDao {

    @Autowired
    private ModaliteService modaliteService;

    @Autowired
    private RDFConnection rdfConnection;

    @Override
    protected Class<ConceptScheme> getClazz() {
	return ConceptScheme.class;
    }

    @Override
    public void supprimerEnCascade(ConceptScheme conceptScheme) {
	List<Statement> triplets = new ArrayList<>();
	remove(conceptScheme, triplets);
	for (Modalite modalite : conceptScheme.getModalites()) {
	    modaliteService.remove(modalite, triplets);
	}
	triplets.add(rdfConnection.removeStatement(conceptScheme.getIriClass(), null, null, null));
	triplets.add(rdfConnection.removeStatement(null, null, conceptScheme.getIriClass(), null));
	rdfConnection.enleverTriplets(BaseRDF.RMES, triplets);
    }

}
