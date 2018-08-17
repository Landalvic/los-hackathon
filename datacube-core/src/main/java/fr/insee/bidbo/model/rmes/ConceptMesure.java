package fr.insee.bidbo.model.rmes;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import fr.insee.bidbo.rdfinsee.annotation.RdfInseeIRI;
import fr.insee.bidbo.rdfinsee.annotation.RdfInseeType;
import fr.insee.bidbo.rdfinsee.annotation.RdfInseeValue;
import fr.insee.bidbo.rdfinsee.enumeration.Langue;
import fr.insee.bidbo.rdfinsee.vocabulary.DCTERMSStr;
import fr.insee.bidbo.rdfinsee.vocabulary.RDFSStr;
import fr.insee.bidbo.vocabulary.str.QBStr;

@RdfInseeType(QBStr.MEASURE_PROPERTY)
@JsonInclude(Include.NON_NULL)
@XmlRootElement(name = "concept")
public class ConceptMesure implements GenericConcept {

    @RdfInseeIRI
    private String iri;
    @RdfInseeValue(DCTERMSStr.IDENTIFIER)
    private String code;
    @RdfInseeValue(value = RDFSStr.LABEL, lang = Langue.FR)
    private String libelleFr;
    @RdfInseeValue(value = RDFSStr.LABEL, lang = Langue.EN, isNullable = true)
    private String libelleEn;
    private List<DataCube> datacubes;

    @Override
    public List<DataCube> getDatacubes() {
	return datacubes;
    }

    @Override
    public void setDatacubes(List<DataCube> datacubes) {
	this.datacubes = datacubes;
    }

    @Override
    public String getCode() {
	return code;
    }

    @Override
    public void setCode(String code) {
	this.code = code;
    }

    @Override
    public String getIri() {
	return iri;
    }

    @Override
    public void setIri(String iri) {
	this.iri = iri;
    }

    @Override
    public String getLibelleFr() {
	return libelleFr;
    }

    @Override
    public void setLibelleFr(String libelleFr) {
	this.libelleFr = libelleFr;
    }

    @Override
    public String getLibelleEn() {
	return libelleEn;
    }

    @Override
    public void setLibelleEn(String libelleEn) {
	this.libelleEn = libelleEn;
    }

    @Override
    public boolean isMesure() {
	return true;
    }

    @Override
    public void setMesure(boolean mesure) {
    }

    @Override
    public String toString() {
	return "Concept [iri=" + iri + ", code=" + code + ", libelleFr=" + libelleFr + ", libelleEn=" + libelleEn + "]";
    }

}
