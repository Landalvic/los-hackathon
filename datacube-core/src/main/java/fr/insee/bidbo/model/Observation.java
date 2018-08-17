package fr.insee.bidbo.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import fr.insee.bidbo.rdfinsee.annotation.RdfInseeIRI;
import fr.insee.bidbo.rdfinsee.annotation.RdfInseeObjectIRI;
import fr.insee.bidbo.rdfinsee.annotation.RdfInseeType;
import fr.insee.bidbo.rdfinsee.annotation.RdfInseeValue;
import fr.insee.bidbo.vocabulary.str.QBStr;
import fr.insee.bidbo.vocabulary.str.SdmxDimensionStr;

@RdfInseeType(QBStr.OBSERVATION_CLASS)
@JsonInclude(Include.NON_NULL)
public class Observation implements Attachable {

    @RdfInseeIRI
    private String iri;
    @RdfInseeObjectIRI(QBStr.DATA_SET)
    private String iriDataset;
    @RdfInseeObjectIRI(QBStr.MEASURE_TYPE)
    private String iriMeasureType;
    @RdfInseeValue(SdmxDimensionStr.TIME_PERIOD)
    private String timePeriod;
    @RdfInseeValue(value = "iriMeasureType", reference = true)
    private Double valeur;
    private List<CodeVariableModalite> attributs;
    private List<CodeVariableModalite> dimensions;

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

    public String getIriDataset() {
	return iriDataset;
    }

    public void setIriDataset(String iriDataset) {
	this.iriDataset = iriDataset;
    }

    public String getIriMeasureType() {
	return iriMeasureType;
    }

    public void setIriMeasureType(String iriMeasureType) {
	this.iriMeasureType = iriMeasureType;
    }

    public String getTimePeriod() {
	return timePeriod;
    }

    public void setTimePeriod(String timePeriod) {
	this.timePeriod = timePeriod;
    }

    public Double getValeur() {
	return valeur;
    }

    public void setValeur(Double valeur) {
	this.valeur = valeur;
    }

}
