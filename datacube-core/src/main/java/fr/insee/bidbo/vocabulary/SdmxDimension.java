package fr.insee.bidbo.vocabulary;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;

import fr.insee.bidbo.vocabulary.str.SdmxDimensionStr;

public class SdmxDimension {

    public static final String NAMESPACE = SdmxDimensionStr.NAMESPACE;

    public static final IRI TIME_PERIOD;
    public static final IRI FREQ;

    static {
	final ValueFactory f = SimpleValueFactory.getInstance();

	TIME_PERIOD = f.createIRI(SdmxDimensionStr.TIME_PERIOD);
	FREQ = f.createIRI(SdmxDimensionStr.FREQ);

    }

}
