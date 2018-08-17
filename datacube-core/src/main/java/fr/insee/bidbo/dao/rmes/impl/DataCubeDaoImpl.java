package fr.insee.bidbo.dao.rmes.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.rdf4j.model.vocabulary.DCTERMS;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.eclipse.rdf4j.model.vocabulary.SKOS;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.query.Update;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.insee.bidbo.dao.BaseRDF;
import fr.insee.bidbo.dao.RDFConnection;
import fr.insee.bidbo.dao.rmes.DataCubeDao;
import fr.insee.bidbo.model.rmes.Attribut;
import fr.insee.bidbo.model.rmes.ConceptMesure;
import fr.insee.bidbo.model.rmes.DataCube;
import fr.insee.bidbo.model.rmes.Dimension;
import fr.insee.bidbo.rdfinsee.AbstractGenericDao;
import fr.insee.bidbo.rdfinsee.wrapper.RequestWrapper;
import fr.insee.bidbo.vocabulary.QB;

@Component
public class DataCubeDaoImpl extends AbstractGenericDao<DataCube> implements DataCubeDao {

    private static final Logger logger = LoggerFactory.getLogger(DataCubeDaoImpl.class);

    @Autowired
    private RDFConnection rdfConnection;

    @Override
    protected Class<DataCube> getClazz() {
	return DataCube.class;
    }

    @Override
    public List<DataCube> relatedToAConceptMesure(String iriConceptMesure) {
	RequestWrapper request = searchFields();
	request.getWhere().append("?iri" + delimiter(QB.COMPONENT) + " ?bn . ");
	request.getWhere().append("?bn " + delimiter(QB.MEASURE) + delimiter(iriConceptMesure) + " .");
	return findAll(BaseRDF.RMES, request);
    }

    @Override
    public List<DataCube> relatedToAConcept(String iriConcept) {
	RequestWrapper request = searchFields();
	request.getWhere().append("?iri" + delimiter(QB.COMPONENT) + " ?bn . ");
	request.getWhere().append("{ ?bn " + delimiter(QB.DIMENSION) + " ?dimension } ");
	request.getWhere().append("UNION ");
	request.getWhere().append("{ ?bn " + delimiter(QB.ATTRIBUTE) + " ?dimension } ");
	request.getWhere().append("?dimension " + delimiter(QB.CONCEPT) + delimiter(iriConcept) + " . ");
	return findAll(BaseRDF.RMES, request);
    }

    @Override
    public List<DataCube> relatedToAConceptScheme(String iriConceptScheme) {
	RequestWrapper request = searchFields();
	request.getWhere().append("?iri" + delimiter(QB.COMPONENT) + " ?bn . ");
	request.getWhere().append("{ ?bn " + delimiter(QB.DIMENSION) + " ?dimension } ");
	request.getWhere().append("UNION ");
	request.getWhere().append("{ ?bn " + delimiter(QB.ATTRIBUTE) + " ?dimension } ");
	request.getWhere().append("?dimension " + delimiter(RDFS.RANGE) + " ?concept . ");
	request.getWhere().append("?concept " + delimiter(RDFS.SEEALSO) + delimiter(iriConceptScheme) + " . ");
	return findAll(BaseRDF.RMES, request);
    }

    @Override
    public List<ConceptMesure> trouverConceptsMesures(String iri) {
	try (RepositoryConnection connection = rdfConnection.getConnection(BaseRDF.RMES)) {
	    StringBuilder requete = new StringBuilder("SELECT DISTINCT ?concept ?code ?libelle ");
	    requete.append("WHERE { ");
	    requete.append(delimiter(iri) + delimiter(QB.COMPONENT) + " ?bn . ");
	    requete.append("?bn a " + delimiter(QB.COMPONENT_SPECIFICATION) + " . ");
	    requete.append("?bn " + delimiter(QB.MEASURE) + " ?concept . ");
	    requete.append("?concept a " + delimiter(QB.MEASURE_PROPERTY) + " . ");
	    requete.append("?concept " + delimiter(DCTERMS.IDENTIFIER) + " ?code . ");
	    requete.append("?concept " + delimiter(RDFS.LABEL) + " ?libelle . ");
	    requete.append("FILTER (lang(?libelle) = 'fr') ");
	    requete.append("} ");
	    TupleQuery tupleQuery = connection.prepareTupleQuery(requete.toString());

	    try (TupleQueryResult result = tupleQuery.evaluate()) {
		List<ConceptMesure> datacubes = new ArrayList<>();
		while (result.hasNext()) {
		    BindingSet bindingSet = result.next();
		    ConceptMesure concept = new ConceptMesure();
		    concept.setIri(bindingSet.getValue("concept").stringValue());
		    concept.setCode(bindingSet.getValue("code").stringValue());
		    concept.setLibelleFr(bindingSet.getValue("libelle").stringValue());
		    datacubes.add(concept);
		}
		return datacubes;
	    } catch (Exception e) {
		logger.error("La requête est erronée", e);
		return null;
	    }
	} catch (Exception e) {
	    logger.error("connexion base RDF inaccessible", e);
	    return null;
	}
    }

