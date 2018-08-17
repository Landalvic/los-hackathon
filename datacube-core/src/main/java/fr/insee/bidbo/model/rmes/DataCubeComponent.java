package fr.insee.bidbo.model.rmes;

import java.util.List;

public class DataCubeComponent {

    protected String iri;
    protected String libelleFr;
    protected String libelleEn;
    protected String code;
    protected String iriConcept;
    protected String iriRange;
    protected String iriConceptScheme;
    protected List<Modalite> modalites;
    protected String iriAttachment;

    public DataCubeComponent() {
	super();
    }

    public String getIriConcept() {
	return iriConcept;
    }

    public void setIriConcept(String iriConcept) {
	this.iriConcept = iriConcept;
    }

    public String getIriAttachment() {
	return iriAttachment;
    }

    public void setIriAttachment(String iriAttachment) {
	this.iriAttachment = iriAttachment;
    }

    public String getCode() {
	return code;
    }

    public void setCode(String code) {
	this.code = code;
    }

    public String getLibelleFr() {
	return libelleFr;
    }

    public String getLibelleEn() {
	return libelleEn;
    }

    public void setLibelleEn(String libelleEn) {
	this.libelleEn = libelleEn;
    }

    public void setLibelleFr(String libelleFr) {
	this.libelleFr = libelleFr;
    }

    public List<Modalite> getModalites() {
	return modalites;
    }

    public void setModalites(List<Modalite> modalites) {
	this.modalites = modalites;
    }

    public String getIri() {
	return iri;
    }

    public void setIri(String iri) {
	this.iri = iri;
    }

    public String getIriRange() {
	return iriRange;
    }

    public void setIriRange(String iriRange) {
	this.iriRange = iriRange;
    }

    public String getIriConceptScheme() {
	return iriConceptScheme;
    }

    public void setIriConceptScheme(String iriConceptScheme) {
	this.iriConceptScheme = iriConceptScheme;
    }

    @Override
    public String toString() {
	return "DataCubeComponent [iri=" + iri + ", libelleFr=" + libelleFr + ", libelleEn=" + libelleEn + ", code="
		+ code + ", iriConcept=" + iriConcept + ", iriRange=" + iriRange + ", iriConceptScheme="
		+ iriConceptScheme + ", modalites=" + modalites + ", iriAttachment=" + iriAttachment + "]";
    }

}
