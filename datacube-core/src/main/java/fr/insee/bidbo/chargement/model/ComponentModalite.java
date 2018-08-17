package fr.insee.bidbo.chargement.model;

import fr.insee.bidbo.model.rmes.DataCubeComponent;
import fr.insee.bidbo.model.rmes.Modalite;

public class ComponentModalite implements Comparable<ComponentModalite> {

    private DataCubeComponent component;
    private Modalite modalite;

    public ComponentModalite(DataCubeComponent component, Modalite modalite) {
	super();
	this.component = component;
	this.modalite = modalite;
    }

    public DataCubeComponent getComponent() {
	return component;
    }

    public void setComponent(DataCubeComponent component) {
	this.component = component;
    }

    public Modalite getModalite() {
	return modalite;
    }

    public void setModalite(Modalite modalite) {
	this.modalite = modalite;
    }

    @Override
    public int compareTo(ComponentModalite o) {
	return component.getIri().compareTo(o.getComponent().getIri());
    }

}
