package fr.insee.bidbo.rdfinsee;

import java.util.List;

import org.eclipse.rdf4j.model.Statement;

import fr.insee.bidbo.dao.BaseRDF;

public interface GenericDao<T> {

    void update(T t, List<Statement> enlever, List<Statement> ajout);

    void update(BaseRDF base, T t);

    void remove(T t, List<Statement> enlever);

    void remove(BaseRDF base, T t);

    void add(BaseRDF base, T t);

    void add(T t, List<Statement> ajout);

    List<T> findAll(BaseRDF base);

    List<T> filterByPredicat(BaseRDF base, String predicat, String value);

    T findByPredicat(BaseRDF base, String predicat, String value);

    T findByIri(BaseRDF base, String iri);

}
