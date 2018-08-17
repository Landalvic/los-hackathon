package fr.insee.bidbo.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.BindingSet;

import fr.insee.bidbo.model.Attachable;
import fr.insee.bidbo.model.CodeVariableModalite;
import fr.insee.bidbo.model.rmes.DataCubeComponent;
import fr.insee.bidbo.model.rmes.Modalite;
import fr.insee.bidbo.rdfinsee.AbstractGenericDao;
import fr.insee.bidbo.rdfinsee.wrapper.RequestWrapper;
import fr.insee.bidbo.vocabulary.str.QBStr;

public abstract class AbstractAttachableDao<T extends Attachable> extends AbstractGenericDao<T> {

    protected abstract String iriClass();

    @Override
    protected RequestWrapper searchFields(RequestWrapper request, String iri, String predicat, String value) {
	super.searchFields(request, iri, predicat, value);
	if (request.getComplement() instanceof AttachableComplement) {
	    AttachableComplement attachement = (AttachableComplement) request.getComplement();
	    if (attachement.getDatacube().getAttributs() != null) {
		searchComponent(attachement.getDatacube().getAttributs(), request, attachement.getModalites());
	    }
	    if (attachement.getDatacube().getDimensions() != null) {
		searchComponent(attachement.getDatacube().getDimensions(), request, attachement.getModalites());
	    }
	    if (!StringUtils.isBlank(attachement.getTimePeriod())
		    && StringUtils.equals(QBStr.OBSERVATION_CLASS, iriClass())) {
		request.getWhere().append("FILTER (?timePeriod=\"" + attachement.getTimePeriod() + "\") ");
	    }
	}
	return request;
    }

    @Override
    protected T bindField(BindingSet bindingSet, RequestWrapper request) throws Exception {
	T t = super.bindField(bindingSet, request);
	if (request.getComplement() instanceof AttachableComplement) {
	    AttachableComplement attachement = (AttachableComplement) request.getComplement();
	    if (attachement.getDatacube().getAttributs() != null) {
		t.setAttributs(bindComponent(attachement.getDatacube().getAttributs(), bindingSet));
	    }
	    if (attachement.getDatacube().getDimensions() != null) {
		t.setDimensions(bindComponent(attachement.getDatacube().getDimensions(), bindingSet));
	    }
	}
	return t;
    }

    private void searchComponent(List<? extends DataCubeComponent> components, RequestWrapper request,
	    Map<String, List<String>> modalitesFiltrees) {
	for (DataCubeComponent component : components) {
	    String attachment = !StringUtils.isBlank(component.getIriAttachment()) ? component.getIriAttachment()
		    : QBStr.OBSERVATION_CLASS;
	    if (StringUtils.equals(attachment, iriClass())) {
		request.getSelect().append("?" + component.getCode() + " ");
		request.getWhere().append(
			"OPTIONAL { ?iri" + delimiter(component.getIri()) + " ?" + component.getCode() + " } ");
		if (modalitesFiltrees.get(component.getCode()) != null) {
		    List<String> modalites = component
			    .getModalites()
			    .stream()
			    .filter(modalite -> modalitesFiltrees.get(component.getCode()).contains(modalite.getCode()))
			    .map(modalite -> "?" + component.getCode() + " = " + delimiter(modalite.getIri()))
			    .collect(Collectors.toList());
		    if (modalites.size() > 0) {
			request.getWhere().append("FILTER (" + String.join(" || ", modalites) + ") ");
		    }
		}
	    }
	}
    }

    private List<CodeVariableModalite> bindComponent(List<? extends DataCubeComponent> components,
	    BindingSet bindingSet) {
	List<CodeVariableModalite> attributs = new ArrayList<>();
	for (DataCubeComponent component : components) {
	    String attachment = !StringUtils.isBlank(component.getIriAttachment()) ? component.getIriAttachment()
		    : QBStr.OBSERVATION_CLASS;
	    if (StringUtils.equals(attachment, iriClass())) {
		Value value = bindingSet.getValue(component.getCode());
		if (value != null) {
		    String bind = value.stringValue();
		    for (Modalite modalite : component.getModalites()) {
			if (StringUtils.equals(modalite.getIri(), bind)) {
			    attributs.add(new CodeVariableModalite(component.getCode(), modalite.getCode()));
			    break;
			}
		    }
		}
	    }
	}
	return attributs;
    }

}