    @Override
    public List<Attribut> trouverAttributs(String iri) {
	try (RepositoryConnection connection = rdfConnection.getConnection(BaseRDF.RMES)) {
	    StringBuilder requete = new StringBuilder(
		    "SELECT DISTINCT ?attribut ?attachment ?libelleFr ?libelleEn ?concept ?range ?conceptScheme ?code ");
	    requete.append("WHERE { ");
	    requete.append(delimiter(iri) + delimiter(QB.COMPONENT) + " ?bn . ");
	    requete.append("?bn a " + delimiter(QB.COMPONENT_SPECIFICATION) + " . ");
	    requete.append("?bn " + delimiter(QB.ATTRIBUTE) + " ?attribut . ");
	    requete.append("?bn " + delimiter(QB.COMPONENT_ATTACHMENT) + " ?attachment . ");
	    requete.append("?attribut a " + delimiter(QB.CODED_PROPERTY) + " . ");
	    requete.append("?attribut a " + delimiter(QB.ATTRIBUTE_PROPERTY) + " . ");
	    requete.append("?attribut " + delimiter(RDFS.LABEL) + " ?libelleFr . ");
	    requete.append("?attribut " + delimiter(RDFS.LABEL) + " ?libelleEn . ");
	    requete.append("?attribut " + delimiter(RDFS.RANGE) + " ?range . ");
	    requete.append("?attribut " + delimiter(QB.CONCEPT) + " ?concept . ");
	    requete.append("?attribut " + delimiter(QB.CODE_LIST) + " ?conceptScheme . ");
	    requete.append("?range " + delimiter(SKOS.NOTATION) + " ?code . ");
	    requete.append("FILTER (lang(?libelleFr) = 'fr') ");
	    requete.append("FILTER (lang(?libelleEn) = 'en') ");
	    requete.append("} ");
	    TupleQuery tupleQuery = connection.prepareTupleQuery(requete.toString());

	    try (TupleQueryResult result = tupleQuery.evaluate()) {
		List<Attribut> attributs = new ArrayList<>();
		while (result.hasNext()) {
		    BindingSet bindingSet = result.next();
		    Attribut attribut = new Attribut();
		    attribut.setIri(bindingSet.getValue("attribut").stringValue());
		    attribut.setIriAttachment(bindingSet.getValue("attachment").stringValue());
		    attribut.setCode(bindingSet.getValue("code").stringValue());
		    attribut.setLibelleFr(bindingSet.getValue("libelleFr").stringValue());
		    attribut.setLibelleEn(bindingSet.getValue("libelleEn").stringValue());
		    attribut.setIriRange(bindingSet.getValue("range").stringValue());
		    attribut.setIriConcept(bindingSet.getValue("concept").stringValue());
		    attribut.setIriConceptScheme(bindingSet.getValue("conceptScheme").stringValue());
		    attributs.add(attribut);
		}
		return attributs;
	    } catch (Exception e) {
		logger.error("La requête est erronée", e);
		return null;
	    }
	} catch (Exception e) {
	    logger.error("connexion base RDF inaccessible", e);
	    return null;
	}
    }

