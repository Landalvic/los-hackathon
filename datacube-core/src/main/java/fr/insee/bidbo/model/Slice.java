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
import fr.insee.bidbo.rdfinsee.vocabulary.DCTERMSStr;
import fr.insee.bidbo.vocabulary.str.QBStr;

@RdfInseeType(QBStr.SLICE_CLASS)
@JsonInclude(Include.NON_NULL)
@XmlRootElement(name = "serie")
public class Slice implements Attachable {

    @RdfInseeIRI
    private String iri;
    @RdfInseeObjectIRI(QBStr.SLICE_STRUCTURE)
    private String iriSliceKey;
    @RdfInseeObjectIRI(QBStr.DATA_SET)
    private String iriDataSet;
    @RdfInseeValue(DCTERMSStr.IDENTIFIER)
    private String code;
    @RdfInseeObjectIRI(QBStr.MEASURE_TYPE)
    private String iriMeasureType;
    private List<Observation> observations;
    private List<CodeVariableModalite> attributs;
    private List<CodeVariableModalite> dimensions;

    public String getIriMeasureType() {
	return iriMeasureType;
    }

    public void setIriMeasureType(String iriMeasureType) {
	this.iriMeasureType = iriMeasureType;
    }

    public String getCode() {
	return code;
    }

    public void setCode(String code) {
	this.code = code;
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

    public String getIriDataSet() {
	return iriDataSet;
    }

    public void setIriDataSet(String iriDataSet) {
	this.iriDataSet = iriDataSet;
    }

    @XmlElementWrapper(name = "observations")
    @XmlElement(name = "observation")
    public List<Observation> getObservations() {
	return observations;
    }

    public void setObservations(List<Observation> observations) {
	this.observations = observations;
    }

    public String getIri() {
	return iri;
    }

    public void setIri(String iri) {
	this.iri = iri;
    }

    public String getIriSliceKey() {
	return iriSliceKey;
    }

    public void setIriSliceKey(String iriSliceKey) {
	this.iriSliceKey = iriSliceKey;
    }

}
