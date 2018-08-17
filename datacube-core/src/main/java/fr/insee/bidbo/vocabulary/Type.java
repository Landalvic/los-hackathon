package fr.insee.bidbo.vocabulary;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;

import fr.insee.bidbo.dao.RDFConnection;

public class Type {

    public static final String NAMESPACE = RDFConnection.IRI_BASE_PROJET + "/type#";

    public static final IRI COLONNE;

    static {
	final ValueFactory f = SimpleValueFactory.getInstance();

	COLONNE = f.createIRI(NAMESPACE, "Colonne");

    }

}