    @Override
    public List<Dimension> trouverDimensions(String iri) {
	try (RepositoryConnection connection = rdfConnection.getConnection(BaseRDF.RMES)) {
	    StringBuilder requete = new StringBuilder(
		    "SELECT DISTINCT ?dimension ?attachment ?order ?libelleFr ?libelleEn ?concept ?range ?conceptScheme ?code ");
	    requete.append("WHERE { ");
	    requete.append(delimiter(iri) + delimiter(QB.COMPONENT) + " ?bn . ");
	    requete.append("?bn a " + delimiter(QB.COMPONENT_SPECIFICATION) + " . ");
	    requete.append("?bn " + delimiter(QB.DIMENSION) + " ?dimension . ");
	    requete.append("OPTIONAL { ?bn " + delimiter(QB.COMPONENT_ATTACHMENT) + " ?attachment } ");
	    requete.append("OPTIONAL { ?bn " + delimiter(QB.ORDER) + " ?order } ");
	    requete.append("?dimension a " + delimiter(QB.CODED_PROPERTY) + " . ");
	    requete.append("?dimension a " + delimiter(QB.DIMENSION_PROPERTY) + " . ");
	    requete.append("?dimension " + delimiter(RDFS.LABEL) + " ?libelleFr . ");
	    requete.append("?dimension " + delimiter(RDFS.LABEL) + " ?libelleEn . ");
	    requete.append("?dimension " + delimiter(RDFS.RANGE) + " ?range . ");
	    requete.append("?dimension " + delimiter(QB.CONCEPT) + " ?concept . ");
	    requete.append("?dimension " + delimiter(QB.CODE_LIST) + " ?conceptScheme . ");
	    requete.append("?range " + delimiter(SKOS.NOTATION) + " ?code . ");
	    requete.append("FILTER (lang(?libelleFr) = 'fr') ");
	    requete.append("FILTER (lang(?libelleEn) = 'en') ");
	    requete.append("} ");
	    TupleQuery tupleQuery = connection.prepareTupleQuery(requete.toString());

	    try (TupleQueryResult result = tupleQuery.evaluate()) {
		List<Dimension> dimensions = new ArrayList<>();
		while (result.hasNext()) {
		    BindingSet bindingSet = result.next();
		    Dimension dimension = new Dimension();
		    dimension.setIri(bindingSet.getValue("dimension").stringValue());
		    if (bindingSet.getValue("attachment") != null) {
			dimension.setIriAttachment(bindingSet.getValue("attachment").stringValue());
		    }
		    if (bindingSet.getValue("order") != null) {
			dimension.setOrdre(Integer.valueOf(bindingSet.getValue("order").stringValue()));
		    }
		    dimension.setCode(bindingSet.getValue("code").stringValue());
		    dimension.setLibelleFr(bindingSet.getValue("libelleFr").stringValue());
		    dimension.setLibelleEn(bindingSet.getValue("libelleEn").stringValue());
		    dimension.setIriRange(bindingSet.getValue("range").stringValue());
		    dimension.setIriConcept(bindingSet.getValue("concept").stringValue());
		    dimension.setIriConceptScheme(bindingSet.getValue("conceptScheme").stringValue());
		    dimensions.add(dimension);
		}
		return dimensions;
	    } catch (Exception e) {
		logger.error("La requête est erronée", e);
		return null;
	    }
	} catch (Exception e) {
	    logger.error("connexion base RDF inaccessible", e);
	    return null;
	}
    }

    @Override
    public List<String> trouverConcepts() {
	try (RepositoryConnection connection = rdfConnection.getConnection(BaseRDF.RMES)) {
	    StringBuilder requete = new StringBuilder("SELECT DISTINCT ?concept ");
	    requete.append("WHERE { ");
	    requete.append("?dimension a " + delimiter(QB.CODED_PROPERTY) + " . ");
	    requete.append("?dimension a " + delimiter(QB.DIMENSION_PROPERTY) + " . ");
	    requete.append("?dimension " + delimiter(QB.CONCEPT) + "  ?concept. ");
	    requete.append("} ");
	    TupleQuery tupleQuery = connection.prepareTupleQuery(requete.toString());

	    try (TupleQueryResult result = tupleQuery.evaluate()) {
		List<String> concepts = new ArrayList<>();
		while (result.hasNext()) {
		    BindingSet bindingSet = result.next();
		    concepts.add(bindingSet.getValue("concept").stringValue());
		}
		return concepts;
	    } catch (Exception e) {
		logger.error("La requête est erronée", e);
		return null;
	    }
	} catch (Exception e) {
	    logger.error("connexion base RDF inaccessible", e);
	    return null;
	}
    }

    @Override
    public void supprimerEnCascade(DataCube datacube) {
	try (RepositoryConnection connection = rdfConnection.getConnection(BaseRDF.RMES)) {
	    StringBuilder requete = new StringBuilder("DELETE { ");
	    requete.append(delimiter(datacube.getIri()) + delimiter(QB.COMPONENT) + " ?bn . ");
	    requete.append("?bn ?predicat ?value. ");
	    requete.append("} ");
	    requete.append("WHERE { ");
	    requete.append(delimiter(datacube.getIri()) + delimiter(QB.COMPONENT) + " ?bn . ");
	    requete.append("OPTIONAL { ?bn ?predicat ?value } ");
	    requete.append("} ");
	    Update update = connection.prepareUpdate(requete.toString());
	    update.execute();

	} catch (Exception e) {
	    logger.error("connexion base RDF inaccessible", e);
	}
	remove(BaseRDF.RMES, datacube);
    }

}
