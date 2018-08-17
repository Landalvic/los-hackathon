package fr.insee.bidbo.dao.impl;

import org.eclipse.rdf4j.model.vocabulary.RDF;
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
import fr.insee.bidbo.dao.ColonneDao;
import fr.insee.bidbo.dao.RDFConnection;
import fr.insee.bidbo.model.Colonne;
import fr.insee.bidbo.rdfinsee.AbstractGenericDao;
import fr.insee.bidbo.vocabulary.Type;

@Component
public class ColonneDaoImpl extends AbstractGenericDao<Colonne> implements ColonneDao {

    private static final Logger logger = LoggerFactory.getLogger(ColonneDaoImpl.class);

    @Autowired
    private RDFConnection rdfConnection;

    @Override
    protected Class<Colonne> getClazz() {
	return Colonne.class;
    }

    @Override
    public Colonne trouverParAltLabel(String altLabel) {
	try (RepositoryConnection connection = rdfConnection.getConnection(BaseRDF.INTERNE)) {
	    StringBuilder requete = new StringBuilder("SELECT DISTINCT ?colonne ?libelle ?code ?iriVariable ");
	    requete.append("WHERE { ");
	    requete.append("?colonne <" + RDF.TYPE + "> <" + Type.COLONNE + "> . ");
	    requete.append("?colonne <" + SKOS.PREF_LABEL + "> ?libelle . ");
	    requete.append("?colonne <" + SKOS.NOTATION + "> ?code . ");
	    requete.append("?colonne <" + SKOS.ALT_LABEL + "> ?altLibelle . ");
	    requete.append("OPTIONAL { ?colonne <" + SKOS.RELATED + "> ?iriVariable } ");
	    requete.append("FILTER (lcase(?altLibelle) = lcase('" + altLabel + "')) ");
	    requete.append("} ");
	    System.out.println(requete.toString());
	    TupleQuery tupleQuery = connection.prepareTupleQuery(requete.toString());

	    try (TupleQueryResult result = tupleQuery.evaluate()) {
		Colonne colonne = null;
		if (result.hasNext()) {
		    BindingSet bindingSet = result.next();
		    colonne = new Colonne();
		    colonne.setIri(bindingSet.getValue("colonne").stringValue());
		    colonne.setLibelle(bindingSet.getValue("libelle").stringValue());
		    colonne.setCode(bindingSet.getValue("code").stringValue());
		    if (bindingSet.getValue("iriVariable") != null) {
			colonne.setIriVariable(bindingSet.getValue("iriVariable").stringValue());
		    }
		}
		return colonne;
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
