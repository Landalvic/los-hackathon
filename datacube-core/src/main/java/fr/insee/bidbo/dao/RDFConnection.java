package fr.insee.bidbo.dao;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.rdf4j.model.BNode;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import fr.insee.bidbo.rdfinsee.RemoveStatement;
import fr.insee.bidbo.rdfinsee.enumeration.Langue;

@Component
public class RDFConnection {

    public static final String IRI_BASE_PROJET = "http://data.insee.fr";
    private static final Logger logger = LoggerFactory.getLogger(RDFConnection.class);
    private HashMap<BaseRDF, Repository> repositories;
    private ValueFactory factory;

    private void createConnection() {
	repositories = new HashMap<>();
	for (BaseRDF baseRdf : BaseRDF.values()) {
	    try {
		Repository repository;
		if (baseRdf.isHttp()) {
		    repository = new HTTPRepository(baseRdf.getUrlServer(), baseRdf.getRepository());
		} else {
		    repository = new SPARQLRepository(baseRdf.getUrlServer());
		}
		repositories.put(baseRdf, repository);
		repository.initialize();
		if (baseRdf == BaseRDF.EXTERNE) {
		    factory = repository.getValueFactory();
		}
	    } catch (Exception e) {
	    }
	}
    }

    public ValueFactory getFactory() {
	if (factory == null) {
	    createConnection();
	}
	return factory;
    }

    public RepositoryConnection getConnection(BaseRDF baseRdf) {
	if (repositories == null) {
	    createConnection();
	}
	return repositories.get(baseRdf).getConnection();
    }

    public IRI createIRI(String iri) {
	return getFactory().createIRI(iri);
    }

    public Value createLiteral(String litteral) {
	return getFactory().createLiteral(litteral);
    }

    public Value createLiteral(String litteral, Langue langue) {
	return getFactory().createLiteral(litteral, langue.getId());
    }

    public Value createLiteral(double decimal) {
	return getFactory().createLiteral(decimal);
    }

    public Value createLiteral(long entier) {
	return getFactory().createLiteral(entier);
    }

    public Value createLiteral(int entier) {
	return getFactory().createLiteral(entier);
    }

    public Value createLiteral(Date date) {
	return getFactory().createLiteral(date);
    }

    public BNode createBlankNode() {
	return getFactory().createBNode();
    }

    private Resource transformResource(Object object) {
	Resource resource = null;
	if (object != null) {
	    if (object instanceof Resource) {
		resource = (Resource) object;
	    } else {
		resource = createIRI(object.toString());
	    }
	}
	return resource;
    }

    private IRI transformIRI(Object object) {
	IRI iri = null;
	if (object != null) {
	    if (object instanceof IRI) {
		iri = (IRI) object;
	    } else {
		iri = createIRI(object.toString());
	    }
	}
	return iri;
    }

    private Value transformValue(Object object) {
	Value value = null;
	if (object != null) {
	    if (object instanceof Value) {
		value = (Value) object;
	    } else {
		value = createIRI(object.toString());
	    }
	}
	return value;
    }

    public Statement createStatement(Object subject, Object predicate, Object object, Object context) {
	Resource resource = transformResource(subject);
	IRI iri = transformIRI(predicate);
	Value value = transformValue(object);
	Resource resourceContext = transformResource(context);
	return getFactory().createStatement(resource, iri, value, resourceContext);
    }

    public Statement createStatement(Object subject, Object predicate, Object object) {
	return createStatement(subject, predicate, object, null);
    }

    public Statement removeStatement(Object subject, Object predicate, Object object, Object context) {
	Resource resource = transformResource(subject);
	IRI iri = transformIRI(predicate);
	Value value = transformValue(object);
	Resource resourceContext = transformResource(context);
	return new RemoveStatement(resource, iri, value, resourceContext);
    }

    public Statement getStatement(Object subject, Object predicate, Object object) {
	return createStatement(subject, predicate, object, null);
    }

    public void transfererTriplets(BaseRDF baseRdfFrom, BaseRDF baseRdfTo, List<Statement> triplets) {
	try (RepositoryConnection connectionFrom = getConnection(baseRdfFrom);
		RepositoryConnection connectionTo = getConnection(baseRdfTo)) {
	    for (Statement triplet : triplets) {
		connectionTo.add(connectionFrom.getStatements(triplet.getSubject(), triplet.getPredicate(),
			triplet.getObject(), triplet.getContext()));
	    }
	} catch (RepositoryException e) {
	    logger.error("connexion base RDF inaccessible" + baseRdfFrom.getUrlServer() + "/"
		    + baseRdfFrom.getRepository() + " ou connexion base RDF inaccessible" + baseRdfTo.getUrlServer()
		    + "/" + baseRdfTo.getRepository(), e);
	}
    }

    public void ajouterTriplets(BaseRDF baseRdf, List<Statement> triplets) {
	try (RepositoryConnection connection = getConnection(baseRdf)) {
	    connection.add(triplets);
	} catch (RepositoryException e) {
	    logger.error("connexion base RDF inaccessible" + baseRdf.getUrlServer() + "/" + baseRdf.getRepository(), e);
	}
    }

    public void ajouterTriplets(BaseRDF baseRdf, Statement triplet) {
	ajouterTriplets(baseRdf, Arrays.asList(triplet));
    }

    public void enleverTriplets(BaseRDF baseRdf, List<Statement> triplets) {
	try (RepositoryConnection connection = getConnection(baseRdf)) {
	    connection.remove(triplets);
	} catch (RepositoryException e) {
	    logger.error("connexion base RDF inaccessible" + baseRdf.getUrlServer() + "/" + baseRdf.getRepository(), e);
	}
    }

    public void enleverTriplets(BaseRDF baseRdf, Statement triplet) {
	enleverTriplets(baseRdf, Arrays.asList(triplet));
    }

    public void clearContext(BaseRDF baseRdf, Resource context) {
	try (RepositoryConnection connection = getConnection(baseRdf)) {
	    connection.clear(context);
	} catch (RepositoryException e) {
	    logger.error("connexion base RDF inaccessible" + baseRdf.getUrlServer() + "/" + baseRdf.getRepository(), e);
	}
    }

    public void addNamespace(BaseRDF baseRdf, Map<String, String> map) {
	try (RepositoryConnection connection = getConnection(baseRdf)) {
	    for (Entry<String, String> entry : map.entrySet()) {
		connection.setNamespace(entry.getKey(), entry.getValue());
	    }
	} catch (RepositoryException e) {
	    logger.error("connexion base RDF inaccessible" + baseRdf.getUrlServer() + "/" + baseRdf.getRepository(), e);
	}
    }

    public static boolean isIri(String s) {
	return s != null && s.indexOf(':') >= 0;
    }
}
