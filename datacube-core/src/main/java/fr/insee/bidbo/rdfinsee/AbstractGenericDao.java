package fr.insee.bidbo.rdfinsee;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import fr.insee.bidbo.dao.BaseRDF;
import fr.insee.bidbo.dao.RDFConnection;
import fr.insee.bidbo.exception.MelodiException;
import fr.insee.bidbo.rdfinsee.annotation.RdfInseeIRI;
import fr.insee.bidbo.rdfinsee.annotation.RdfInseeListIRI;
import fr.insee.bidbo.rdfinsee.annotation.RdfInseeListValue;
import fr.insee.bidbo.rdfinsee.annotation.RdfInseeObjectIRI;
import fr.insee.bidbo.rdfinsee.annotation.RdfInseeObjectValue;
import fr.insee.bidbo.rdfinsee.annotation.RdfInseeType;
import fr.insee.bidbo.rdfinsee.annotation.RdfInseeValue;
import fr.insee.bidbo.rdfinsee.enumeration.Langue;
import fr.insee.bidbo.rdfinsee.wrapper.FieldWrapper;
import fr.insee.bidbo.rdfinsee.wrapper.RequestWrapper;
import fr.insee.bidbo.utils.StringBuilderUtils;

public abstract class AbstractGenericDao<T> implements GenericDao<T> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractGenericDao.class);
    private static final String SEPARATOR = "#1#";

    protected abstract Class<T> getClazz();

    @Autowired
    private RDFConnection rdfConnection;

    public void update(T t, IRI oldIri, List<Statement> enlever, List<Statement> ajout) {
	try {
	    enlever.add(rdfConnection.createStatement(oldIri, null, null, null));
	    add(t, ajout);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public void update(T t, String oldIri, List<Statement> enlever, List<Statement> ajout) {
	update(t, rdfConnection.createIRI(oldIri), enlever, ajout);
    }

    @Override
    public void update(T t, List<Statement> enlever, List<Statement> ajout) {
	try {
	    IRI iri = getIri(t);
	    update(t, iri, enlever, ajout);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public void update(BaseRDF base, T t, IRI oldIri) {
	List<Statement> enlever = new LinkedList<>();
	List<Statement> ajout = new LinkedList<>();
	update(t, oldIri, enlever, ajout);
	rdfConnection.enleverTriplets(base, enlever);
	rdfConnection.ajouterTriplets(base, ajout);
    }

    public void update(BaseRDF base, T t, String oldIri) {
	update(base, t, rdfConnection.createIRI(oldIri));
    }

    @Override
    public void update(BaseRDF base, T t) {
	try {
	    IRI iri = getIri(t);
	    update(base, t, iri);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    @Override
    public void remove(T t, List<Statement> enlever) {
	IRI iri;
	try {
	    iri = getIri(t);
	    enlever.add(rdfConnection.removeStatement(iri, null, null, null));
	    enlever.add(rdfConnection.removeStatement(null, null, iri, null));
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    @Override
    public void remove(BaseRDF base, T t) {
	List<Statement> enlever = new LinkedList<>();
	remove(t, enlever);
	rdfConnection.enleverTriplets(base, enlever);
    }

    @Override
    public void add(BaseRDF base, T t) {
	List<Statement> ajout = new LinkedList<>();
	add(t, ajout);
	rdfConnection.ajouterTriplets(base, ajout);
    }

    private IRI getIri(T t) throws IllegalArgumentException, IllegalAccessException {
	String iriStr = null;
	for (Field field : getClazz().getDeclaredFields()) {
	    RdfInseeIRI annotationIri = field.getAnnotation(RdfInseeIRI.class);
	    if (annotationIri != null) {
		field.setAccessible(true);
		iriStr = (String) field.get(t);
		break;
	    }
	}
	return rdfConnection.createIRI(iriStr);
    }

    @Override
    public void add(T t, List<Statement> ajout) {
	try {
	    IRI iri = getIri(t);
	    String[] types = getClazz().getAnnotation(RdfInseeType.class).value();
	    IRI iriType = rdfConnection.createIRI(types[0]);
	    for (String type : types) {
		ajout.add(rdfConnection.createStatement(iri, RDF.TYPE, type, iriType));
	    }
	    for (Field field : getClazz().getDeclaredFields()) {
		RdfInseeValue annValue = field.getAnnotation(RdfInseeValue.class);
		RdfInseeObjectIRI annIri = field.getAnnotation(RdfInseeObjectIRI.class);
		if (annValue != null) {
		    String predicat = trouverPredicat(t, annValue.value(), annValue.reference());
		    field.setAccessible(true);
		    if (field.get(t) != null) {
			if (field.getType().equals(Date.class)) {
			    ajout.add(rdfConnection.createStatement(iri, predicat,
				    rdfConnection.createLiteral((Date) field.get(t)), iriType));
			} else if (field.getType().equals(Long.class)) {
			    ajout.add(rdfConnection.createStatement(iri, predicat,
				    rdfConnection.createLiteral((Long) field.get(t)), iriType));
			} else if (field.getType().equals(Integer.class)) {
			    ajout.add(rdfConnection.createStatement(iri, predicat,
				    rdfConnection.createLiteral((Integer) field.get(t)), iriType));
			} else if (field.getType().equals(Double.class)) {
			    ajout.add(rdfConnection.createStatement(iri, predicat,
				    rdfConnection.createLiteral((Double) field.get(t)), iriType));
			} else {
			    if (annValue.lang() != Langue.NULL) {
				ajout.add(rdfConnection.createStatement(iri, predicat,
					rdfConnection.createLiteral((String) field.get(t), annValue.lang()), iriType));
			    } else {
				ajout.add(rdfConnection.createStatement(iri, predicat,
					rdfConnection.createLiteral((String) field.get(t)), iriType));
			    }
			}
		    } else if (!annValue.isNullable()) {
			throw new MelodiException(
				"Le champ " + field.getName() + " de l'objet " + getClazz() + " ne peut pas être null");
		    }
		} else if (annIri != null) {
		    String predicat = trouverPredicat(t, annIri.value(), annIri.reference());
		    field.setAccessible(true);
		    if (field.get(t) != null) {
			ajout.add(rdfConnection.createStatement(iri, predicat, field.get(t), iriType));
		    }
		}
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    @Override
    public List<T> findAll(BaseRDF base) {
	return findAll(base, searchFields());
    }

    protected List<T> findAll(BaseRDF base, RequestWrapper request) {
	try (RepositoryConnection connection = rdfConnection.getConnection(base)) {
	    TupleQuery tupleQuery = connection.prepareTupleQuery(createStringRequest(request).toString());

	    try (TupleQueryResult result = tupleQuery.evaluate()) {
		List<T> ts = new ArrayList<>();
		while (result.hasNext()) {
		    ts.add(bindField(result.next(), request));
		}
		return ts;
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
    public List<T> filterByPredicat(BaseRDF base, String predicat, String value) {
	return filterByPredicat(base, predicat, value, searchFields(predicat, value));
    }

    protected List<T> filterByPredicat(BaseRDF base, String predicat, String value, RequestWrapper request) {
	try (RepositoryConnection connection = rdfConnection.getConnection(base)) {
	    TupleQuery tupleQuery = connection.prepareTupleQuery(createStringRequest(request).toString());

	    try (TupleQueryResult result = tupleQuery.evaluate()) {
		List<T> ts = new ArrayList<>();
		while (result.hasNext()) {
		    ts.add(bindFieldFilter(result.next(), request, value));
		}
		return ts;
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
    public T findByIri(BaseRDF base, String iri) {
	RequestWrapper request = searchFields(iri);
	StringBuilder requete = createStringRequest(request).append(" LIMIT 1 ");
	try (RepositoryConnection connection = rdfConnection.getConnection(base)) {
	    TupleQuery tupleQuery = connection.prepareTupleQuery(requete.toString());

	    try (TupleQueryResult result = tupleQuery.evaluate()) {
		if (result.hasNext()) {
		    return bindFieldFilter(result.next(), request, iri);
		} else {
		    return null;
		}
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
    public T findByPredicat(BaseRDF base, String predicat, String value) {
	RequestWrapper request = searchFields(predicat, value);
	StringBuilder requete = createStringRequest(request).append(" LIMIT 1 ");
	try (RepositoryConnection connection = rdfConnection.getConnection(base)) {
	    TupleQuery tupleQuery = connection.prepareTupleQuery(requete.toString());

	    try (TupleQueryResult result = tupleQuery.evaluate()) {
		if (result.hasNext()) {
		    return bindFieldFilter(result.next(), request, value);
		} else {
		    return null;
		}
	    } catch (Exception e) {
		logger.error("La requête est erronée", e);
		return null;
	    }
	} catch (Exception e) {
	    logger.error("connexion base RDF inaccessible", e);
	    return null;
	}
    }

    protected String delimiter(IRI iri) {
	return delimiter(iri.stringValue());
    }

    protected String delimiter(String iri) {
	return " <" + iri + "> ";
    }

    protected Date getDate(BindingSet bindingSet, String bindingName) {
	Value value = bindingSet.getValue(bindingName);
	if (value != null) {
	    return ((Literal) value).calendarValue().toGregorianCalendar().getTime();
	} else {
	    return null;
	}
    }

    protected RequestWrapper searchFields() {
	return searchFields(new RequestWrapper(), null, null, null);
    }

    protected RequestWrapper searchFields(RequestWrapper request) {
	return searchFields(request, null, null, null);
    }

    protected RequestWrapper searchFields(String iri) {
	return searchFields(new RequestWrapper(), iri, null, null);
    }

    protected RequestWrapper searchFields(String predicat, String value) {
	return searchFields(new RequestWrapper(), null, predicat, value);
    }

    protected RequestWrapper searchFields(RequestWrapper request, String predicat, String value) {
	return searchFields(request, null, predicat, value);
    }

    protected RequestWrapper searchFields(RequestWrapper request, String iri, String predicat, String value) {
	return searchFields(request, iri, predicat, value, getClazz(), "");
    }

    private RequestWrapper searchFields(RequestWrapper request, String iri, String predicat, String value,
	    Class<?> clazz, String startName) {
	String varFiltre = null;
	boolean filtreIRI = false;
	String[] types = clazz.getAnnotation(RdfInseeType.class).value();
	String iriString = null;
	StringBuilder select = null;
	if (iri == null) {
	    Field fieldIri = getFieldIri(clazz);
	    if (fieldIri != null) {
		iriString = "?" + startName + fieldIri.getName();
		request.getFieldsWrapper().add(new FieldWrapper(fieldIri));
	    } else {
		iriString = "?" + clazz.getSimpleName();
	    }
	    select = new StringBuilder(iriString + " ");
	} else {
	    iriString = delimiter(iri);
	    select = new StringBuilder();
	    request.setFieldFilter(getFieldIri(clazz));
	}
	StringBuilder where = new StringBuilder();
	for (String type : types) {
	    where.append(iriString + delimiter(RDF.TYPE) + delimiter(type) + " . ");
	}
	for (Field field : clazz.getDeclaredFields()) {
	    RdfInseeValue annotation = field.getAnnotation(RdfInseeValue.class);
	    RdfInseeObjectIRI annotationObjectIri = field.getAnnotation(RdfInseeObjectIRI.class);
	    RdfInseeObjectValue annotationObject = field.getAnnotation(RdfInseeObjectValue.class);
	    RdfInseeListIRI annotationListIri = field.getAnnotation(RdfInseeListIRI.class);
	    RdfInseeListValue annotationListValue = field.getAnnotation(RdfInseeListValue.class);
	    String nameVar = startName + field.getName();
	    if (annotation != null || annotationObjectIri != null) {
		String annValue = annotation != null ? annotation.value() : annotationObjectIri.value();
		boolean annRef = annotation != null ? annotation.reference() : annotationObjectIri.reference();
		String annPredicat = annRef ? " ?" + annValue : delimiter(annValue);
		boolean annIsNullable = annotation != null ? annotation.isNullable() : annotationObjectIri.isNullable();
		select.append("?" + nameVar + " ");
		if (annIsNullable) {
		    where.append("OPTIONAL { ");
		}
		where.append(iriString + annPredicat + " ?" + nameVar + " . ");
		where.append(filterLang(annotation, nameVar));
		if (annIsNullable) {
		    where.append(" } ");
		}
		if (StringUtils.equals(annValue, predicat)) {
		    varFiltre = "?" + nameVar;
		    filtreIRI = annotation == null;
		    request.setFieldFilter(field);
		} else {
		    request.getFieldsWrapper().add(new FieldWrapper(field));
		}
	    } else if (annotationObject != null) {
		FieldWrapper fieldWrapper = new FieldWrapper(field);
		where.append("OPTIONAL { " + iriString + delimiter(annotationObject.value()) + " ?" + nameVar + " } ");
		RequestWrapper sousRequest = searchFields(new RequestWrapper(), null, null, null, field.getType(),
			nameVar + "_");
		select.append(sousRequest.getSelect());
		where.append(sousRequest.getWhere());
		fieldWrapper.setSubList(sousRequest.getFieldsWrapper());
		request.getFieldsWrapper().add(fieldWrapper);
	    } else if (annotationListIri != null || annotationListValue != null) {
		String subNameVar = nameVar + "sub";
		String annValue = annotationListIri != null ? annotationListIri.value() : annotationListValue.value();
		boolean annRef = annotationListIri != null ? annotationListIri.reference()
			: annotationListValue.reference();
		String annPredicat = annRef ? " ?" + annValue : delimiter(annValue);
		boolean annIsNullable = annotationListIri != null ? annotationListIri.isNullable()
			: annotationListValue.isNullable();
		select.append("?" + nameVar + " ");
		if (annIsNullable) {
		    where.append("OPTIONAL { ");
		}
		where.append("{ SELECT (GROUP_CONCAT(?" + subNameVar + "; SEPARATOR=\"" + SEPARATOR + "\") as ?"
			+ nameVar + ") ");
		where.append("WHERE { ");
		where.append(iriString + annPredicat + " ?" + subNameVar + " . ");
		where.append(filterLang(annotationListValue, subNameVar));
		where.append("} ");
		where.append("GROUP BY " + iriString + " ");
		where.append("} ");
		if (annIsNullable) {
		    where.append(" } ");
		}
		request.getFieldsListWrapper().add(new FieldWrapper(field));
	    }
	}

	StringBuilder end = new StringBuilder();

	if (varFiltre != null) {
	    if (filtreIRI) {
		StringBuilderUtils.replaceAll(where, "\\" + varFiltre, delimiter(value));
		StringBuilderUtils.replaceAll(select, "\\" + varFiltre, "");
	    } else {
		where.append("FILTER (lcase(" + varFiltre + ") = lcase('" + value + "')) ");
	    }
	}
	request.setSelect(select);
	request.setWhere(where);
	request.setEnd(end);
	return request;
    }

    private Field getFieldIri(Class<?> clazz) {
	for (Field field : clazz.getDeclaredFields()) {
	    if (field.getAnnotation(RdfInseeIRI.class) != null) {
		return field;
	    }
	}
	return null;
    }

    private String filterLang(RdfInseeValue annotation, String nomVariable) {
	if (annotation != null && annotation.lang() != Langue.NULL) {
	    return "FILTER (lang(?" + nomVariable + ") = '" + annotation.lang().getId() + "') ";
	} else {
	    return "";
	}
    }

    private String filterLang(RdfInseeListValue annotation, String nomVariable) {
	if (annotation != null && annotation.lang() != Langue.NULL) {
	    return "FILTER (lang(?" + nomVariable + ") = '" + annotation.lang().getId() + "') ";
	} else {
	    return "";
	}
    }

    protected StringBuilder createStringRequest(RequestWrapper request) {
	StringBuilder requete = new StringBuilder();
	requete
		.append("SELECT DISTINCT ")
		.append(request.getSelect())
		.append("WHERE { ")
		.append(request.getWhere())
		.append("} ")
		.append(request.getEnd());
	return requete;
    }

    protected T bindField(BindingSet bindingSet, RequestWrapper request) throws Exception {
	return bindField(bindingSet, request.getFieldsWrapper(), request.getFieldsListWrapper(), getClazz(), "");
    }

    private T bindFieldFilter(BindingSet bindingSet, RequestWrapper request, String value) throws Exception {
	T u = bindField(bindingSet, request);
	Field field = request.getFieldFilter();
	field.setAccessible(true);
	if (field.getType().equals(Long.class)) {
	    field.set(u, Long.valueOf(value));
	} else if (field.getType().equals(Integer.class)) {
	    field.set(u, Integer.valueOf(value));
	} else if (field.getType().equals(Double.class)) {
	    field.set(u, Double.valueOf(value));
	} else {
	    field.set(u, value);
	}
	return u;
    }

    private <U> U bindField(BindingSet bindingSet, List<FieldWrapper> fieldsWrapper,
	    List<FieldWrapper> fieldsListWrapper, Class<U> clazz, String startName) throws Exception {
	U u = clazz.newInstance();
	for (FieldWrapper fieldWrapper : fieldsWrapper) {
	    Field field = fieldWrapper.getField();
	    String nameVar = startName + field.getName();
	    field.setAccessible(true);
	    Value v = bindingSet.getValue(nameVar);
	    if (fieldWrapper.getSubList() != null) {
		Object o = bindField(bindingSet, fieldWrapper.getSubList(), new ArrayList<>(), field.getType(),
			startName + "_");
		field.set(u, o);
	    } else if (field.getType().equals(Date.class)) {
		field.set(u, getDate(bindingSet, nameVar));
	    } else if (field.getType().equals(Long.class)) {
		field.set(u, Long.valueOf(v.stringValue()));
	    } else if (field.getType().equals(Integer.class)) {
		field.set(u, Integer.valueOf(v.stringValue()));
	    } else if (field.getType().equals(Double.class)) {
		field.set(u, Double.valueOf(v.stringValue()));
	    } else if (v != null) {
		field.set(u, v.stringValue());
	    }
	}
	for (FieldWrapper fieldWrapper : fieldsListWrapper) {
	    Field field = fieldWrapper.getField();
	    String nameVar = startName + field.getName();
	    field.setAccessible(true);
	    Value v = bindingSet.getValue(nameVar);
	    if (fieldWrapper.getSubList() != null) {
		Object o = bindField(bindingSet, fieldWrapper.getSubList(), new ArrayList<>(), field.getType(),
			startName + "_");
		field.set(u, o);
	    } else if (field.getType().equals(Date.class)) {
		field.set(u, getDate(bindingSet, nameVar));
	    } else if (field.getType().equals(Long.class)) {
		field.set(u, Long.valueOf(v.stringValue()));
	    } else if (field.getType().equals(Integer.class)) {
		field.set(u, Integer.valueOf(v.stringValue()));
	    } else if (field.getType().equals(Double.class)) {
		field.set(u, Double.valueOf(v.stringValue()));
	    } else if (v != null) {
		field.set(u, Arrays.asList(v.stringValue().split(SEPARATOR)));
	    }
	}
	return u;
    }

    private String trouverPredicat(T t, String valueAnnotation, boolean reference) throws Exception {
	String predicat = valueAnnotation;
	if (reference) {
	    Field ref = getClazz().getDeclaredField(valueAnnotation);
	    ref.setAccessible(true);
	    predicat = (String) ref.get(t);
	}
	return predicat;
    }

}
