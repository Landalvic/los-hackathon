package fr.insee.bidbo.rdfinsee;

import java.util.Objects;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;

public class RemoveStatement implements Statement {

    private static final long serialVersionUID = 1L;
    private final Resource subject;
    private final IRI predicate;
    private final Value object;
    private final Resource context;

    public RemoveStatement(Resource subject, IRI predicate, Value object, Resource context) {
	this.subject = subject;
	this.predicate = predicate;
	this.object = object;
	this.context = context;
    }

    @Override
    public Resource getSubject() {
	return subject;
    }

    @Override
    public IRI getPredicate() {
	return predicate;
    }

    @Override
    public Value getObject() {
	return object;
    }

    @Override
    public Resource getContext() {
	return context;
    }

    @Override
    public boolean equals(Object other) {
	if (this == other) {
	    return true;
	}

	if (other instanceof Statement) {
	    Statement that = (Statement) other;

	    return object.equals(that.getObject()) && subject.equals(that.getSubject())
		    && predicate.equals(that.getPredicate()) && Objects.equals(getContext(), that.getContext());
	}

	return false;
    }

    @Override
    public int hashCode() {
	return Objects.hash(subject, predicate, object, getContext());
    }

    @Override
    public String toString() {
	StringBuilder sb = new StringBuilder(256);

	sb.append("(");
	sb.append(getSubject());
	sb.append(", ");
	sb.append(getPredicate());
	sb.append(", ");
	sb.append(getObject());
	sb.append(")");

	return sb.toString();
    }

}
