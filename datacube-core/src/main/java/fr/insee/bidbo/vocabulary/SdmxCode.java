package fr.insee.bidbo.vocabulary;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;

import fr.insee.bidbo.vocabulary.str.SdmxCodeStr;

public class SdmxCode {

    public static final String NAMESPACE = SdmxCodeStr.NAMESPACE;

    public static final IRI CONF_STATUT_CLASS;
    public static final IRI CONF_STATUT;

    static {
	final ValueFactory f = SimpleValueFactory.getInstance();

	CONF_STATUT_CLASS = f.createIRI(SdmxCodeStr.CONF_STATUT_CLASS);
	CONF_STATUT = f.createIRI(SdmxCodeStr.CONF_STATUT);

    }

}
