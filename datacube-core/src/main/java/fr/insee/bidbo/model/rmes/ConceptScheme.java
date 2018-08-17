package fr.insee.bidbo.model.rmes;

import java.util.List;

import fr.insee.bidbo.rdfinsee.annotation.RdfInseeIRI;
import fr.insee.bidbo.rdfinsee.annotation.RdfInseeObjectIRI;
import fr.insee.bidbo.rdfinsee.annotation.RdfInseeType;
import fr.insee.bidbo.rdfinsee.annotation.RdfInseeValue;
import fr.insee.bidbo.rdfinsee.enumeration.Langue;
import fr.insee.bidbo.rdfinsee.vocabulary.RDFSStr;
import fr.insee.bidbo.rdfinsee.vocabulary.SKOSStr;

@RdfInseeType(SKOSStr.CONCEPT_SCHEME)
public class ConceptScheme {

    @RdfInseeIRI
    private String iri;
    @RdfInseeValue(SKOSStr.NOTATION)
    private String code;
    @RdfInseeValue(value = SKOSStr.PREF_LABEL, lang = Langue.FR)
    private String libelleFr;
    @RdfInseeValue(value = SKOSStr.PREF_LABEL, lang = Langue.EN, isNullable = true)
    private String libelleEn;
    @RdfInseeObjectIRI(value = RDFSStr.SEE_ALSO)
    private String iriClass;
    private List<Modalite> modalites;
    private List<DataCube> datacubes;

    public String getIriClass() {
	return iriClass;
    }

    public void setIriClass(String iriClass) {
	this.iriClass = iriClass;
    }

    public List<DataCube> getDatacubes() {
	return datacubes;
    }

    public void setDatacubes(List<DataCube> datacubes) {
	this.datacubes = datacubes;
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

    public String getCode() {
	return code;
    }

    public void setCode(String code) {
	this.code = code;
    }

    public String getLibelleFr() {
	return libelleFr;
    }

    public void setLibelleFr(String libelleFr) {
	this.libelleFr = libelleFr;
    }

    public String getLibelleEn() {
	return libelleEn;
    }

    public void setLibelleEn(String libelleEn) {
	this.libelleEn = libelleEn;
    }

}
