package fr.insee.bidbo.vocabulary;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;

import fr.insee.bidbo.vocabulary.str.SdmxAttributeStr;

public class SdmxAttribute {

    public static final String NAMESPACE = SdmxAttributeStr.NAMESPACE;

    public static final IRI CONF_STATUS;
    public static final IRI UNIT_MULT;
    public static final IRI DECIMALS;
    public static final IRI DATA_REV;
    public static final IRI UNIT_MEASURE;

    static {
	final ValueFactory f = SimpleValueFactory.getInstance();

	CONF_STATUS = f.createIRI(SdmxAttributeStr.CONF_STATUS);
	UNIT_MULT = f.createIRI(SdmxAttributeStr.UNIT_MULT);
	DECIMALS = f.createIRI(SdmxAttributeStr.DECIMALS);
	DATA_REV = f.createIRI(SdmxAttributeStr.DATA_REV);
	UNIT_MEASURE = f.createIRI(SdmxAttributeStr.UNIT_MEASURE);

    }

}
