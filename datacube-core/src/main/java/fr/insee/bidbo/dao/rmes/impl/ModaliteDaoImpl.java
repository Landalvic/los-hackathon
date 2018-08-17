package fr.insee.bidbo.dao.rmes.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.rdf4j.model.vocabulary.SKOS;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.insee.bidbo.dao.BaseRDF;
import fr.insee.bidbo.dao.Parametres;
import fr.insee.bidbo.dao.RDFConnection;
import fr.insee.bidbo.dao.rmes.ModaliteDao;
import fr.insee.bidbo.model.rmes.Modalite;
import fr.insee.bidbo.rdfinsee.AbstractGenericDao;
import fr.insee.bidbo.rdfinsee.wrapper.RequestWrapper;

@Component
public class ModaliteDaoImpl extends AbstractGenericDao<Modalite> implements ModaliteDao {

    private static final Logger logger = LoggerFactory.getLogger(ModaliteDaoImpl.class);

    @Autowired
    private RDFConnection rdfConnection;

    @Override
    protected Class<Modalite> getClazz() {
	return Modalite.class;
    }

    @Override
    public List<Modalite> modalitesByConceptScheme(String code) {
	RequestWrapper request = searchFields();
	request.getWhere().append("?iriConceptScheme " + delimiter(SKOS.NOTATION) + " '" + code + "' . ");
	return findAll(BaseRDF.RMES, request);
    }

    @Override
    public List<Modalite> modalites(Parametres parametres) {
	if (parametres.getModalites().size() == 0) {
	    return new ArrayList<>(0);
	}
	try (RepositoryConnection connection = rdfConnection.getConnection(BaseRDF.INTERNE)) {
	    StringBuilder requete = new StringBuilder("SELECT DISTINCT ?modalite ");
	    requete.append("WHERE { ");
	    // A mettre dans les union une fois dans Rmes TODO
	    requete.append("?variable <" + SKOS.NOTATION + "> ?codeVariable . ");
	    requete.append("{ ");
	    requete.append("?modalite <" + SKOS.NOTATION + "> ?codeModalite . ");
	    requete.append("?modalite <" + SKOS.IN_SCHEME + "> ?variable . ");
	    requete.append("} ");
	    requete.append("UNION ");
	    requete.append("{ ");
	    requete.append("SERVICE <" + BaseRDF.RMES.getUrlServer() + "> { ");
	    requete.append("?modalite <" + SKOS.NOTATION + "> ?codeModalite . ");
	    requete.append("?modalite <" + SKOS.IN_SCHEME + "> ?variable . ");
	    requete.append("} ");
	    requete.append("} ");
	    requete.append("FILTER (");
	    List<String> listeS = new ArrayList<>(parametres.getModalites().size());
	    for (Entry<String, List<String>> entry : parametres.getModalites().entrySet()) {
		listeS.add("(?codeModalite in ('" + String.join("','", entry.getValue()) + "') && ?codeVariable='"
			+ entry.getKey() + "')");
	    }
	    requete.append(String.join(" || ", listeS));
	    requete.append(") ");
	    requete.append("} ");
	    TupleQuery tupleQuery = connection.prepareTupleQuery(requete.toString());

	    try (TupleQueryResult result = tupleQuery.evaluate()) {
		List<Modalite> modalites = new ArrayList<Modalite>();
		while (result.hasNext()) {
		    BindingSet bindingSet = result.next();
		    Modalite modalite = new Modalite();
		    modalite.setIri(bindingSet.getValue("modalite").stringValue());
		    modalites.add(modalite);
		}
		return modalites;
	    } catch (Exception e) {
		logger.error("La requête est erronée", e);
		return null;
	    }
	} catch (Exception e) {
	    logger.error("connexion base RDF inaccessible", e);
	    return null;
	}
    }

}
