package fr.insee.bidbo.vocabulary;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;

import fr.insee.bidbo.vocabulary.str.QBStr;

public class QB {

    public static final String NAMESPACE = QBStr.NAMESPACE;

    public static final IRI DATA_SET_CLASS;
    public static final IRI DATA_SET;
    public static final IRI OBSERVATION_CLASS;
    public static final IRI OBSERVATION;
    public static final IRI OBSERVATION_GROUP_CLASS;
    public static final IRI OBSERVATION_GROUP;
    public static final IRI SLICE_CLASS;
    public static final IRI SLICE;
    public static final IRI STRUCTURE;
    public static final IRI MEASURE_TYPE;
    public static final IRI DATA_STRUCTURE_DEFINITION;
    public static final IRI COMPONENT;
    public static final IRI COMPONENT_SPECIFICATION;
    public static final IRI COMPONENT_ATTACHMENT;
    public static final IRI MEASURE;
    public static final IRI ATTRIBUTE;
    public static final IRI DIMENSION;
    public static final IRI CODED_PROPERTY;
    public static final IRI DIMENSION_PROPERTY;
    public static final IRI ATTRIBUTE_PROPERTY;
    public static final IRI MEASURE_PROPERTY;
    public static final IRI SLICE_KEY;
    public static final IRI SLICE_KEY_CLASS;
    public static final IRI SLICE_STRUCTURE;
    public static final IRI CONCEPT;
    public static final IRI ORDER;
    public static final IRI COMPONENT_PROPERTY;
    public static final IRI CODE_LIST;

    static {
	final ValueFactory f = SimpleValueFactory.getInstance();

	DATA_SET_CLASS = f.createIRI(QBStr.DATA_SET_CLASS);
	DATA_SET = f.createIRI(QBStr.DATA_SET);
	OBSERVATION_CLASS = f.createIRI(QBStr.OBSERVATION_CLASS);
	OBSERVATION = f.createIRI(QBStr.OBSERVATION);
	OBSERVATION_GROUP_CLASS = f.createIRI(QBStr.OBSERVATION_GROUP_CLASS);
	OBSERVATION_GROUP = f.createIRI(QBStr.OBSERVATION_GROUP);
	SLICE_CLASS = f.createIRI(QBStr.SLICE_CLASS);
	SLICE = f.createIRI(QBStr.SLICE);
	STRUCTURE = f.createIRI(QBStr.STRUCTURE);
	MEASURE_TYPE = f.createIRI(QBStr.MEASURE_TYPE);
	DATA_STRUCTURE_DEFINITION = f.createIRI(QBStr.DATA_STRUCTURE_DEFINITION);
	COMPONENT = f.createIRI(QBStr.COMPONENT);
	COMPONENT_SPECIFICATION = f.createIRI(QBStr.COMPONENT_SPECIFICATION);
	COMPONENT_ATTACHMENT = f.createIRI(QBStr.COMPONENT_ATTACHMENT);
	MEASURE = f.createIRI(QBStr.MEASURE);
	ATTRIBUTE = f.createIRI(QBStr.ATTRIBUTE);
	DIMENSION = f.createIRI(QBStr.DIMENSION);
	CODED_PROPERTY = f.createIRI(QBStr.CODED_PROPERTY);
	DIMENSION_PROPERTY = f.createIRI(QBStr.DIMENSION_PROPERTY);
	ATTRIBUTE_PROPERTY = f.createIRI(QBStr.ATTRIBUTE_PROPERTY);
	MEASURE_PROPERTY = f.createIRI(QBStr.MEASURE_PROPERTY);
	SLICE_KEY = f.createIRI(QBStr.SLICE_KEY);
	SLICE_KEY_CLASS = f.createIRI(QBStr.SLICE_KEY_CLASS);
	SLICE_STRUCTURE = f.createIRI(QBStr.SLICE_STRUCTURE);
	CONCEPT = f.createIRI(QBStr.CONCEPT);
	ORDER = f.createIRI(QBStr.ORDER);
	COMPONENT_PROPERTY = f.createIRI(QBStr.COMPONENT_PROPERTY);
	CODE_LIST = f.createIRI(QBStr.CODE_LIST);

    }

}
