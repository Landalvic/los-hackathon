package fr.insee.bidbo.rdfinsee;

import java.util.List;

import org.eclipse.rdf4j.model.Statement;

import fr.insee.bidbo.dao.BaseRDF;

public abstract class AbstractGenericService<T> implements GenericService<T> {

    protected abstract GenericDao<T> getDao();

    @Override
    public void update(T t, List<Statement> enlever, List<Statement> ajout) {
	getDao().update(t, enlever, ajout);
    }

    @Override
    public void update(BaseRDF base, T t) {
	getDao().update(base, t);
    }

    @Override
    public void remove(T t, List<Statement> enlever) {
	getDao().remove(t, enlever);
    }

    @Override
    public void remove(BaseRDF base, T t) {
	getDao().remove(base, t);
    }

    @Override
    public void add(BaseRDF base, T t) {
	getDao().add(base, t);
    }

    @Override
    public void add(T t, List<Statement> ajout) {
	getDao().add(t, ajout);
    }

    @Override
    public List<T> findAll(BaseRDF base) {
	return getDao().findAll(base);
    }

    @Override
    public List<T> filterByPredicat(BaseRDF base, String predicat, String value) {
	return getDao().filterByPredicat(base, predicat, value);
    }

    @Override
    public T findByPredicat(BaseRDF base, String predicat, String value) {
	return getDao().findByPredicat(base, predicat, value);
    }

    @Override
    public T findByIri(BaseRDF base, String iri) {
	return getDao().findByIri(base, iri);
    }

}
