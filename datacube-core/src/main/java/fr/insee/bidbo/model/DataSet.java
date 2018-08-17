package fr.insee.bidbo.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import fr.insee.bidbo.rdfinsee.annotation.RdfInseeIRI;
import fr.insee.bidbo.rdfinsee.annotation.RdfInseeObjectIRI;
import fr.insee.bidbo.rdfinsee.annotation.RdfInseeType;
import fr.insee.bidbo.rdfinsee.annotation.RdfInseeValue;
import fr.insee.bidbo.rdfinsee.enumeration.Langue;
import fr.insee.bidbo.rdfinsee.vocabulary.RDFSStr;
import fr.insee.bidbo.vocabulary.str.QBStr;

@RdfInseeType(QBStr.DATA_SET_CLASS)
@JsonInclude(Include.NON_EMPTY)
@XmlRootElement(name = "dataset")
public class DataSet implements Attachable {

    @RdfInseeIRI
    private String iri;
    @RdfInseeValue(value = RDFSStr.LABEL, lang = Langue.FR)
    private String libelleFr;
    @RdfInseeValue(value = RDFSStr.LABEL, lang = Langue.EN, isNullable = true)
    private String libelleEn;
    @RdfInseeObjectIRI(QBStr.STRUCTURE)
    private String iriCube;
    private List<Observation> observations;
    private List<CodeVariableModalite> attributs;
    private List<CodeVariableModalite> dimensions;

    @XmlElementWrapper(name = "observations")
    @XmlElement(name = "observation")
    public List<Observation> getObservations() {
	return observations;
    }

    public void setObservations(List<Observation> observations) {
	this.observations = observations;
    }

    @XmlElementWrapper(name = "attributs")
    @XmlElement(name = "attribut")
    @Override
    public List<CodeVariableModalite> getAttributs() {
	return attributs;
    }

    @Override
    public void setAttributs(List<CodeVariableModalite> attributs) {
	this.attributs = attributs;
    }

    @XmlElementWrapper(name = "dimensions")
    @XmlElement(name = "dimension")
    @Override
    public List<CodeVariableModalite> getDimensions() {
	return dimensions;
    }

    @Override
    public void setDimensions(List<CodeVariableModalite> dimensions) {
	this.dimensions = dimensions;
    }

    public String getIri() {
	return iri;
    }

    public void setIri(String iri) {
	this.iri = iri;
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

    public String getIriCube() {
	return iriCube;
    }

    public void setIriCube(String iriCube) {
	this.iriCube = iriCube;
    }

}
